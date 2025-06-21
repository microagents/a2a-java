package io.microagents.a2a.core.server;

import io.microagents.a2a.core.types.core.Event;
import io.microagents.a2a.core.types.core.MessageSendParams;
import io.microagents.a2a.core.types.core.Task;
import io.microagents.a2a.core.types.core.TaskIdParams;
import io.microagents.a2a.core.types.core.TaskPushNotificationConfig;
import io.microagents.a2a.core.types.core.TaskQueryParams;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Abstract base class defining the interface for handling A2A protocol requests.
 *
 * <p>Implementations of this class provide the business logic for processing various A2A methods
 * including message sending, task management, and push notifications. All methods are asynchronous
 * and return reactive types (Mono/Flux) for non-blocking operation.
 *
 * @since 0.1.0
 */
public abstract class RequestHandler {

  /**
   * Handles the 'tasks/get' method to retrieve task state and history.
   *
   * @param params The task query parameters including task ID and optional history length
   * @param context The server call context containing authentication and state information
   * @return A Mono containing the Task if found, or empty if not found
   */
  public abstract Mono<Task> onGetTask(TaskQueryParams params, ServerCallContext context);

  /**
   * Handles the 'tasks/cancel' method to cancel an ongoing task.
   *
   * @param params The task ID parameters
   * @param context The server call context
   * @return A Mono containing the updated Task if cancelled, or empty if not found
   */
  public abstract Mono<Task> onCancelTask(TaskIdParams params, ServerCallContext context);

  /**
   * Handles the 'message/send' method for non-streaming message processing.
   *
   * <p>This method creates new tasks, continues existing tasks, or restarts completed tasks based
   * on the message content and configuration.
   *
   * @param params The message send parameters including the message and optional configuration
   * @param context The server call context
   * @return A Mono containing either the final Task or Message response
   */
  public abstract Mono<Object> onMessageSend(MessageSendParams params, ServerCallContext context);

  /**
   * Handles the 'message/stream' method for streaming message processing.
   *
   * <p>This method streams events as they are produced during message processing. The default
   * implementation throws UnsupportedOperationException.
   *
   * @param params The message send parameters
   * @param context The server call context
   * @return A Flux of events (Message, Task, TaskStatusUpdateEvent, TaskArtifactUpdateEvent)
   * @throws UnsupportedOperationException if streaming is not supported
   */
  public Flux<Event> onMessageSendStream(MessageSendParams params, ServerCallContext context) {
    return Flux.error(
        new UnsupportedOperationException(
            "Streaming message sending is not supported by this handler"));
  }

  /**
   * Handles the 'tasks/pushNotificationConfig/set' method to configure push notifications.
   *
   * @param params The push notification configuration parameters
   * @param context The server call context
   * @return A Mono containing the updated push notification configuration
   */
  public abstract Mono<TaskPushNotificationConfig> onSetTaskPushNotificationConfig(
      TaskPushNotificationConfig params, ServerCallContext context);

  /**
   * Handles the 'tasks/pushNotificationConfig/get' method to retrieve push notification
   * configuration.
   *
   * @param params Either TaskIdParams or GetTaskPushNotificationConfigParams
   * @param context The server call context
   * @return A Mono containing the push notification configuration
   */
  public abstract Mono<TaskPushNotificationConfig> onGetTaskPushNotificationConfig(
      Object params, ServerCallContext context);

  /**
   * Handles the 'tasks/resubscribe' method to re-subscribe to a running task's event stream.
   *
   * <p>This allows clients to reconnect to ongoing task streams after disconnection. The default
   * implementation throws UnsupportedOperationException.
   *
   * @param params The task ID parameters
   * @param context The server call context
   * @return A Flux of events from the running task
   * @throws UnsupportedOperationException if resubscription is not supported
   */
  public Flux<Event> onResubscribeToTask(TaskIdParams params, ServerCallContext context) {
    return Flux.error(
        new UnsupportedOperationException("Task resubscription is not supported by this handler"));
  }
}
