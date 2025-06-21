package io.microagents.a2a.examples;

import io.microagents.a2a.core.server.AgentExecutor;
import io.microagents.a2a.core.server.EventQueue;
import io.microagents.a2a.core.server.RequestContext;
import io.microagents.a2a.core.types.core.Event;
import io.microagents.a2a.core.types.core.Message;
import io.microagents.a2a.core.types.core.Role;
import io.microagents.a2a.core.types.core.Task;
import io.microagents.a2a.core.types.core.TaskState;
import io.microagents.a2a.core.types.core.TaskStatus;
import io.microagents.a2a.core.types.core.TaskStatusUpdateEvent;
import java.util.ArrayList;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Simple echo agent executor for demonstration and testing.
 *
 * <p>This agent executor provides a basic echo functionality that responds to user messages by
 * repeating them back with an "Echo: " prefix. It demonstrates the proper pattern for implementing
 * agent executors and can serve as a reference implementation for more complex agents.
 *
 * <p>The echo agent follows this execution flow:
 *
 * <ol>
 *   <li>Creates a new task in WORKING state
 *   <li>Publishes the initial task
 *   <li>Processes the user's message
 *   <li>Creates an echo response message
 *   <li>Updates task to COMPLETED state
 *   <li>Publishes the final task and response message
 * </ol>
 *
 * @since 0.1.0
 */
public class EchoAgentExecutor implements AgentExecutor {
  private static final Logger logger = LoggerFactory.getLogger(EchoAgentExecutor.class);

  @Override
  public Mono<Void> execute(RequestContext context, EventQueue eventQueue) {
    logger.debug("Starting echo agent execution for context: {}", context.getTaskId());

    return Mono.fromRunnable(
            () -> {
              // Extract user input
              String userInput = context.getUserInput();
              logger.debug("User input: {}", userInput);

              // Generate task ID if not present
              String taskId = context.getTaskId();
              if (taskId == null) {
                taskId = UUID.randomUUID().toString();
              }

              String contextId = context.getContextId();
              if (contextId == null) {
                contextId = "default";
              }

              // Create initial task in WORKING state
              TaskStatus workingStatus =
                  TaskStatus.builder(TaskState.WORKING).withCurrentTimestamp().build();

              Task workingTask =
                  Task.builder(taskId, contextId, workingStatus)
                      .withHistory(
                          context.getMessage() != null
                              ? new ArrayList<>(java.util.List.of(context.getMessage()))
                              : new ArrayList<>())
                      .withArtifacts(new ArrayList<>())
                      .withMetadata(
                          context.getParams() != null ? context.getParams().getMetadata() : null)
                      .build();

              // Publish initial working task
              eventQueue.enqueueEvent(Event.task(workingTask)).subscribe();

              // Generate echo response
              String echoText = generateEchoResponse(userInput, context);
              Message echoMessage = Message.ofText(taskId, Role.AGENT, echoText);

              // Update task history with echo message
              java.util.List<Message> history = new ArrayList<>(workingTask.getHistory());
              history.add(echoMessage);

              // Create completed task
              TaskStatus completedStatus =
                  TaskStatus.builder(TaskState.COMPLETED).withCurrentTimestamp().build();

              Task completedTask =
                  Task.builder(taskId, contextId, completedStatus)
                      .withHistory(history)
                      .withArtifacts(workingTask.getArtifacts())
                      .withMetadata(workingTask.getMetadata())
                      .build();

              // Publish completed task and final message
              eventQueue.enqueueEvent(Event.task(completedTask)).subscribe();
              eventQueue.enqueueEvent(Event.message(echoMessage)).subscribe();

              logger.debug("Echo agent execution completed for task: {}", taskId);
            })
        .then();
  }

  @Override
  public Mono<Void> cancel(RequestContext context, EventQueue eventQueue) {
    logger.debug("Cancelling echo agent task: {}", context.getTaskId());

    return Mono.fromRunnable(
            () -> {
              String taskId = context.getTaskId();
              if (taskId == null) {
                logger.warn("Cannot cancel task: no task ID provided");
                return;
              }

              // Create cancelled status
              TaskStatus cancelledStatus =
                  TaskStatus.builder(TaskState.CANCELED).withCurrentTimestamp().build();

              // Create status update event
              TaskStatusUpdateEvent cancelEvent =
                  TaskStatusUpdateEvent.builder(taskId, cancelledStatus)
                      .withContextId(context.getContextId())
                      .withFinal(true)
                      .build();

              // Publish cancellation event
              eventQueue.enqueueEvent(cancelEvent).subscribe();

              logger.debug("Published cancellation event for task: {}", taskId);
            })
        .then();
  }

  @Override
  public String getName() {
    return "EchoAgent";
  }

  @Override
  public String getDescription() {
    return "Simple echo agent that repeats user messages with an 'Echo: ' prefix";
  }

  @Override
  public Mono<Void> initialize() {
    logger.info("Initializing Echo Agent Executor");
    return Mono.empty();
  }

  @Override
  public Mono<Void> cleanup() {
    logger.info("Cleaning up Echo Agent Executor");
    return Mono.empty();
  }

  /**
   * Generates an echo response based on the user input.
   *
   * @param userInput the text input from the user
   * @param context the request context for additional information
   * @return the echo response text
   */
  private String generateEchoResponse(String userInput, RequestContext context) {
    if (userInput == null || userInput.trim().isEmpty()) {
      return "Echo: [no text content]";
    }

    String userName =
        context.getCallContext() != null
            ? context.getCallContext().getUser().getUserName()
            : "unknown";

    return String.format("Echo: %s (from user: %s)", userInput.trim(), userName);
  }
}
