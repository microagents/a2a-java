package io.microagents.a2a.core.types.notifications;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Configuration for setting up push notifications for task updates.
 *
 * <p>This configuration allows clients to specify where and how they want to receive asynchronous
 * updates about task progress when not actively connected to the agent.
 */
@Schema(description = "Configuration for setting up push notifications for task updates")
public class PushNotificationConfig {

  /** URL for sending the push notifications. */
  @JsonProperty("url")
  @NotNull
  @NotBlank
  @Schema(
      description = "URL for sending the push notifications",
      example = "https://client.example.com/webhook/task-updates",
      required = true)
  private String url;

  /** Push Notification ID - created by server to support multiple callbacks */
  @JsonProperty("id")
  @Schema(
      description = "Push Notification ID - created by server to support multiple callbacks",
      example = "push-config-123")
  private String id;

  /** Token unique to this task/session. */
  @JsonProperty("token")
  @Schema(description = "Token unique to this task/session", example = "session-token-456")
  private String token;

  /** Authentication details for push notifications. */
  @JsonProperty("authentication")
  @Valid
  @Schema(description = "Authentication details for push notifications")
  private PushNotificationAuthenticationInfo authentication;

  /** Default constructor. */
  public PushNotificationConfig() {}

  /**
   * Constructor with URL.
   *
   * @param url the notification URL
   */
  public PushNotificationConfig(String url) {
    this.url = url;
  }

  /**
   * Full constructor.
   *
   * @param url the notification URL
   * @param id the push notification ID
   * @param token the session token
   * @param authentication the authentication details
   */
  public PushNotificationConfig(
      String url, String id, String token, PushNotificationAuthenticationInfo authentication) {
    this.url = url;
    this.id = id;
    this.token = token;
    this.authentication = authentication;
  }

  /**
   * Gets the notification URL.
   *
   * @return the notification URL
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets the notification URL.
   *
   * @param url the notification URL
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Gets the push notification ID.
   *
   * @return the push notification ID, may be null
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the push notification ID.
   *
   * @param id the push notification ID
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the session token.
   *
   * @return the session token, may be null
   */
  public String getToken() {
    return token;
  }

  /**
   * Sets the session token.
   *
   * @param token the session token
   */
  public void setToken(String token) {
    this.token = token;
  }

  /**
   * Gets the authentication details.
   *
   * @return the authentication details, may be null
   */
  public PushNotificationAuthenticationInfo getAuthentication() {
    return authentication;
  }

  /**
   * Sets the authentication details.
   *
   * @param authentication the authentication details
   */
  public void setAuthentication(PushNotificationAuthenticationInfo authentication) {
    this.authentication = authentication;
  }

  /**
   * Creates a new push notification config with the specified URL.
   *
   * @param url the notification URL
   * @return a new PushNotificationConfig
   */
  public static PushNotificationConfig of(String url) {
    return new PushNotificationConfig(url);
  }

  /**
   * Creates a copy of this config with a new URL.
   *
   * @param newUrl the new URL
   * @return a new config with the specified URL
   */
  public PushNotificationConfig withUrl(String newUrl) {
    return new PushNotificationConfig(newUrl, this.id, this.token, this.authentication);
  }

  /**
   * Creates a copy of this config with a new ID.
   *
   * @param newId the new ID
   * @return a new config with the specified ID
   */
  public PushNotificationConfig withId(String newId) {
    return new PushNotificationConfig(this.url, newId, this.token, this.authentication);
  }

  /**
   * Creates a copy of this config with a new token.
   *
   * @param newToken the new token
   * @return a new config with the specified token
   */
  public PushNotificationConfig withToken(String newToken) {
    return new PushNotificationConfig(this.url, this.id, newToken, this.authentication);
  }

  /**
   * Creates a copy of this config with new authentication.
   *
   * @param newAuthentication the new authentication details
   * @return a new config with the specified authentication
   */
  public PushNotificationConfig withAuthentication(
      PushNotificationAuthenticationInfo newAuthentication) {
    return new PushNotificationConfig(this.url, this.id, this.token, newAuthentication);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    PushNotificationConfig that = (PushNotificationConfig) obj;
    return Objects.equals(url, that.url)
        && Objects.equals(id, that.id)
        && Objects.equals(token, that.token)
        && Objects.equals(authentication, that.authentication);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url, id, token, authentication);
  }

  @Override
  public String toString() {
    return "PushNotificationConfig{"
        + "url='"
        + url
        + '\''
        + ", id='"
        + id
        + '\''
        + ", token='"
        + token
        + '\''
        + ", authentication="
        + authentication
        + '}';
  }
}
