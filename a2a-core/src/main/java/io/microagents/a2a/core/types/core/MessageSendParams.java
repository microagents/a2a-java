package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

/**
 * Parameters for the message/send and message/stream A2A methods.
 *
 * <p>Sent by the client to the agent as a request. May create, continue or restart a task. Contains
 * the message being sent and optional configuration parameters.
 */
@Schema(description = "Parameters for the message/send and message/stream A2A methods")
public class MessageSendParams {

  /** The message being sent to the server. */
  @JsonProperty("message")
  @Valid
  @NotNull
  @Schema(description = "The message being sent to the server", required = true)
  private Message message;

  /** Send message configuration. */
  @JsonProperty("configuration")
  @Valid
  @Schema(description = "Send message configuration")
  private MessageSendConfiguration configuration;

  /** Extension metadata. */
  @JsonProperty("metadata")
  @Schema(description = "Extension metadata")
  private Map<String, Object> metadata;

  /** Default constructor. */
  public MessageSendParams() {}

  /**
   * Constructor with message.
   *
   * @param message the message to send
   */
  public MessageSendParams(Message message) {
    this.message = message;
  }

  /**
   * Full constructor.
   *
   * @param message the message to send
   * @param configuration the send configuration
   * @param metadata extension metadata
   */
  public MessageSendParams(
      Message message, MessageSendConfiguration configuration, Map<String, Object> metadata) {
    this.message = message;
    this.configuration = configuration;
    this.metadata = metadata;
  }

  /**
   * Gets the message.
   *
   * @return the message
   */
  public Message getMessage() {
    return message;
  }

  /**
   * Sets the message.
   *
   * @param message the message
   */
  public void setMessage(Message message) {
    this.message = message;
  }

  /**
   * Gets the configuration.
   *
   * @return the configuration, may be null
   */
  public MessageSendConfiguration getConfiguration() {
    return configuration;
  }

  /**
   * Sets the configuration.
   *
   * @param configuration the configuration
   */
  public void setConfiguration(MessageSendConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * Gets the metadata.
   *
   * @return the metadata, may be null
   */
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  /**
   * Sets the metadata.
   *
   * @param metadata the metadata
   */
  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  /**
   * Creates a new MessageSendParams with the specified message.
   *
   * @param message the message to send
   * @return a new MessageSendParams
   */
  public static MessageSendParams of(Message message) {
    return new MessageSendParams(message);
  }

  /**
   * Creates a copy of this params with a new message.
   *
   * @param newMessage the new message
   * @return a new MessageSendParams with the specified message
   */
  public MessageSendParams withMessage(Message newMessage) {
    return new MessageSendParams(newMessage, this.configuration, this.metadata);
  }

  /**
   * Creates a copy of this params with a new configuration.
   *
   * @param newConfiguration the new configuration
   * @return a new MessageSendParams with the specified configuration
   */
  public MessageSendParams withConfiguration(MessageSendConfiguration newConfiguration) {
    return new MessageSendParams(this.message, newConfiguration, this.metadata);
  }

  /**
   * Creates a copy of this params with new metadata.
   *
   * @param newMetadata the new metadata
   * @return a new MessageSendParams with the specified metadata
   */
  public MessageSendParams withMetadata(Map<String, Object> newMetadata) {
    return new MessageSendParams(this.message, this.configuration, newMetadata);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    MessageSendParams that = (MessageSendParams) obj;
    return Objects.equals(message, that.message)
        && Objects.equals(configuration, that.configuration)
        && Objects.equals(metadata, that.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, configuration, metadata);
  }

  @Override
  public String toString() {
    return "MessageSendParams{"
        + "message="
        + message
        + ", configuration="
        + configuration
        + ", metadata="
        + metadata
        + '}';
  }
}
