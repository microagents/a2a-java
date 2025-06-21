package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Objects;

/**
 * Configuration for the send message request.
 *
 * <p>This class contains configuration options that control how the agent processes and responds to
 * a message request.
 */
@Schema(description = "Configuration for the send message request")
public class MessageSendConfiguration {

  /** Accepted output modalities by the client. */
  @JsonProperty("acceptedOutputModes")
  @NotNull
  @Schema(
      description = "Accepted output modalities by the client",
      example = "[\"text/plain\", \"application/json\"]",
      required = true)
  private List<String> acceptedOutputModes;

  /** If the server should treat the client as a blocking request. */
  @JsonProperty("blocking")
  @Schema(description = "If the server should treat the client as a blocking request")
  private Boolean blocking;

  /** Number of recent messages to be retrieved. */
  @JsonProperty("historyLength")
  @Positive
  @Schema(description = "Number of recent messages to be retrieved", example = "10")
  private Integer historyLength;

  /** Where the server should send notifications when disconnected. */
  @JsonProperty("pushNotificationConfig")
  @Valid
  @Schema(description = "Where the server should send notifications when disconnected")
  private PushNotificationConfig pushNotificationConfig;

  /** Default constructor. */
  public MessageSendConfiguration() {}

  /**
   * Constructor with accepted output modes.
   *
   * @param acceptedOutputModes the accepted output modes
   */
  public MessageSendConfiguration(List<String> acceptedOutputModes) {
    this.acceptedOutputModes = acceptedOutputModes;
  }

  /**
   * Full constructor.
   *
   * @param acceptedOutputModes the accepted output modes
   * @param blocking whether this is a blocking request
   * @param historyLength number of recent messages to retrieve
   * @param pushNotificationConfig push notification configuration
   */
  public MessageSendConfiguration(
      List<String> acceptedOutputModes,
      Boolean blocking,
      Integer historyLength,
      PushNotificationConfig pushNotificationConfig) {
    this.acceptedOutputModes = acceptedOutputModes;
    this.blocking = blocking;
    this.historyLength = historyLength;
    this.pushNotificationConfig = pushNotificationConfig;
  }

  /**
   * Gets the accepted output modes.
   *
   * @return the accepted output modes
   */
  public List<String> getAcceptedOutputModes() {
    return acceptedOutputModes;
  }

  /**
   * Sets the accepted output modes.
   *
   * @param acceptedOutputModes the accepted output modes
   */
  public void setAcceptedOutputModes(List<String> acceptedOutputModes) {
    this.acceptedOutputModes = acceptedOutputModes;
  }

  /**
   * Gets whether this is a blocking request.
   *
   * @return true if blocking, false otherwise, may be null
   */
  public Boolean getBlocking() {
    return blocking;
  }

  /**
   * Sets whether this is a blocking request.
   *
   * @param blocking true if blocking, false otherwise
   */
  public void setBlocking(Boolean blocking) {
    this.blocking = blocking;
  }

  /**
   * Gets the history length.
   *
   * @return the history length, may be null
   */
  public Integer getHistoryLength() {
    return historyLength;
  }

  /**
   * Sets the history length.
   *
   * @param historyLength the history length
   */
  public void setHistoryLength(Integer historyLength) {
    this.historyLength = historyLength;
  }

  /**
   * Gets the push notification config.
   *
   * @return the push notification config, may be null
   */
  public PushNotificationConfig getPushNotificationConfig() {
    return pushNotificationConfig;
  }

  /**
   * Sets the push notification config.
   *
   * @param pushNotificationConfig the push notification config
   */
  public void setPushNotificationConfig(PushNotificationConfig pushNotificationConfig) {
    this.pushNotificationConfig = pushNotificationConfig;
  }

  /**
   * Creates a new configuration with the specified accepted output modes.
   *
   * @param acceptedOutputModes the accepted output modes
   * @return a new MessageSendConfiguration
   */
  public static MessageSendConfiguration of(List<String> acceptedOutputModes) {
    return new MessageSendConfiguration(acceptedOutputModes);
  }

  /**
   * Creates a copy of this configuration with new accepted output modes.
   *
   * @param newAcceptedOutputModes the new accepted output modes
   * @return a new configuration with the specified accepted output modes
   */
  public MessageSendConfiguration withAcceptedOutputModes(List<String> newAcceptedOutputModes) {
    return new MessageSendConfiguration(
        newAcceptedOutputModes, this.blocking, this.historyLength, this.pushNotificationConfig);
  }

  /**
   * Creates a copy of this configuration with a new blocking setting.
   *
   * @param newBlocking the new blocking setting
   * @return a new configuration with the specified blocking setting
   */
  public MessageSendConfiguration withBlocking(Boolean newBlocking) {
    return new MessageSendConfiguration(
        this.acceptedOutputModes, newBlocking, this.historyLength, this.pushNotificationConfig);
  }

  /**
   * Creates a copy of this configuration with a new history length.
   *
   * @param newHistoryLength the new history length
   * @return a new configuration with the specified history length
   */
  public MessageSendConfiguration withHistoryLength(Integer newHistoryLength) {
    return new MessageSendConfiguration(
        this.acceptedOutputModes, this.blocking, newHistoryLength, this.pushNotificationConfig);
  }

  /**
   * Creates a copy of this configuration with a new push notification config.
   *
   * @param newPushNotificationConfig the new push notification config
   * @return a new configuration with the specified push notification config
   */
  public MessageSendConfiguration withPushNotificationConfig(
      PushNotificationConfig newPushNotificationConfig) {
    return new MessageSendConfiguration(
        this.acceptedOutputModes, this.blocking, this.historyLength, newPushNotificationConfig);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    MessageSendConfiguration that = (MessageSendConfiguration) obj;
    return Objects.equals(acceptedOutputModes, that.acceptedOutputModes)
        && Objects.equals(blocking, that.blocking)
        && Objects.equals(historyLength, that.historyLength)
        && Objects.equals(pushNotificationConfig, that.pushNotificationConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hash(acceptedOutputModes, blocking, historyLength, pushNotificationConfig);
  }

  @Override
  public String toString() {
    return "MessageSendConfiguration{"
        + "acceptedOutputModes="
        + acceptedOutputModes
        + ", blocking="
        + blocking
        + ", historyLength="
        + historyLength
        + ", pushNotificationConfig="
        + pushNotificationConfig
        + '}';
  }
}
