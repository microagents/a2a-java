package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * JSON-RPC request model for the 'tasks/pushNotificationConfig/set' method.
 *
 * <p>This request is used to set or update the push notification configuration for a specific task,
 * enabling real-time notifications about task updates.
 */
@Schema(description = "JSON-RPC request model for the 'tasks/pushNotificationConfig/set' method")
public class SetTaskPushNotificationConfigRequest extends JSONRPCMessage {
  /** A String containing the name of the method to be invoked. */
  @JsonProperty("method")
  @NotNull
  @Schema(
      description = "A String containing the name of the method to be invoked",
      example = "tasks/pushNotificationConfig/set",
      required = true)
  private final String method = "tasks/pushNotificationConfig/set";

  /**
   * A Structured value that holds the parameter values to be used during the invocation of the
   * method.
   */
  @JsonProperty("params")
  @Valid
  @NotNull
  @Schema(
      description =
          "A Structured value that holds the parameter values to be used during the invocation of the method",
      required = true)
  private TaskPushNotificationConfig params;

  /** Default constructor. */
  public SetTaskPushNotificationConfigRequest() {
    super();
  }

  /**
   * Constructor with parameters.
   *
   * @param params the push notification configuration parameters
   */
  public SetTaskPushNotificationConfigRequest(TaskPushNotificationConfig params) {
    super();
    this.params = params;
  }

  /**
   * Constructor with parameters and ID.
   *
   * @param params the push notification configuration parameters
   * @param id the request ID
   */
  public SetTaskPushNotificationConfigRequest(TaskPushNotificationConfig params, String id) {
    super(id);
    this.params = params;
  }

  /**
   * Gets the method name.
   *
   * @return always "tasks/pushNotificationConfig/set"
   */
  public String getMethod() {
    return method;
  }

  /**
   * Gets the request parameters.
   *
   * @return the push notification configuration parameters
   */
  public TaskPushNotificationConfig getParams() {
    return params;
  }

  /**
   * Sets the request parameters.
   *
   * @param params the push notification configuration parameters
   */
  public void setParams(TaskPushNotificationConfig params) {
    this.params = params;
  }

  /**
   * Creates a new SetTaskPushNotificationConfigRequest.
   *
   * @param params the push notification configuration parameters
   * @return a new SetTaskPushNotificationConfigRequest instance
   */
  public static SetTaskPushNotificationConfigRequest of(TaskPushNotificationConfig params) {
    return new SetTaskPushNotificationConfigRequest(params);
  }

  /**
   * Creates a new SetTaskPushNotificationConfigRequest with ID.
   *
   * @param params the push notification configuration parameters
   * @param id the request ID
   * @return a new SetTaskPushNotificationConfigRequest instance
   */
  public static SetTaskPushNotificationConfigRequest of(
      TaskPushNotificationConfig params, String id) {
    return new SetTaskPushNotificationConfigRequest(params, id);
  }

  /**
   * Creates a copy of this request with new parameters.
   *
   * @param newParams the new parameters
   * @return a new request with the specified parameters
   */
  public SetTaskPushNotificationConfigRequest withParams(TaskPushNotificationConfig newParams) {
    return new SetTaskPushNotificationConfigRequest(newParams, this.getId());
  }

  @Override
  public JSONRPCMessage withId(String newId) {
    return new SetTaskPushNotificationConfigRequest(this.params, newId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    SetTaskPushNotificationConfigRequest that = (SetTaskPushNotificationConfigRequest) obj;
    return (Objects.equals(getId(), that.getId())
        && Objects.equals(method, that.method)
        && Objects.equals(params, that.params));
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), method, params);
  }

  @Override
  public String toString() {
    return ("SetTaskPushNotificationConfigRequest{"
        + "params="
        + params
        + ", method='"
        + method
        + '\''
        + ", id='"
        + getId()
        + '\''
        + '}');
  }
}
