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
 * Sent by server during sendStream or subscribe requests to notify of task status changes.
 *
 * <p>This event is emitted when a task's status changes during execution, providing real-time
 * updates about the task's progress and current state.
 */
@Schema(description = "Sent by server during sendStream or subscribe requests")
public final class TaskStatusUpdateEvent implements Event {

  /** The context the task is associated with. */
  @JsonProperty("contextId")
  @NotBlank
  @Schema(description = "The context the task is associated with", required = true)
  private String contextId;

  /** Indicates the end of the event stream. */
  @JsonProperty("final")
  @NotNull
  @Schema(description = "Indicates the end of the event stream", required = true)
  private Boolean finalEvent;

  /** Event type. */
  @JsonProperty("kind")
  @Schema(description = "Event type", example = "status-update")
  private String kind = "status-update";

  /** Extension metadata. */
  @JsonProperty("metadata")
  @Schema(description = "Extension metadata")
  private Map<String, Object> metadata;

  /** Current status of the task. */
  @JsonProperty("status")
  @Valid
  @NotNull
  @Schema(description = "Current status of the task", required = true)
  private TaskStatus status;

  /** Task id. */
  @JsonProperty("taskId")
  @NotBlank
  @Schema(description = "Task id", required = true)
  private String taskId;

  /** Default constructor. */
  public TaskStatusUpdateEvent() {}

  /** Gets the context ID. */
  public String getContextId() {
    return contextId;
  }

  /** Sets the context ID. */
  public void setContextId(String contextId) {
    this.contextId = contextId;
  }

  /** Gets the final flag. */
  public Boolean isFinal() {
    return finalEvent;
  }

  /** Sets the final flag. */
  public void setFinal(Boolean finalEvent) {
    this.finalEvent = finalEvent;
  }

  /** Gets the event kind. */
  public String getKind() {
    return kind;
  }

  /** Sets the event kind. */
  public void setKind(String kind) {
    this.kind = kind;
  }

  /** Gets the metadata. */
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  /** Sets the metadata. */
  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  /** Gets the task status. */
  public TaskStatus getStatus() {
    return status;
  }

  /** Sets the task status. */
  public void setStatus(TaskStatus status) {
    this.status = status;
  }

  /** Gets the task ID. */
  public String getTaskId() {
    return taskId;
  }

  /** Sets the task ID. */
  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  /** Builder for TaskStatusUpdateEvent. */
  public static Builder builder(String taskId, TaskStatus status) {
    return new Builder(taskId, status);
  }

  public static class Builder {
    private final String taskId;
    private final TaskStatus status;
    private String contextId;
    private Boolean finalEvent = false;
    private Map<String, Object> metadata;

    private Builder(String taskId, TaskStatus status) {
      this.taskId = taskId;
      this.status = status;
    }

    public Builder withContextId(String contextId) {
      this.contextId = contextId;
      return this;
    }

    public Builder withFinal(Boolean finalEvent) {
      this.finalEvent = finalEvent;
      return this;
    }

    public Builder withMetadata(Map<String, Object> metadata) {
      this.metadata = metadata;
      return this;
    }

    public TaskStatusUpdateEvent build() {
      TaskStatusUpdateEvent event = new TaskStatusUpdateEvent();
      event.taskId = this.taskId;
      event.status = this.status;
      event.contextId = this.contextId;
      event.finalEvent = this.finalEvent;
      event.metadata = this.metadata;
      return event;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    TaskStatusUpdateEvent that = (TaskStatusUpdateEvent) obj;
    return Objects.equals(contextId, that.contextId)
        && Objects.equals(finalEvent, that.finalEvent)
        && Objects.equals(status, that.status)
        && Objects.equals(taskId, that.taskId)
        && Objects.equals(metadata, that.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contextId, finalEvent, status, taskId, metadata);
  }

  @Override
  public String toString() {
    return "TaskStatusUpdateEvent{"
        + "contextId='"
        + contextId
        + '\''
        + ", final="
        + finalEvent
        + ", kind='"
        + kind
        + '\''
        + ", status="
        + status
        + ", taskId='"
        + taskId
        + '\''
        + ", metadata="
        + metadata
        + '}';
  }
}
