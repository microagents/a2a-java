package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

/**
 * Sent by server during sendStream or subscribe requests to notify of task artifact updates.
 *
 * <p>This event is emitted when artifacts are added, modified, or updated for a task, providing
 * real-time notifications about new task outputs and results.
 */
@Schema(description = "Sent by server during sendStream or subscribe requests")
public final class TaskArtifactUpdateEvent implements Event {

  /** Indicates if this artifact appends to a previous one. */
  @JsonProperty("append")
  @Schema(description = "Indicates if this artifact appends to a previous one")
  private Boolean append;

  /** Generated artifact. */
  @JsonProperty("artifact")
  @Valid
  @NotNull
  @Schema(description = "Generated artifact", required = true)
  private Artifact artifact;

  /** The context the task is associated with. */
  @JsonProperty("contextId")
  @NotBlank
  @Schema(description = "The context the task is associated with", required = true)
  private String contextId;

  /** Event type. */
  @JsonProperty("kind")
  @Schema(description = "Event type", example = "artifact-update")
  private String kind = "artifact-update";

  /** Indicates if this is the last chunk of the artifact. */
  @JsonProperty("lastChunk")
  @Schema(description = "Indicates if this is the last chunk of the artifact")
  private Boolean lastChunk;

  /** Extension metadata. */
  @JsonProperty("metadata")
  @Schema(description = "Extension metadata")
  private Map<String, Object> metadata;

  /** Task id. */
  @JsonProperty("taskId")
  @NotBlank
  @Schema(description = "Task id", required = true)
  private String taskId;

  /** Default constructor. */
  public TaskArtifactUpdateEvent() {}

  /** Gets the append flag. */
  public Boolean getAppend() {
    return append;
  }

  /** Sets the append flag. */
  public void setAppend(Boolean append) {
    this.append = append;
  }

  /** Gets the artifact. */
  public Artifact getArtifact() {
    return artifact;
  }

  /** Sets the artifact. */
  public void setArtifact(Artifact artifact) {
    this.artifact = artifact;
  }

  /** Gets the context ID. */
  public String getContextId() {
    return contextId;
  }

  /** Sets the context ID. */
  public void setContextId(String contextId) {
    this.contextId = contextId;
  }

  /** Gets the event kind. */
  public String getKind() {
    return kind;
  }

  /** Sets the event kind. */
  public void setKind(String kind) {
    this.kind = kind;
  }

  /** Gets the last chunk flag. */
  public Boolean getLastChunk() {
    return lastChunk;
  }

  /** Sets the last chunk flag. */
  public void setLastChunk(Boolean lastChunk) {
    this.lastChunk = lastChunk;
  }

  /** Gets the metadata. */
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  /** Sets the metadata. */
  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  /** Gets the task ID. */
  public String getTaskId() {
    return taskId;
  }

  /** Sets the task ID. */
  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  /** Builder for TaskArtifactUpdateEvent. */
  public static Builder builder(String taskId, Artifact artifact) {
    return new Builder(taskId, artifact);
  }

  public static class Builder {
    private final String taskId;
    private final Artifact artifact;
    private String contextId;
    private Boolean append;
    private Boolean lastChunk;
    private Map<String, Object> metadata;

    private Builder(String taskId, Artifact artifact) {
      this.taskId = taskId;
      this.artifact = artifact;
    }

    public Builder withContextId(String contextId) {
      this.contextId = contextId;
      return this;
    }

    public Builder withAppend(Boolean append) {
      this.append = append;
      return this;
    }

    public Builder withLastChunk(Boolean lastChunk) {
      this.lastChunk = lastChunk;
      return this;
    }

    public Builder withMetadata(Map<String, Object> metadata) {
      this.metadata = metadata;
      return this;
    }

    public TaskArtifactUpdateEvent build() {
      TaskArtifactUpdateEvent event = new TaskArtifactUpdateEvent();
      event.taskId = this.taskId;
      event.artifact = this.artifact;
      event.contextId = this.contextId;
      event.append = this.append;
      event.lastChunk = this.lastChunk;
      event.metadata = this.metadata;
      return event;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    TaskArtifactUpdateEvent that = (TaskArtifactUpdateEvent) obj;
    return Objects.equals(append, that.append)
        && Objects.equals(artifact, that.artifact)
        && Objects.equals(contextId, that.contextId)
        && Objects.equals(lastChunk, that.lastChunk)
        && Objects.equals(taskId, that.taskId)
        && Objects.equals(metadata, that.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(append, artifact, contextId, lastChunk, taskId, metadata);
  }

  @Override
  public String toString() {
    return "TaskArtifactUpdateEvent{"
        + "append="
        + append
        + ", artifact="
        + artifact
        + ", contextId='"
        + contextId
        + '\''
        + ", kind='"
        + kind
        + '\''
        + ", lastChunk="
        + lastChunk
        + ", taskId='"
        + taskId
        + '\''
        + ", metadata="
        + metadata
        + '}';
  }
}
