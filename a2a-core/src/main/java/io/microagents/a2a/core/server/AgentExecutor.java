package io.microagents.a2a.core.server;

import reactor.core.publisher.Mono;

/**
 * Agent Executor interface.
 *
 * <p>Implementations of this interface contain the core logic of an agent, executing tasks based on
 * requests and publishing updates to an event queue. The AgentExecutor is responsible for the
 * actual AI agent functionality, while the A2A server framework handles the communication protocol.
 *
 * <p>Agent executors should:
 *
 * <ul>
 *   <li>Read necessary information from the RequestContext
 *   <li>Perform the agent's core logic (AI processing, tool calls, etc.)
 *   <li>Publish events to the EventQueue (Task updates, Messages, Status changes)
 *   <li>Handle cancellation requests gracefully
 * </ul>
 *
 * <p>All methods return {@link Mono} to support reactive programming patterns and non-blocking
 * execution.
 *
 * @since 0.1.0
 */
public interface AgentExecutor {
  /**
   * Executes the agent's logic for a given request context.
   *
   * <p>The agent should read necessary information from the context and publish Task or Message
   * events, or TaskStatusUpdateEvent / TaskArtifactUpdateEvent to the event queue. This method
   * should complete once the agent's execution for this request is finished or yields control
   * (e.g., enters an input-required state).
   *
   * <p>Example event flow:
   *
   * <pre>
   * 1. Publish initial Task with WORKING status
   * 2. Process the user's message
   * 3. Publish intermediate TaskStatusUpdateEvent (optional)
   * 4. Publish TaskArtifactUpdateEvent for any outputs
   * 5. Publish final Task with COMPLETED status or final Message
   * </pre>
   *
   * @param context the request context containing the message, task ID, etc.
   * @param eventQueue the queue to publish events to
   * @return a Mono that completes when the agent execution is finished
   */
  Mono<Void> execute(RequestContext context, EventQueue eventQueue);

  /**
   * Requests the agent to cancel an ongoing task.
   *
   * <p>The agent should attempt to stop the task identified by the task_id in the context and
   * publish a TaskStatusUpdateEvent with state TaskState.CANCELED to the event queue.
   *
   * <p>If the task cannot be cancelled (e.g., already completed), the agent should still respond
   * with the current task status. If the task is not found, an appropriate error should be
   * published.
   *
   * @param context the request context containing the task ID to cancel
   * @param eventQueue the queue to publish the cancellation status update to
   * @return a Mono that completes when the cancellation attempt is finished
   */
  Mono<Void> cancel(RequestContext context, EventQueue eventQueue);

  /**
   * Gets the name or identifier of this agent executor.
   *
   * <p>This is used for logging, monitoring, and debugging purposes. The default implementation
   * returns the class name.
   *
   * @return the agent executor name
   */
  default String getName() {
    return getClass().getSimpleName();
  }

  /**
   * Gets a description of what this agent executor does.
   *
   * <p>This is used for documentation and debugging purposes. Implementations should provide a
   * brief description of the agent's capabilities and purpose.
   *
   * @return a description of the agent executor
   */
  default String getDescription() {
    return "A2A Agent Executor";
  }

  /**
   * Performs any necessary initialization before the agent starts processing requests.
   *
   * <p>This method is called once when the agent is first created or registered. It can be used to
   * set up resources, load models, establish connections, etc. The default implementation does
   * nothing.
   *
   * @return a Mono that completes when initialization is finished
   */
  default Mono<Void> initialize() {
    return Mono.empty();
  }

  /**
   * Performs any necessary cleanup when the agent is shutting down.
   *
   * <p>This method is called when the agent is being destroyed or when the server is shutting down.
   * It can be used to clean up resources, save state, close connections, etc. The default
   * implementation does nothing.
   *
   * @return a Mono that completes when cleanup is finished
   */
  default Mono<Void> cleanup() {
    return Mono.empty();
  }
}
