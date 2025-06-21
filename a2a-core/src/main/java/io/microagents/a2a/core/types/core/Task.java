package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.notifications.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.*;

/**
 * Represents a task in the A2A protocol.
 *
 * <p>A task is the primary unit of work exchanged between agents. It contains the current status,
 * message history, artifacts generated, and metadata for contextual alignment.
 */
public final class Task implements Event {

  @NotBlank
  @JsonProperty("id")
  private final String id;

  @NotBlank
  @JsonProperty("contextId")
  private final String contextId;

  @NotNull
  @Valid
  @JsonProperty("status")
  private final TaskStatus status;

  @JsonProperty("kind")
  private final String kind = "task";

  @Valid
  @JsonProperty("history")
  private final List<Message> history;

  @Valid
  @JsonProperty("artifacts")
  private final List<Artifact> artifacts;

  @JsonProperty("metadata")
  private final Map<String, Object> metadata;

  /**
   * Creates a new Task with required fields.
   *
   * @param id unique identifier for the task
   * @param contextId server-generated id for contextual alignment
   * @param status current status of the task
   */
  public Task(@NotBlank String id, @NotBlank String contextId, @NotNull TaskStatus status) {
    this(id, contextId, status, null, null, null);
  }

  /**
   * Creates a new Task with all fields.
   *
   * @param id unique identifier for the task
   * @param contextId server-generated id for contextual alignment
   * @param status current status of the task
   * @param history message history (may be null)
   * @param artifacts collection of artifacts created by the agent (may be null)
   * @param metadata extension metadata (may be null)
   */
  public Task(
      @NotBlank String id,
      @NotBlank String contextId,
      @NotNull TaskStatus status,
      List<Message> history,
      List<Artifact> artifacts,
      Map<String, Object> metadata) {
    this.id = Objects.requireNonNull(id, "id cannot be null").trim();
    if (this.id.isEmpty()) {
      throw new IllegalArgumentException("id cannot be blank");
    }
    this.contextId = Objects.requireNonNull(contextId, "contextId cannot be null").trim();
    if (this.contextId.isEmpty()) {
      throw new IllegalArgumentException("contextId cannot be blank");
    }
    this.status = Objects.requireNonNull(status, "status cannot be null");
    this.history = history == null ? null : Collections.unmodifiableList(new ArrayList<>(history));
    this.artifacts =
        artifacts == null ? null : Collections.unmodifiableList(new ArrayList<>(artifacts));
    this.metadata = metadata == null ? null : Collections.unmodifiableMap(new HashMap<>(metadata));
  }

  /**
   * Gets the unique identifier for the task.
   *
   * @return the task ID
   */
  @NotBlank
  public String getId() {
    return id;
  }

  /**
   * Gets the server-generated context ID for contextual alignment.
   *
   * @return the context ID
   */
  @NotBlank
  public String getContextId() {
    return contextId;
  }

  /**
   * Gets the current status of the task.
   *
   * @return the task status
   */
  @NotNull
  public TaskStatus getStatus() {
    return status;
  }

  /**
   * Gets the event type (always "task").
   *
   * @return the kind
   */
  public String getKind() {
    return kind;
  }

  /**
   * Gets the message history.
   *
   * @return unmodifiable list of messages, or null if no history
   */
  public List<Message> getHistory() {
    return history;
  }

  /**
   * Gets the collection of artifacts created by the agent.
   *
   * @return unmodifiable list of artifacts, or null if none
   */
  public List<Artifact> getArtifacts() {
    return artifacts;
  }

  /**
   * Gets the extension metadata.
   *
   * @return unmodifiable map of metadata, or null if none
   */
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  /**
   * Creates a new Task with required fields.
   *
   * @param id unique identifier for the task
   * @param contextId server-generated context ID
   * @param status current task status
   * @return a new Task instance
   */
  public static Task of(
      @NotBlank String id, @NotBlank String contextId, @NotNull TaskStatus status) {
    return new Task(id, contextId, status);
  }

  /**
   * Creates a new Task with a simple state-only status.
   *
   * @param id unique identifier for the task
   * @param contextId server-generated context ID
   * @param state task state
   * @return a new Task instance
   */
  public static Task of(@NotBlank String id, @NotBlank String contextId, @NotNull TaskState state) {
    return new Task(id, contextId, TaskStatus.of(state));
  }

  /**
   * Creates a builder for constructing Task instances.
   *
   * @param id the required task ID
   * @param contextId the required context ID
   * @param status the required task status
   * @return a new builder instance
   */
  public static Builder builder(
      @NotBlank String id, @NotBlank String contextId, @NotNull TaskStatus status) {
    return new Builder(id, contextId, status);
  }

  /**
   * Creates a builder for constructing Task instances with a simple state.
   *
   * @param id the required task ID
   * @param contextId the required context ID
   * @param state the required task state
   * @return a new builder instance
   */
  public static Builder builder(
      @NotBlank String id, @NotBlank String contextId, @NotNull TaskState state) {
    return new Builder(id, contextId, TaskStatus.of(state));
  }

  /** Builder for constructing Task instances. */
  public static class Builder {
    private final String id;
    private final String contextId;
    private final TaskStatus status;
    private final List<Message> history = new ArrayList<>();
    private final List<Artifact> artifacts = new ArrayList<>();
    private final Map<String, Object> metadata = new HashMap<>();

    private Builder(@NotBlank String id, @NotBlank String contextId, @NotNull TaskStatus status) {
      this.id = Objects.requireNonNull(id, "id cannot be null").trim();
      if (this.id.isEmpty()) {
        throw new IllegalArgumentException("id cannot be blank");
      }
      this.contextId = Objects.requireNonNull(contextId, "contextId cannot be null").trim();
      if (this.contextId.isEmpty()) {
        throw new IllegalArgumentException("contextId cannot be blank");
      }
      this.status = Objects.requireNonNull(status, "status cannot be null");
    }

    /**
     * Adds a message to the history.
     *
     * @param message the message to add
     * @return this builder
     */
    public Builder addMessage(@NotNull Message message) {
      this.history.add(Objects.requireNonNull(message, "message cannot be null"));
      return this;
    }

    /**
     * Adds multiple messages to the history.
     *
     * @param messages the messages to add
     * @return this builder
     */
    public Builder addMessages(@NotNull Collection<Message> messages) {
      Objects.requireNonNull(messages, "messages cannot be null");
      messages.forEach(message -> addMessage(message));
      return this;
    }

    /**
     * Sets the complete message history.
     *
     * @param history the message history
     * @return this builder
     */
    public Builder withHistory(@NotNull List<Message> history) {
      this.history.clear();
      return addMessages(history);
    }

    /**
     * Adds an artifact.
     *
     * @param artifact the artifact to add
     * @return this builder
     */
    public Builder addArtifact(@NotNull Artifact artifact) {
      this.artifacts.add(Objects.requireNonNull(artifact, "artifact cannot be null"));
      return this;
    }

    /**
     * Adds multiple artifacts.
     *
     * @param artifacts the artifacts to add
     * @return this builder
     */
    public Builder addArtifacts(@NotNull Collection<Artifact> artifacts) {
      Objects.requireNonNull(artifacts, "artifacts cannot be null");
      artifacts.forEach(artifact -> addArtifact(artifact));
      return this;
    }

    /**
     * Sets the complete artifacts collection.
     *
     * @param artifacts the artifacts
     * @return this builder
     */
    public Builder withArtifacts(@NotNull List<Artifact> artifacts) {
      this.artifacts.clear();
      return addArtifacts(artifacts);
    }

    /**
     * Adds a metadata entry.
     *
     * @param key the metadata key
     * @param value the metadata value
     * @return this builder
     */
    public Builder addMetadata(@NotBlank String key, Object value) {
      Objects.requireNonNull(key, "key cannot be null");
      if (key.trim().isEmpty()) {
        throw new IllegalArgumentException("key cannot be blank");
      }
      this.metadata.put(key.trim(), value);
      return this;
    }

    /**
     * Adds multiple metadata entries.
     *
     * @param metadata the metadata entries
     * @return this builder
     */
    public Builder addMetadata(@NotNull Map<String, Object> metadata) {
      Objects.requireNonNull(metadata, "metadata cannot be null");
      metadata.forEach((key, value) -> addMetadata(key, value));
      return this;
    }

    /**
     * Sets the complete metadata.
     *
     * @param metadata the metadata
     * @return this builder
     */
    public Builder withMetadata(@NotNull Map<String, Object> metadata) {
      this.metadata.clear();
      return addMetadata(metadata);
    }

    /**
     * Builds the Task instance.
     *
     * @return a new Task
     */
    public Task build() {
      return new Task(
          id,
          contextId,
          status,
          history.isEmpty() ? null : history,
          artifacts.isEmpty() ? null : artifacts,
          metadata.isEmpty() ? null : metadata);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Task task = (Task) o;
    return Objects.equals(id, task.id)
        && Objects.equals(contextId, task.contextId)
        && Objects.equals(status, task.status)
        && Objects.equals(kind, task.kind)
        && Objects.equals(history, task.history)
        && Objects.equals(artifacts, task.artifacts)
        && Objects.equals(metadata, task.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, contextId, status, kind, history, artifacts, metadata);
  }

  @Override
  public String toString() {
    return "Task{"
        + "id='"
        + id
        + '\''
        + ", contextId='"
        + contextId
        + '\''
        + ", status="
        + status
        + ", kind='"
        + kind
        + '\''
        + ", history="
        + history
        + ", artifacts="
        + artifacts
        + ", metadata="
        + metadata
        + '}';
  }
}
