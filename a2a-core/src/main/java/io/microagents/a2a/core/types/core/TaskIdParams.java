package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

/**
 * Parameters containing only a task ID, used for simple task operations.
 *
 * <p>This is a simplified parameter type used by methods that only need a task identifier, such as
 * task cancellation or simple task queries.
 */
@Schema(description = "Parameters containing only a task ID, used for simple task operations")
public class TaskIdParams {

  /** Task id. */
  @JsonProperty("id")
  @NotNull
  @NotBlank
  @Schema(description = "Task id", example = "task-123", required = true)
  private String id;

  /** Extension metadata. */
  @JsonProperty("metadata")
  @Schema(description = "Extension metadata")
  private Map<String, Object> metadata;

  /** Default constructor. */
  public TaskIdParams() {}

  /**
   * Constructor with task ID.
   *
   * @param id the task ID
   */
  public TaskIdParams(String id) {
    this.id = id;
  }

  /**
   * Constructor with task ID and metadata.
   *
   * @param id the task ID
   * @param metadata extension metadata
   */
  public TaskIdParams(String id, Map<String, Object> metadata) {
    this.id = id;
    this.metadata = metadata;
  }

  /**
   * Gets the task ID.
   *
   * @return the task ID
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the task ID.
   *
   * @param id the task ID
   */
  public void setId(String id) {
    this.id = id;
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
   * Creates a new TaskIdParams with the specified task ID.
   *
   * @param id the task ID
   * @return a new TaskIdParams
   */
  public static TaskIdParams of(String id) {
    return new TaskIdParams(id);
  }

  /**
   * Creates a copy of this params with a new task ID.
   *
   * @param newId the new task ID
   * @return a new TaskIdParams with the specified ID
   */
  public TaskIdParams withId(String newId) {
    return new TaskIdParams(newId, this.metadata);
  }

  /**
   * Creates a copy of this params with new metadata.
   *
   * @param newMetadata the new metadata
   * @return a new TaskIdParams with the specified metadata
   */
  public TaskIdParams withMetadata(Map<String, Object> newMetadata) {
    return new TaskIdParams(this.id, newMetadata);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    TaskIdParams that = (TaskIdParams) obj;
    return Objects.equals(id, that.id) && Objects.equals(metadata, that.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, metadata);
  }

  @Override
  public String toString() {
    return "TaskIdParams{" + "id='" + id + '\'' + ", metadata=" + metadata + '}';
  }
}
