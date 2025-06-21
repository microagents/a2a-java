package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Map;
import java.util.Objects;

/**
 * Parameters for querying a task, including optional history length.
 *
 * <p>Used by the tasks/get method to retrieve task information with optional filtering of message
 * history.
 */
@Schema(description = "Parameters for querying a task, including optional history length")
public class TaskQueryParams {

  /** Task id. */
  @JsonProperty("id")
  @NotNull
  @NotBlank
  @Schema(description = "Task id", example = "task-123", required = true)
  private String id;

  /** Number of recent messages to be retrieved. */
  @JsonProperty("historyLength")
  @Positive
  @Schema(description = "Number of recent messages to be retrieved", example = "10")
  private Integer historyLength;

  /** Extension metadata. */
  @JsonProperty("metadata")
  @Schema(description = "Extension metadata")
  private Map<String, Object> metadata;

  /** Default constructor. */
  public TaskQueryParams() {}

  /**
   * Constructor with task ID.
   *
   * @param id the task ID
   */
  public TaskQueryParams(String id) {
    this.id = id;
  }

  /**
   * Constructor with task ID and history length.
   *
   * @param id the task ID
   * @param historyLength the number of recent messages to retrieve
   */
  public TaskQueryParams(String id, Integer historyLength) {
    this.id = id;
    this.historyLength = historyLength;
  }

  /**
   * Full constructor.
   *
   * @param id the task ID
   * @param historyLength the number of recent messages to retrieve
   * @param metadata extension metadata
   */
  public TaskQueryParams(String id, Integer historyLength, Map<String, Object> metadata) {
    this.id = id;
    this.historyLength = historyLength;
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
   * Creates a new TaskQueryParams with the specified task ID.
   *
   * @param id the task ID
   * @return a new TaskQueryParams
   */
  public static TaskQueryParams of(String id) {
    return new TaskQueryParams(id);
  }

  /**
   * Creates a new TaskQueryParams with the specified task ID and history length.
   *
   * @param id the task ID
   * @param historyLength the history length
   * @return a new TaskQueryParams
   */
  public static TaskQueryParams of(String id, Integer historyLength) {
    return new TaskQueryParams(id, historyLength);
  }

  /**
   * Creates a copy of this params with a new task ID.
   *
   * @param newId the new task ID
   * @return a new TaskQueryParams with the specified ID
   */
  public TaskQueryParams withId(String newId) {
    return new TaskQueryParams(newId, this.historyLength, this.metadata);
  }

  /**
   * Creates a copy of this params with a new history length.
   *
   * @param newHistoryLength the new history length
   * @return a new TaskQueryParams with the specified history length
   */
  public TaskQueryParams withHistoryLength(Integer newHistoryLength) {
    return new TaskQueryParams(this.id, newHistoryLength, this.metadata);
  }

  /**
   * Creates a copy of this params with new metadata.
   *
   * @param newMetadata the new metadata
   * @return a new TaskQueryParams with the specified metadata
   */
  public TaskQueryParams withMetadata(Map<String, Object> newMetadata) {
    return new TaskQueryParams(this.id, this.historyLength, newMetadata);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    TaskQueryParams that = (TaskQueryParams) obj;
    return Objects.equals(id, that.id)
        && Objects.equals(historyLength, that.historyLength)
        && Objects.equals(metadata, that.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, historyLength, metadata);
  }

  @Override
  public String toString() {
    return "TaskQueryParams{"
        + "id='"
        + id
        + '\''
        + ", historyLength="
        + historyLength
        + ", metadata="
        + metadata
        + '}';
  }
}
