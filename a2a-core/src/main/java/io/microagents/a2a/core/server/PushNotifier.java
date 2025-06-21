package io.microagents.a2a.core.server;

import io.microagents.a2a.core.types.core.Task;
import io.microagents.a2a.core.types.notifications.PushNotificationConfig;
import reactor.core.publisher.Mono;

/**
 * PushNotifier interface to store, retrieve push notification configurations for tasks and send
 * push notifications.
 *
 * <p>The PushNotifier provides a way to configure webhook notifications for task updates and
 * automatically send notifications when tasks reach certain states. This enables asynchronous
 * communication patterns where clients can be notified of task progress without polling.
 *
 * <p>All operations return {@link Mono} to support reactive programming patterns and non-blocking
 * I/O operations.
 *
 * @since 0.1.0
 */
public interface PushNotifier {

  /**
   * Sets or updates the push notification configuration for a task.
   *
   * <p>If a configuration already exists for the task, it will be replaced. The configuration
   * defines the webhook URL and authentication details for sending notifications.
   *
   * @param taskId the unique identifier of the task
   * @param notificationConfig the push notification configuration
   * @return a Mono that completes when the configuration is saved
   * @throws IllegalArgumentException if taskId is null or empty, or notificationConfig is null
   */
  Mono<Void> setInfo(String taskId, PushNotificationConfig notificationConfig);

  /**
   * Retrieves the push notification configuration for a task.
   *
   * <p>Returns an empty Mono if no configuration exists for the given task ID.
   *
   * @param taskId the unique identifier of the task
   * @return a Mono containing the push notification configuration if found, or empty Mono if not
   *     found
   * @throws IllegalArgumentException if taskId is null or empty
   */
  Mono<PushNotificationConfig> getInfo(String taskId);

  /**
   * Deletes the push notification configuration for a task.
   *
   * <p>This operation succeeds even if no configuration exists for the given task ID. Once deleted,
   * no further notifications will be sent for the task.
   *
   * @param taskId the unique identifier of the task
   * @return a Mono that completes when the configuration is deleted
   * @throws IllegalArgumentException if taskId is null or empty
   */
  Mono<Void> deleteInfo(String taskId);

  /**
   * Sends a push notification containing the latest task state.
   *
   * <p>This method checks if a push notification configuration exists for the task and sends an
   * HTTP POST request to the configured webhook URL with the task data as JSON payload. If no
   * configuration exists, no notification is sent.
   *
   * <p>The task data will be serialized to JSON and sent in the request body. Authentication
   * headers will be added based on the notification configuration.
   *
   * @param task the task to send notification for
   * @return a Mono that completes when the notification is sent (or skipped if no config)
   * @throws IllegalArgumentException if task is null
   */
  Mono<Void> sendNotification(Task task);

  /**
   * Checks if a push notification configuration exists for a task.
   *
   * <p>This method provides a more efficient way to check for configuration existence without
   * retrieving the full configuration object.
   *
   * @param taskId the unique identifier of the task
   * @return a Mono containing true if configuration exists, false otherwise
   * @throws IllegalArgumentException if taskId is null or empty
   */
  Mono<Boolean> hasInfo(String taskId);
}
