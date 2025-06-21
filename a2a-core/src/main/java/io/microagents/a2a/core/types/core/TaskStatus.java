package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.notifications.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

/**
 * TaskState and accompanying message.
 *
 * <p>Represents the current status of a task including its state, optional message, and timestamp.
 */
public class TaskStatus {

  @NotNull
  @JsonProperty("state")
  private final TaskState state;

  @Valid
  @JsonProperty("message")
  private final Message message;

  @JsonProperty("timestamp")
  private final String timestamp;

  /**
   * Creates a new TaskStatus with the specified state.
   *
   * @param state the task state
   */
  public TaskStatus(@NotNull TaskState state) {
    this(state, null, Instant.now().toString());
  }

  /**
   * Creates a new TaskStatus with the specified state and message.
   *
   * @param state the task state
   * @param message additional status updates for client (may be null)
   */
  public TaskStatus(@NotNull TaskState state, Message message) {
    this(state, message, Instant.now().toString());
  }

  /**
   * Creates a new TaskStatus with all fields.
   *
   * @param state the task state
   * @param message additional status updates for client (may be null)
   * @param timestamp ISO 8601 datetime string when the status was recorded (may be null)
   */
  public TaskStatus(@NotNull TaskState state, Message message, String timestamp) {
    this.state = Objects.requireNonNull(state, "state cannot be null");
    this.message = message;
    this.timestamp = timestamp;
  }

  /**
   * Gets the task state.
   *
   * @return the task state
   */
  @NotNull
  public TaskState getState() {
    return state;
  }

  /**
   * Gets the additional status message.
   *
   * @return the message, or null if none
   */
  public Message getMessage() {
    return message;
  }

  /**
   * Gets the timestamp when the status was recorded.
   *
   * @return ISO 8601 datetime string, or null if not specified
   */
  public String getTimestamp() {
    return timestamp;
  }

  /**
   * Creates a new TaskStatus with the specified state.
   *
   * @param state the task state
   * @return a new TaskStatus instance
   */
  public static TaskStatus of(@NotNull TaskState state) {
    return new TaskStatus(state);
  }

  /**
   * Creates a new TaskStatus with the specified state and message.
   *
   * @param state the task state
   * @param message additional status message
   * @return a new TaskStatus instance
   */
  public static TaskStatus of(@NotNull TaskState state, Message message) {
    return new TaskStatus(state, message);
  }

  /**
   * Creates a new TaskStatus with all fields.
   *
   * @param state the task state
   * @param message additional status message
   * @param timestamp ISO 8601 datetime string
   * @return a new TaskStatus instance
   */
  public static TaskStatus of(@NotNull TaskState state, Message message, String timestamp) {
    return new TaskStatus(state, message, timestamp);
  }

  /**
   * Creates a builder for constructing TaskStatus instances.
   *
   * @param state the required task state
   * @return a new builder instance
   */
  public static Builder builder(@NotNull TaskState state) {
    return new Builder(state);
  }

  /** Builder for constructing TaskStatus instances. */
  public static class Builder {
    private final TaskState state;
    private Message message;
    private String timestamp;

    private Builder(@NotNull TaskState state) {
      this.state = Objects.requireNonNull(state, "state cannot be null");
      this.timestamp = Instant.now().toString(); // Default to current time
    }

    /**
     * Sets the additional status message.
     *
     * @param message the message
     * @return this builder
     */
    public Builder withMessage(Message message) {
      this.message = message;
      return this;
    }

    /**
     * Sets the timestamp.
     *
     * @param timestamp ISO 8601 datetime string
     * @return this builder
     */
    public Builder withTimestamp(String timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    /**
     * Sets the timestamp to the current instant.
     *
     * @return this builder
     */
    public Builder withCurrentTimestamp() {
      this.timestamp = Instant.now().toString();
      return this;
    }

    /**
     * Builds the TaskStatus instance.
     *
     * @return a new TaskStatus
     */
    public TaskStatus build() {
      return new TaskStatus(state, message, timestamp);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TaskStatus that = (TaskStatus) o;
    return state == that.state
        && Objects.equals(message, that.message)
        && Objects.equals(timestamp, that.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(state, message, timestamp);
  }

  @Override
  public String toString() {
    return "TaskStatus{"
        + "state="
        + state
        + ", message="
        + message
        + ", timestamp='"
        + timestamp
        + '\''
        + '}';
  }
}
