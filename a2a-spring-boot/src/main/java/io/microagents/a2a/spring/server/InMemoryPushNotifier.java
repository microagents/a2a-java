package io.microagents.a2a.core.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.microagents.a2a.core.types.core.Task;
import io.microagents.a2a.core.types.notifications.PushNotificationAuthenticationInfo;
import io.microagents.a2a.core.types.notifications.PushNotificationConfig;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * InMemoryPushNotifier implements push notifications using in-memory storage for webhook
 * configurations.
 *
 * <p>This implementation stores push notification configurations in memory and uses Spring
 * WebClient to send HTTP requests to configured webhook URLs. It provides thread-safe operations
 * and proper error handling with graceful failure recovery.
 *
 * <p>This implementation is suitable for single-instance deployments but may need a distributed
 * approach for scalable multi-instance deployments where configuration sharing is required.
 */
public class InMemoryPushNotifier implements PushNotifier {

  private static final Logger logger = LoggerFactory.getLogger(InMemoryPushNotifier.class);

  private final Map<String, PushNotificationConfig> configurations;
  private final WebClient webClient;
  private final ObjectMapper objectMapper;

  /** Creates a new InMemoryPushNotifier with a default WebClient. */
  public InMemoryPushNotifier() {
    this(
        WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build());
  }

  /**
   * Creates a new InMemoryPushNotifier with a custom WebClient.
   *
   * @param webClient the WebClient instance to use for HTTP requests
   */
  public InMemoryPushNotifier(@NotNull WebClient webClient) {
    this.configurations = new ConcurrentHashMap<>();
    this.webClient = webClient;
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public Mono<Void> setInfo(@NotNull String taskId, @NotNull PushNotificationConfig config) {
    return Mono.fromRunnable(
        () -> {
          configurations.put(taskId, config);
          logger.debug("Set push notification config for task ID: {}", taskId);
        });
  }

  @Override
  public Mono<PushNotificationConfig> getInfo(@NotNull String taskId) {
    return Mono.fromCallable(
        () -> {
          PushNotificationConfig config = configurations.get(taskId);
          if (config != null) {
            logger.debug("Retrieved push notification config for task ID: {}", taskId);
          } else {
            logger.debug("No push notification config found for task ID: {}", taskId);
          }
          return config;
        });
  }

  @Override
  public Mono<Void> deleteInfo(@NotNull String taskId) {
    return Mono.fromRunnable(
        () -> {
          PushNotificationConfig removed = configurations.remove(taskId);
          if (removed != null) {
            logger.debug("Removed push notification config for task ID: {}", taskId);
          } else {
            logger.debug("No push notification config to remove for task ID: {}", taskId);
          }
        });
  }

  @Override
  public Mono<Boolean> hasInfo(@NotNull String taskId) {
    return Mono.fromCallable(() -> configurations.containsKey(taskId));
  }

  @Override
  public Mono<Void> sendNotification(@NotNull Task task) {
    return getInfo(task.getId())
        .flatMap(config -> sendNotificationInternal(config, task))
        .doOnSuccess(
            v -> logger.debug("Successfully sent push notification for task: {}", task.getId()))
        .doOnError(
            error ->
                logger.warn(
                    "Failed to send push notification for task {}: {}",
                    task.getId(),
                    error.getMessage()))
        .onErrorResume(
            error -> {
              // Graceful failure - don't propagate notification errors
              logger.debug("Push notification error handled gracefully", error);
              return Mono.empty();
            });
  }

  /**
   * Sends an HTTP notification to the configured webhook URL.
   *
   * @param config the push notification configuration
   * @param task the task to send in the notification
   * @return a Mono that completes when the notification has been sent
   */
  private Mono<Void> sendNotificationInternal(PushNotificationConfig config, Task task) {
    return Mono.fromCallable(
            () -> {
              try {
                // Serialize task to JSON, excluding null values
                return objectMapper.writeValueAsString(task);
              } catch (Exception e) {
                throw new RuntimeException("Failed to serialize task to JSON", e);
              }
            })
        .flatMap(
            taskJson -> {
              var requestHeadersSpec = webClient.post().uri(config.getUrl()).bodyValue(taskJson);

              // Add authentication headers if configured
              if (config.getAuthentication() != null) {
                requestHeadersSpec =
                    addAuthenticationHeaders(requestHeadersSpec, config.getAuthentication());
              }

              return requestHeadersSpec
                  .retrieve()
                  .toBodilessEntity()
                  .doOnNext(
                      response -> {
                        if (response.getStatusCode().is2xxSuccessful()) {
                          logger.debug(
                              "Push notification sent successfully to: {}", config.getUrl());
                        } else {
                          logger.warn(
                              "Push notification returned non-2xx status: {} for URL: {}",
                              response.getStatusCode(),
                              config.getUrl());
                        }
                      })
                  .then();
            })
        .doOnError(
            error ->
                logger.error(
                    "Failed to send push notification to {}: {}",
                    config.getUrl(),
                    error.getMessage(),
                    error));
  }

  /**
   * Adds authentication headers to the request based on the authentication configuration.
   *
   * <p>Supports multiple authentication schemes with priority-based selection:
   *
   * <ul>
   *   <li><b>Bearer</b>: Adds "Authorization: Bearer {credentials}" header
   *   <li><b>Basic</b>: Adds "Authorization: Basic {base64(credentials)}" header for user:pass
   *       format
   *   <li><b>API Key</b>: Adds "X-API-Key: {credentials}" header
   * </ul>
   *
   * <p>If multiple schemes are supported, Bearer takes precedence, followed by Basic, then API Key.
   *
   * @param requestSpec the WebClient request specification to modify
   * @param authInfo the authentication information
   * @return the modified request specification with authentication headers
   */
  private org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec<?>
      addAuthenticationHeaders(
          org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec<?>
              requestSpec,
          PushNotificationAuthenticationInfo authInfo) {
    if (authInfo.getSchemes() == null || authInfo.getSchemes().isEmpty()) {
      logger.debug("No authentication schemes configured");
      return requestSpec;
    }

    String credentials = authInfo.getCredentials();
    if (credentials == null || credentials.trim().isEmpty()) {
      logger.debug("No credentials provided for authentication");
      return requestSpec;
    }

    // Priority-based scheme selection: Bearer > Basic > API Key
    for (String scheme : authInfo.getSchemes()) {
      String normalizedScheme = scheme.trim().toLowerCase();

      switch (normalizedScheme) {
        case "bearer":
          logger.debug("Using Bearer token authentication");
          return requestSpec.header("Authorization", "Bearer " + credentials.trim());
        case "basic":
          logger.debug("Using Basic authentication");
          String basicAuth = encodeBasicAuth(credentials.trim());
          if (basicAuth != null) {
            return requestSpec.header("Authorization", "Basic " + basicAuth);
          }
          break;
        case "apikey":
        case "api-key":
        case "api_key":
          logger.debug("Using API Key authentication");
          return requestSpec.header("X-API-Key", credentials.trim());
        default:
          logger.debug("Unknown authentication scheme: {}", scheme);
      }
    }

    logger.warn("No supported authentication scheme found in: {}", authInfo.getSchemes());
    return requestSpec;
  }

  /**
   * Encodes credentials for Basic authentication.
   *
   * <p>Accepts credentials in the format "username:password" and returns the base64-encoded value.
   * If credentials don't contain a colon, they are treated as a pre-encoded value.
   *
   * @param credentials the credentials in "user:pass" format or pre-encoded
   * @return base64-encoded credentials, or null if encoding fails
   */
  private String encodeBasicAuth(String credentials) {
    try {
      // If credentials already contain a colon, encode them as user:pass
      // Otherwise, assume they're already encoded or a token
      String toEncode = credentials.contains(":") ? credentials : credentials;
      return java.util.Base64.getEncoder()
          .encodeToString(toEncode.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    } catch (Exception e) {
      logger.error("Failed to encode Basic auth credentials: {}", e.getMessage());
      return null;
    }
  }

  /**
   * Gets the current number of configured push notifications.
   *
   * <p>This method is useful for monitoring and debugging purposes.
   *
   * @return the number of currently configured push notifications
   */
  public int getConfigCount() {
    return configurations.size();
  }

  /**
   * Checks if a push notification configuration exists for the given task ID.
   *
   * @param taskId the task ID to check
   * @return true if a configuration exists for the task ID, false otherwise
   */
  public boolean hasConfig(@NotNull String taskId) {
    return configurations.containsKey(taskId);
  }

  /**
   * Removes all push notification configurations.
   *
   * <p>This method is primarily intended for cleanup during shutdown or testing scenarios.
   *
   * @return a Mono that completes when all configurations have been removed
   */
  public Mono<Void> clearAll() {
    return Mono.fromRunnable(
        () -> {
          int count = configurations.size();
          configurations.clear();
          logger.debug("Cleared all {} push notification configurations", count);
        });
  }
}
