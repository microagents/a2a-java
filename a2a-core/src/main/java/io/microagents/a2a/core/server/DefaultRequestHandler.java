package io.microagents.a2a.core.server;

import io.microagents.a2a.core.types.core.Event;
import io.microagents.a2a.core.types.core.Message;
import io.microagents.a2a.core.types.core.MessageSendParams;
import io.microagents.a2a.core.types.core.Task;
import io.microagents.a2a.core.types.core.TaskIdParams;
import io.microagents.a2a.core.types.core.TaskPushNotificationConfig;
import io.microagents.a2a.core.types.core.TaskQueryParams;
import io.microagents.a2a.core.types.exceptions.A2AServerException;
import io.microagents.a2a.examples.EchoAgentExecutor;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Default implementation of RequestHandler providing a simple echo agent.
 *
 * <p>This implementation serves as both a reference implementation and a functional echo agent that
 * responds to messages by echoing them back. It uses the Phase 6 task management components
 * (TaskStore, TaskManager, EventQueue, etc.) to provide proper task lifecycle management and event
 * streaming.
 *
 * @since 0.1.0
 */
public class DefaultRequestHandler extends RequestHandler {
  private static final Logger logger = LoggerFactory.getLogger(DefaultRequestHandler.class);

  private final TaskStore taskStore;
  private final PushNotifier pushNotifier;
  private final AgentExecutor agentExecutor;

  /** Creates a DefaultRequestHandler with default Phase 6 components. */
  public DefaultRequestHandler() {
    this.taskStore = new InMemoryTaskStore();
    this.pushNotifier = new SimplePushNotifier();
    this.agentExecutor = new EchoAgentExecutor();

    // Initialize agent executor
    this.agentExecutor.initialize().subscribe();
    logger.info("DefaultRequestHandler initialized with echo agent executor");
  }

  /**
   * Creates a DefaultRequestHandler with custom components.
   *
   * @param taskStore the task store implementation
   * @param pushNotifier the push notifier implementation
   * @param agentExecutor the agent executor implementation
   */
  public DefaultRequestHandler(
      TaskStore taskStore, PushNotifier pushNotifier, AgentExecutor agentExecutor) {
    this.taskStore = taskStore;
    this.pushNotifier = pushNotifier;
    this.agentExecutor = agentExecutor;

    // Initialize agent executor
    this.agentExecutor.initialize().subscribe();
    logger.info("DefaultRequestHandler initialized with custom components");
  }

  @Override
  public Mono<Task> onGetTask(TaskQueryParams params, ServerCallContext context) {
    logger.debug("Getting task: {}", params.getId());

    return taskStore
        .get(params.getId())
        .switchIfEmpty(
            Mono.error(
                new A2AServerException(
                    "Task not found: " + params.getId(),
                    -32001, // Task not found
                    null)))
        .map(task -> applyHistoryLimit(task, params.getHistoryLength()));
  }

  @Override
  public Mono<Task> onCancelTask(TaskIdParams params, ServerCallContext context) {
    logger.debug("Cancelling task: {}", params.getId());

    return taskStore
        .get(params.getId())
        .switchIfEmpty(
            Mono.error(
                new A2AServerException(
                    "Task not found: " + params.getId(),
                    -32001, // Task not found
                    null)))
        .flatMap(
            task -> {
              // Check if task can be cancelled
              if (task.getStatus().getState().name().matches("COMPLETED|CANCELED|FAILED")) {
                return Mono.error(
                    new A2AServerException(
                        "Task cannot be cancelled in state: " + task.getStatus().getState(),
                        -32002, // Task not cancellable
                        null));
              }

              // Use TaskManager and AgentExecutor for cancellation
              RequestContext requestContext =
                  RequestContext.withIds(null, params.getId(), task.getContextId(), context);
              EventQueue eventQueue = new EventQueue();
              EventConsumer eventConsumer = new EventConsumer(eventQueue);

              // Execute cancellation and consume the result
              return agentExecutor
                  .cancel(requestContext, eventQueue)
                  .then(eventConsumer.consumeOne())
                  .cast(Task.class)
                  .doOnNext(
                      cancelledTask -> {
                        // Send push notification if configured
                        pushNotifier.sendNotification(cancelledTask).subscribe();
                        eventQueue.close().subscribe();
                      })
                  .doOnError(
                      error -> {
                        logger.error("Error cancelling task", error);
                        eventQueue.close().subscribe();
                      });
            });
  }

  @Override
  public Mono<Object> onMessageSend(MessageSendParams params, ServerCallContext context) {
    logger.debug("Processing message send request");

    // Create RequestContext
    RequestContext requestContext = RequestContext.forNewRequest(params, context);

    // Create EventQueue and Consumer
    EventQueue eventQueue = new EventQueue();
    EventConsumer eventConsumer = new EventConsumer(eventQueue);

    // Execute agent logic and consume the final result
    return agentExecutor
        .execute(requestContext, eventQueue)
        .then(eventConsumer.consumeOne())
        .doOnNext(
            event -> {
              // Send push notification if result is a Task
              if (event instanceof Task task) {
                pushNotifier.sendNotification(task).subscribe();
              }
              eventQueue.close().subscribe();
            })
        .cast(Object.class)
        .doOnError(
            error -> {
              logger.error("Error processing message send", error);
              eventQueue.close().subscribe();
            });
  }

  @Override
  public Flux<Event> onMessageSendStream(MessageSendParams params, ServerCallContext context) {
    logger.debug("Processing streaming message send request");

    // Create RequestContext
    RequestContext requestContext = RequestContext.forNewRequest(params, context);

    // Create EventQueue and Consumer
    EventQueue eventQueue = new EventQueue();
    EventConsumer eventConsumer = new EventConsumer(eventQueue);

    // Execute agent logic and stream all events
    return agentExecutor
        .execute(requestContext, eventQueue)
        .thenMany(eventConsumer.consumeAll())
        .doOnNext(
            event -> {
              // Send push notification for Task events
              if (event instanceof Task task) {
                pushNotifier.sendNotification(task).subscribe();
              }
            })
        .doOnComplete(
            () -> {
              logger.debug("Streaming message send completed");
              eventQueue.close().subscribe();
            })
        .doOnError(
            error -> {
              logger.error("Error in streaming message send", error);
              eventQueue.close().subscribe();
            });
  }

  @Override
  public Mono<TaskPushNotificationConfig> onSetTaskPushNotificationConfig(
      TaskPushNotificationConfig params, ServerCallContext context) {
    logger.debug("Setting push notification config for task: {}", params.getTaskId());

    // Verify task exists
    return taskStore
        .exists(params.getTaskId())
        .filter(exists -> exists)
        .switchIfEmpty(
            Mono.error(
                new A2AServerException(
                    "Task not found: " + params.getTaskId(),
                    -32001, // Task not found
                    null)))
        .then(pushNotifier.setInfo(params.getTaskId(), params.getPushNotificationConfig()))
        .thenReturn(params);
  }

  @Override
  public Mono<TaskPushNotificationConfig> onGetTaskPushNotificationConfig(
      Object params, ServerCallContext context) {

    String taskId;
    if (params instanceof TaskIdParams taskIdParams) {
      taskId = taskIdParams.getId();
    } else {
      // Handle other parameter types if needed
      return Mono.error(
          new A2AServerException(
              "Unsupported parameter type for push notification config retrieval",
              -32602, // Invalid params
              null));
    }

    logger.debug("Getting push notification config for task: {}", taskId);

    return pushNotifier
        .getInfo(taskId)
        .switchIfEmpty(
            Mono.error(
                new A2AServerException(
                    "Push notification config not found for task: " + taskId,
                    -32001, // Not found
                    null)))
        .map(config -> TaskPushNotificationConfig.of(taskId, config));
  }

  /**
   * Gets the agent executor used by this handler.
   *
   * @return the agent executor
   */
  public AgentExecutor getAgentExecutor() {
    return agentExecutor;
  }

  /**
   * Gets the task store used by this handler.
   *
   * @return the task store
   */
  public TaskStore getTaskStore() {
    return taskStore;
  }

  /**
   * Gets the push notifier used by this handler.
   *
   * @return the push notifier
   */
  public PushNotifier getPushNotifier() {
    return pushNotifier;
  }

  /**
   * Applies history length limit to a task if specified.
   *
   * @param task the task to limit
   * @param historyLength the maximum history length (null for no limit)
   * @return the task with limited history
   */
  private Task applyHistoryLimit(Task task, Integer historyLength) {
    if (historyLength == null || historyLength <= 0 || task.getHistory() == null) {
      return task;
    }

    List<Message> history = task.getHistory();
    if (history.size() <= historyLength) {
      return task;
    }

    List<Message> limitedHistory =
        history.subList(Math.max(0, history.size() - historyLength), history.size());

    return Task.builder(task.getId(), task.getContextId(), task.getStatus())
        .withHistory(limitedHistory)
        .withArtifacts(task.getArtifacts())
        .withMetadata(task.getMetadata())
        .build();
  }
}
