package io.microagents.a2a.core.server;

import io.microagents.a2a.core.types.core.Task;
import io.microagents.a2a.core.types.notifications.PushNotificationConfig;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Simple in-memory implementation of PushNotifier for the core module.
 *
 * <p>This implementation stores push notification configurations in memory but does not actually
 * send HTTP notifications. It's intended for use in the core module where we don't have HTTP client
 * dependencies.
 *
 * <p>For full HTTP push notification functionality, use the Spring Boot module's
 * InMemoryPushNotifier implementation.
 *
 * @since 0.1.0
 */
public class SimplePushNotifier implements PushNotifier {
  private static final Logger logger = LoggerFactory.getLogger(SimplePushNotifier.class);

  private final Map<String, PushNotificationConfig> configurations = new ConcurrentHashMap<>();

  @Override
  public Mono<Void> setInfo(String taskId, PushNotificationConfig notificationConfig) {
    if (taskId == null || taskId.isEmpty()) {
      return Mono.error(new IllegalArgumentException("Task ID cannot be null or empty"));
    }
    if (notificationConfig == null) {
      return Mono.error(new IllegalArgumentException("Notification config cannot be null"));
    }

    configurations.put(taskId, notificationConfig);
    logger.debug("Stored push notification config for task: {}", taskId);
    return Mono.empty();
  }

  @Override
  public Mono<PushNotificationConfig> getInfo(String taskId) {
    if (taskId == null || taskId.isEmpty()) {
      return Mono.error(new IllegalArgumentException("Task ID cannot be null or empty"));
    }

    PushNotificationConfig config = configurations.get(taskId);
    return config != null ? Mono.just(config) : Mono.empty();
  }

  @Override
  public Mono<Void> deleteInfo(String taskId) {
    if (taskId == null || taskId.isEmpty()) {
      return Mono.error(new IllegalArgumentException("Task ID cannot be null or empty"));
    }

    configurations.remove(taskId);
    logger.debug("Deleted push notification config for task: {}", taskId);
    return Mono.empty();
  }

  @Override
  public Mono<Void> sendNotification(Task task) {
    if (task == null) {
      return Mono.error(new IllegalArgumentException("Task cannot be null"));
    }

    String taskId = task.getId();
    PushNotificationConfig config = configurations.get(taskId);

    if (config != null) {
      logger.info("Would send push notification for task {} to {}", taskId, config.getUrl());
      // Note: This simple implementation doesn't actually send HTTP requests
      // Use the Spring Boot module's InMemoryPushNotifier for full functionality
    }

    return Mono.empty();
  }

  @Override
  public Mono<Boolean> hasInfo(String taskId) {
    if (taskId == null || taskId.isEmpty()) {
      return Mono.error(new IllegalArgumentException("Task ID cannot be null or empty"));
    }

    return Mono.just(configurations.containsKey(taskId));
  }
}
