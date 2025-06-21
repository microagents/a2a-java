package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Parameters for setting or getting push notification configuration for a task.
 *
 * <p>This class represents the configuration that binds a task to a push notification endpoint and
 * authentication details for real-time task updates.
 */
@Schema(
    description = "Parameters for setting or getting push notification configuration for a task")
public class TaskPushNotificationConfig {

  /** Task id. */
  @JsonProperty("taskId")
  @NotBlank
  @Schema(description = "Task id", required = true, example = "task-123")
  private String taskId;

  /** Push notification configuration. */
  @JsonProperty("pushNotificationConfig")
  @Valid
  @NotNull
  @Schema(description = "Push notification configuration", required = true)
  private PushNotificationConfig pushNotificationConfig;

  /** Default constructor. */
  public TaskPushNotificationConfig() {}

  /**
   * Constructor with task ID and push notification config.
   *
   * @param taskId the task ID
   * @param pushNotificationConfig the push notification configuration
   */
  public TaskPushNotificationConfig(String taskId, PushNotificationConfig pushNotificationConfig) {
    this.taskId = taskId;
    this.pushNotificationConfig = pushNotificationConfig;
  }

  /**
   * Gets the task ID.
   *
   * @return the task ID
   */
  public String getTaskId() {
    return taskId;
  }

  /**
   * Sets the task ID.
   *
   * @param taskId the task ID
   */
  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  /**
   * Gets the push notification configuration.
   *
   * @return the push notification configuration
   */
  public PushNotificationConfig getPushNotificationConfig() {
    return pushNotificationConfig;
  }

  /**
   * Sets the push notification configuration.
   *
   * @param pushNotificationConfig the push notification configuration
   */
  public void setPushNotificationConfig(PushNotificationConfig pushNotificationConfig) {
    this.pushNotificationConfig = pushNotificationConfig;
  }

  /**
   * Creates a new TaskPushNotificationConfig.
   *
   * @param taskId the task ID
   * @param pushNotificationConfig the push notification configuration
   * @return a new TaskPushNotificationConfig instance
   */
  public static TaskPushNotificationConfig of(
      String taskId, PushNotificationConfig pushNotificationConfig) {
    return new TaskPushNotificationConfig(taskId, pushNotificationConfig);
  }

  /**
   * Creates a copy of this configuration with a new task ID.
   *
   * @param newTaskId the new task ID
   * @return a new configuration with the specified task ID
   */
  public TaskPushNotificationConfig withTaskId(String newTaskId) {
    return new TaskPushNotificationConfig(newTaskId, this.pushNotificationConfig);
  }

  /**
   * Creates a copy of this configuration with a new push notification config.
   *
   * @param newConfig the new push notification configuration
   * @return a new configuration with the specified push notification config
   */
  public TaskPushNotificationConfig withPushNotificationConfig(PushNotificationConfig newConfig) {
    return new TaskPushNotificationConfig(this.taskId, newConfig);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    TaskPushNotificationConfig that = (TaskPushNotificationConfig) obj;
    return Objects.equals(taskId, that.taskId)
        && Objects.equals(pushNotificationConfig, that.pushNotificationConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hash(taskId, pushNotificationConfig);
  }

  @Override
  public String toString() {
    return "TaskPushNotificationConfig{"
        + "taskId='"
        + taskId
        + '\''
        + ", pushNotificationConfig="
        + pushNotificationConfig
        + '}';
  }
}
