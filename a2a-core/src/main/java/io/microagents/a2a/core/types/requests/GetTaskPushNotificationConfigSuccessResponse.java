package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * JSON-RPC success response model for the 'tasks/pushNotificationConfig/get' method.
 *
 * <p>This response contains the push notification configuration that was retrieved for the
 * specified task.
 */
@Schema(
    description =
        "JSON-RPC success response model for the 'tasks/pushNotificationConfig/get' method")
public class GetTaskPushNotificationConfigSuccessResponse extends JSONRPCMessage {

  /** The result object on success - the retrieved TaskPushNotificationConfig */
  @JsonProperty("result")
  @Valid
  @NotNull
  @Schema(
      description = "The result object on success - the retrieved TaskPushNotificationConfig",
      required = true)
  private TaskPushNotificationConfig result;

  /** Default constructor. */
  public GetTaskPushNotificationConfigSuccessResponse() {
    super();
  }

  /**
   * Constructor with result.
   *
   * @param result the push notification configuration result
   */
  public GetTaskPushNotificationConfigSuccessResponse(TaskPushNotificationConfig result) {
    super();
    this.result = result;
  }

  /**
   * Constructor with result and ID.
   *
   * @param result the push notification configuration result
   * @param id the response ID
   */
  public GetTaskPushNotificationConfigSuccessResponse(
      TaskPushNotificationConfig result, String id) {
    super(id);
    this.result = result;
  }

  /**
   * Gets the result push notification configuration.
   *
   * @return the push notification configuration result
   */
  public TaskPushNotificationConfig getResult() {
    return result;
  }

  /**
   * Sets the result push notification configuration.
   *
   * @param result the push notification configuration result
   */
  public void setResult(TaskPushNotificationConfig result) {
    this.result = result;
  }

  /**
   * Creates a new GetTaskPushNotificationConfigSuccessResponse with a TaskPushNotificationConfig
   * result.
   *
   * @param config the push notification configuration result
   * @return a new GetTaskPushNotificationConfigSuccessResponse
   */
  public static GetTaskPushNotificationConfigSuccessResponse of(TaskPushNotificationConfig config) {
    return new GetTaskPushNotificationConfigSuccessResponse(config);
  }

  /**
   * Creates a new GetTaskPushNotificationConfigSuccessResponse with a TaskPushNotificationConfig
   * result and ID.
   *
   * @param config the push notification configuration result
   * @param id the response ID
   * @return a new GetTaskPushNotificationConfigSuccessResponse
   */
  public static GetTaskPushNotificationConfigSuccessResponse of(
      TaskPushNotificationConfig config, String id) {
    return new GetTaskPushNotificationConfigSuccessResponse(config, id);
  }

  /**
   * Creates a copy of this response with a new result.
   *
   * @param newResult the new push notification configuration result
   * @return a new response with the specified result
   */
  public GetTaskPushNotificationConfigSuccessResponse withResult(
      TaskPushNotificationConfig newResult) {
    return new GetTaskPushNotificationConfigSuccessResponse(newResult, this.getId());
  }

  @Override
  public JSONRPCMessage withId(String newId) {
    return new GetTaskPushNotificationConfigSuccessResponse(this.result, newId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    GetTaskPushNotificationConfigSuccessResponse that =
        (GetTaskPushNotificationConfigSuccessResponse) obj;
    return Objects.equals(getId(), that.getId()) && Objects.equals(result, that.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), result);
  }

  @Override
  public String toString() {
    return "GetTaskPushNotificationConfigSuccessResponse{"
        + "result="
        + result
        + ", id='"
        + getId()
        + '\''
        + '}';
  }
}
