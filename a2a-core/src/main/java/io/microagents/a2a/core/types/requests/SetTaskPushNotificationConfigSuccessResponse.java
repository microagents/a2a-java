package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * JSON-RPC success response model for the 'tasks/pushNotificationConfig/set' method.
 *
 * <p>This response contains the updated push notification configuration that was successfully set
 * for the specified task.
 */
@Schema(
    description =
        "JSON-RPC success response model for the 'tasks/pushNotificationConfig/set' method")
public class SetTaskPushNotificationConfigSuccessResponse extends JSONRPCMessage {

  /** The result object on success - the updated TaskPushNotificationConfig */
  @JsonProperty("result")
  @Valid
  @NotNull
  @Schema(
      description = "The result object on success - the updated TaskPushNotificationConfig",
      required = true)
  private TaskPushNotificationConfig result;

  /** Default constructor. */
  public SetTaskPushNotificationConfigSuccessResponse() {
    super();
  }

  /**
   * Constructor with result.
   *
   * @param result the push notification configuration result
   */
  public SetTaskPushNotificationConfigSuccessResponse(TaskPushNotificationConfig result) {
    super();
    this.result = result;
  }

  /**
   * Constructor with result and ID.
   *
   * @param result the push notification configuration result
   * @param id the response ID
   */
  public SetTaskPushNotificationConfigSuccessResponse(
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
   * Creates a new SetTaskPushNotificationConfigSuccessResponse with a TaskPushNotificationConfig
   * result.
   *
   * @param config the push notification configuration result
   * @return a new SetTaskPushNotificationConfigSuccessResponse
   */
  public static SetTaskPushNotificationConfigSuccessResponse of(TaskPushNotificationConfig config) {
    return new SetTaskPushNotificationConfigSuccessResponse(config);
  }

  /**
   * Creates a new SetTaskPushNotificationConfigSuccessResponse with a TaskPushNotificationConfig
   * result and ID.
   *
   * @param config the push notification configuration result
   * @param id the response ID
   * @return a new SetTaskPushNotificationConfigSuccessResponse
   */
  public static SetTaskPushNotificationConfigSuccessResponse of(
      TaskPushNotificationConfig config, String id) {
    return new SetTaskPushNotificationConfigSuccessResponse(config, id);
  }

  /**
   * Creates a copy of this response with a new result.
   *
   * @param newResult the new push notification configuration result
   * @return a new response with the specified result
   */
  public SetTaskPushNotificationConfigSuccessResponse withResult(
      TaskPushNotificationConfig newResult) {
    return new SetTaskPushNotificationConfigSuccessResponse(newResult, this.getId());
  }

  @Override
  public JSONRPCMessage withId(String newId) {
    return new SetTaskPushNotificationConfigSuccessResponse(this.result, newId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    SetTaskPushNotificationConfigSuccessResponse that =
        (SetTaskPushNotificationConfigSuccessResponse) obj;
    return Objects.equals(getId(), that.getId()) && Objects.equals(result, that.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), result);
  }

  @Override
  public String toString() {
    return "SetTaskPushNotificationConfigSuccessResponse{"
        + "result="
        + result
        + ", id='"
        + getId()
        + '\''
        + '}';
  }
}
