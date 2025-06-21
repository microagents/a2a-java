package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.notifications.*;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Defines optional capabilities supported by an agent.
 *
 * <p>This class describes what additional features and extensions an agent supports beyond the
 * basic A2A protocol.
 */
public class AgentCapabilities {

  @JsonProperty("streaming")
  private final Boolean streaming;

  @JsonProperty("pushNotifications")
  private final Boolean pushNotifications;

  @JsonProperty("stateTransitionHistory")
  private final Boolean stateTransitionHistory;

  @Valid
  @JsonProperty("extensions")
  private final List<AgentExtension> extensions;

  /** Creates AgentCapabilities with no specific capabilities set. */
  public AgentCapabilities() {
    this(null, null, null, null);
  }

  /**
   * Creates AgentCapabilities with all fields.
   *
   * @param streaming true if the agent supports Server-Sent Events
   * @param pushNotifications true if the agent can notify updates to client
   * @param stateTransitionHistory true if the agent exposes status change history for tasks
   * @param extensions extensions supported by this agent
   */
  public AgentCapabilities(
      Boolean streaming,
      Boolean pushNotifications,
      Boolean stateTransitionHistory,
      List<AgentExtension> extensions) {
    this.streaming = streaming;
    this.pushNotifications = pushNotifications;
    this.stateTransitionHistory = stateTransitionHistory;
    this.extensions =
        extensions == null ? null : Collections.unmodifiableList(new ArrayList<>(extensions));
  }

  /**
   * Gets whether the agent supports Server-Sent Events (SSE).
   *
   * @return true if supported, false if not, null if not specified
   */
  public Boolean getStreaming() {
    return streaming;
  }

  /**
   * Checks if the agent supports streaming.
   *
   * @return true if streaming is supported, false otherwise
   */
  public boolean isStreamingSupported() {
    return Boolean.TRUE.equals(streaming);
  }

  /**
   * Gets whether the agent can notify updates to client.
   *
   * @return true if supported, false if not, null if not specified
   */
  public Boolean getPushNotifications() {
    return pushNotifications;
  }

  /**
   * Checks if the agent supports push notifications.
   *
   * @return true if push notifications are supported, false otherwise
   */
  public boolean isPushNotificationsSupported() {
    return Boolean.TRUE.equals(pushNotifications);
  }

  /**
   * Gets whether the agent exposes status change history for tasks.
   *
   * @return true if supported, false if not, null if not specified
   */
  public Boolean getStateTransitionHistory() {
    return stateTransitionHistory;
  }

  /**
   * Checks if the agent supports state transition history.
   *
   * @return true if state transition history is supported, false otherwise
   */
  public boolean isStateTransitionHistorySupported() {
    return Boolean.TRUE.equals(stateTransitionHistory);
  }

  /**
   * Gets the extensions supported by this agent.
   *
   * @return unmodifiable list of extensions, or null if none
   */
  public List<AgentExtension> getExtensions() {
    return extensions;
  }

  /**
   * Creates AgentCapabilities with no capabilities.
   *
   * @return a new AgentCapabilities instance
   */
  public static AgentCapabilities none() {
    return new AgentCapabilities();
  }

  /**
   * Creates AgentCapabilities with basic capabilities.
   *
   * @param streaming streaming support
   * @param pushNotifications push notification support
   * @return a new AgentCapabilities instance
   */
  public static AgentCapabilities of(boolean streaming, boolean pushNotifications) {
    return new AgentCapabilities(streaming, pushNotifications, null, null);
  }

  /**
   * Creates a builder for constructing AgentCapabilities instances.
   *
   * @return a new builder instance
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for constructing AgentCapabilities instances. */
  public static class Builder {
    private Boolean streaming;
    private Boolean pushNotifications;
    private Boolean stateTransitionHistory;
    private final List<AgentExtension> extensions = new ArrayList<>();

    /**
     * Sets streaming support.
     *
     * @param streaming true if streaming is supported
     * @return this builder
     */
    public Builder withStreaming(boolean streaming) {
      this.streaming = streaming;
      return this;
    }

    /**
     * Enables streaming support.
     *
     * @return this builder
     */
    public Builder withStreaming() {
      this.streaming = true;
      return this;
    }

    /**
     * Sets push notification support.
     *
     * @param pushNotifications true if push notifications are supported
     * @return this builder
     */
    public Builder withPushNotifications(boolean pushNotifications) {
      this.pushNotifications = pushNotifications;
      return this;
    }

    /**
     * Enables push notification support.
     *
     * @return this builder
     */
    public Builder withPushNotifications() {
      this.pushNotifications = true;
      return this;
    }

    /**
     * Sets state transition history support.
     *
     * @param stateTransitionHistory true if state transition history is supported
     * @return this builder
     */
    public Builder withStateTransitionHistory(boolean stateTransitionHistory) {
      this.stateTransitionHistory = stateTransitionHistory;
      return this;
    }

    /**
     * Enables state transition history support.
     *
     * @return this builder
     */
    public Builder withStateTransitionHistory() {
      this.stateTransitionHistory = true;
      return this;
    }

    /**
     * Adds an extension.
     *
     * @param extension the extension to add
     * @return this builder
     */
    public Builder addExtension(AgentExtension extension) {
      this.extensions.add(Objects.requireNonNull(extension, "extension cannot be null"));
      return this;
    }

    /**
     * Adds multiple extensions.
     *
     * @param extensions the extensions to add
     * @return this builder
     */
    public Builder addExtensions(Collection<AgentExtension> extensions) {
      Objects.requireNonNull(extensions, "extensions cannot be null");
      extensions.forEach(extension -> addExtension(extension));
      return this;
    }

    /**
     * Adds an extension by URI.
     *
     * @param extensionUri the extension URI
     * @return this builder
     */
    public Builder addExtension(String extensionUri) {
      return addExtension(AgentExtension.of(extensionUri));
    }

    /**
     * Adds an extension by URI and description.
     *
     * @param extensionUri the extension URI
     * @param description the extension description
     * @return this builder
     */
    public Builder addExtension(String extensionUri, String description) {
      return addExtension(AgentExtension.of(extensionUri, description));
    }

    /**
     * Builds the AgentCapabilities instance.
     *
     * @return a new AgentCapabilities
     */
    public AgentCapabilities build() {
      return new AgentCapabilities(
          streaming,
          pushNotifications,
          stateTransitionHistory,
          extensions.isEmpty() ? null : extensions);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AgentCapabilities that = (AgentCapabilities) o;
    return Objects.equals(streaming, that.streaming)
        && Objects.equals(pushNotifications, that.pushNotifications)
        && Objects.equals(stateTransitionHistory, that.stateTransitionHistory)
        && Objects.equals(extensions, that.extensions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(streaming, pushNotifications, stateTransitionHistory, extensions);
  }

  @Override
  public String toString() {
    return "AgentCapabilities{"
        + "streaming="
        + streaming
        + ", pushNotifications="
        + pushNotifications
        + ", stateTransitionHistory="
        + stateTransitionHistory
        + ", extensions="
        + extensions
        + '}';
  }
}
