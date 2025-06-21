package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import java.util.Objects;

/**
 * Parameters for fetching a pushNotificationConfiguration associated with a Task.
 *
 * <p>This class contains the parameters needed to retrieve push notification configuration for a
 * specific task, with optional filtering by configuration ID.
 */
@Schema(
    description = "Parameters for fetching a pushNotificationConfiguration associated with a Task")
public class GetTaskPushNotificationConfigParams {

  /** Task id. */
  @JsonProperty("id")
  @NotBlank
  @Schema(description = "Task id", required = true, example = "task-123")
  private String id;

  /** Optional metadata for the request. */
  @JsonProperty("metadata")
  @Schema(description = "Optional metadata for the request")
  private Map<String, Object> metadata;

  /** Optional push notification configuration ID to filter by. */
  @JsonProperty("pushNotificationConfigId")
  @Schema(
      description = "Optional push notification configuration ID to filter by",
      example = "config-456")
  private String pushNotificationConfigId;

  /** Default constructor. */
  public GetTaskPushNotificationConfigParams() {}

  /**
   * Constructor with task ID.
   *
   * @param id the task ID
   */
  public GetTaskPushNotificationConfigParams(String id) {
    this.id = id;
  }

  /**
   * Constructor with all parameters.
   *
   * @param id the task ID
   * @param metadata the optional metadata
   * @param pushNotificationConfigId the optional push notification configuration ID
   */
  public GetTaskPushNotificationConfigParams(
      String id, Map<String, Object> metadata, String pushNotificationConfigId) {
    this.id = id;
    this.metadata = metadata;
    this.pushNotificationConfigId = pushNotificationConfigId;
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
   * @return the metadata
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
   * Gets the push notification configuration ID.
   *
   * @return the push notification configuration ID
   */
  public String getPushNotificationConfigId() {
    return pushNotificationConfigId;
  }

  /**
   * Sets the push notification configuration ID.
   *
   * @param pushNotificationConfigId the push notification configuration ID
   */
  public void setPushNotificationConfigId(String pushNotificationConfigId) {
    this.pushNotificationConfigId = pushNotificationConfigId;
  }

  /**
   * Creates a new GetTaskPushNotificationConfigParams with just the task ID.
   *
   * @param taskId the task ID
   * @return a new GetTaskPushNotificationConfigParams instance
   */
  public static GetTaskPushNotificationConfigParams of(String taskId) {
    return new GetTaskPushNotificationConfigParams(taskId);
  }

  /**
   * Creates a new GetTaskPushNotificationConfigParams with task ID and config ID.
   *
   * @param taskId the task ID
   * @param configId the push notification configuration ID
   * @return a new GetTaskPushNotificationConfigParams instance
   */
  public static GetTaskPushNotificationConfigParams of(String taskId, String configId) {
    return new GetTaskPushNotificationConfigParams(taskId, null, configId);
  }

  /**
   * Creates a copy of this parameters object with a new task ID.
   *
   * @param newId the new task ID
   * @return a new parameters object with the specified task ID
   */
  public GetTaskPushNotificationConfigParams withId(String newId) {
    return new GetTaskPushNotificationConfigParams(
        newId, this.metadata, this.pushNotificationConfigId);
  }

  /**
   * Creates a copy of this parameters object with new metadata.
   *
   * @param newMetadata the new metadata
   * @return a new parameters object with the specified metadata
   */
  public GetTaskPushNotificationConfigParams withMetadata(Map<String, Object> newMetadata) {
    return new GetTaskPushNotificationConfigParams(
        this.id, newMetadata, this.pushNotificationConfigId);
  }

  /**
   * Creates a copy of this parameters object with a new push notification configuration ID.
   *
   * @param newConfigId the new push notification configuration ID
   * @return a new parameters object with the specified configuration ID
   */
  public GetTaskPushNotificationConfigParams withPushNotificationConfigId(String newConfigId) {
    return new GetTaskPushNotificationConfigParams(this.id, this.metadata, newConfigId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    GetTaskPushNotificationConfigParams that = (GetTaskPushNotificationConfigParams) obj;
    return Objects.equals(id, that.id)
        && Objects.equals(metadata, that.metadata)
        && Objects.equals(pushNotificationConfigId, that.pushNotificationConfigId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, metadata, pushNotificationConfigId);
  }

  @Override
  public String toString() {
    return "GetTaskPushNotificationConfigParams{"
        + "id='"
        + id
        + '\''
        + ", metadata="
        + metadata
        + ", pushNotificationConfigId='"
        + pushNotificationConfigId
        + '\''
        + '}';
  }
}
