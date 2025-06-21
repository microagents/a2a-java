package io.microagents.a2a.core.server;

import io.microagents.a2a.core.types.core.Event;
import io.microagents.a2a.core.types.core.Message;
import io.microagents.a2a.core.types.core.Task;
import io.microagents.a2a.core.types.core.TaskState;
import io.microagents.a2a.core.types.core.TaskStatusUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * ResultAggregator processes event streams from AgentExecutor implementations.
 *
 * <p>There are three main consumption patterns provided:
 *
 * <ol>
 *   <li><b>Streaming Processing</b>: {@link #consumeAndEmit(EventConsumer)} constructs the updated
 *       task as events arrive and re-emits those events for downstream consumers
 *   <li><b>Blocking Processing</b>: {@link #consumeAll(EventConsumer)} processes the entire stream
 *       and returns the final Task or Message object
 *   <li><b>Interruptible Processing</b>: {@link #consumeAndBreakOnInterrupt(EventConsumer)}
 *       processes events until completion or an interruptible state (like {@code auth_required}) is
 *       encountered
 * </ol>
 *
 * <p>This class provides the core event consumption patterns that enable the A2A protocol's
 * sophisticated task lifecycle management and streaming capabilities.
 */
public class ResultAggregator {

  private static final Logger logger = LoggerFactory.getLogger(ResultAggregator.class);

  private final TaskManager taskManager;
  private volatile Message message;

  /**
   * Creates a new ResultAggregator.
   *
   * @param taskManager the TaskManager instance to use for processing events and managing task
   *     state
   */
  public ResultAggregator(TaskManager taskManager) {
    this.taskManager = taskManager;
  }

  /**
   * Returns the current aggregated result (Task or Message).
   *
   * <p>This is the latest state processed from the event stream.
   *
   * @return the current Task object managed by the TaskManager, or the final Message if one was
   *     received, or null if no result has been produced yet
   */
  public Mono<Object> getCurrentResult() {
    if (message != null) {
      return Mono.just(message);
    }
    return taskManager.getTask().cast(Object.class);
  }

  /**
   * Processes the event stream from the consumer, updates the task state, and re-emits the same
   * events.
   *
   * <p>Useful for streaming scenarios where the server needs to observe and process events (e.g.,
   * save task state, send push notifications) while forwarding them to the client.
   *
   * @param consumer the EventConsumer to read events from
   * @return a Flux of Event objects consumed from the EventConsumer
   */
  public Flux<Event> consumeAndEmit(EventConsumer consumer) {
    return consumer
        .consumeAll()
        .flatMap(event -> taskManager.processEvent(event).then(Mono.just(event)))
        .doOnNext(
            event ->
                logger.debug("Processed and emitted event: {}", event.getClass().getSimpleName()));
  }

  /**
   * Processes the entire event stream from the consumer and returns the final result.
   *
   * <p>Blocks until the event stream ends (queue is closed after final event or exception).
   *
   * @param consumer the EventConsumer to read events from
   * @return the final Task object or Message object after the stream is exhausted, or null if the
   *     stream ends without producing a final result
   */
  public Mono<Object> consumeAll(EventConsumer consumer) {
    return consumer
        .consumeAll()
        .flatMap(
            event -> {
              if (event instanceof Message msg) {
                this.message = msg;
                return Mono.just(msg);
              }
              return taskManager.processEvent(event).then(Mono.empty());
            })
        .cast(Object.class)
        .last()
        .onErrorResume(
            error -> {
              logger.debug("No final message found, returning current task");
              return taskManager.getTask().cast(Object.class);
            })
        .switchIfEmpty(taskManager.getTask().cast(Object.class));
  }

  /**
   * Processes the event stream until completion or an interruptible state is encountered.
   *
   * <p>Interruptible states currently include {@link TaskState#auth_required}. If interrupted,
   * consumption continues in a background task.
   *
   * @param consumer the EventConsumer to read events from
   * @return a Mono containing an InterruptionResult with the current aggregated result and whether
   *     the consumption was interrupted
   */
  public Mono<InterruptionResult> consumeAndBreakOnInterrupt(EventConsumer consumer) {
    return consumer
        .consumeAll()
        .flatMap(
            event -> {
              if (event instanceof Message msg) {
                this.message = msg;
                return Mono.just(new InterruptionResult(msg, false));
              }

              return taskManager
                  .processEvent(event)
                  .then(
                      Mono.defer(
                          () -> {
                            // Check for auth_required state
                            if (isAuthRequiredState(event)) {
                              logger.debug(
                                  "Encountered auth-required task: breaking synchronous message/send flow");

                              // Continue consuming in background
                              continueConsumingInBackground(consumer);

                              return taskManager
                                  .getTask()
                                  .map(task -> new InterruptionResult(task, true));
                            }
                            return Mono.empty();
                          }));
            })
        .cast(InterruptionResult.class)
        .next()
        .switchIfEmpty(taskManager.getTask().map(task -> new InterruptionResult(task, false)));
  }

  /**
   * Continues processing an event stream in a background task.
   *
   * <p>Used after an interruptible state (like auth_required) is encountered in the synchronous
   * consumption flow.
   *
   * @param consumer the EventConsumer with remaining events to process
   */
  private void continueConsumingInBackground(EventConsumer consumer) {
    consumer
        .consumeAll()
        .flatMap(event -> taskManager.processEvent(event))
        .onErrorResume(
            error -> {
              logger.warn("Error in background consumption: {}", error.getMessage(), error);
              return Mono.empty();
            })
        .subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic())
        .subscribe(
            result -> logger.debug("Background consumption processed event"),
            error -> logger.warn("Background consumption failed: {}", error.getMessage()),
            () -> logger.debug("Background consumption completed"));
  }

  /**
   * Checks if an event represents an auth_required state.
   *
   * @param event the event to check
   * @return true if the event indicates auth_required state
   */
  private boolean isAuthRequiredState(Event event) {
    if (event instanceof Task task) {
      return (task.getStatus() != null && task.getStatus().getState() == TaskState.AUTH_REQUIRED);
    }
    if (event instanceof TaskStatusUpdateEvent statusEvent) {
      return (statusEvent.getStatus() != null
          && statusEvent.getStatus().getState() == TaskState.AUTH_REQUIRED);
    }
    return false;
  }

  /**
   * Result of an interruptible consumption operation.
   *
   * @param result the current aggregated result (Task or Message) at the point of completion or
   *     interruption
   * @param interrupted whether the consumption was interrupted (true) or completed naturally
   *     (false)
   */
  public record InterruptionResult(Object result, boolean interrupted) {}
}
