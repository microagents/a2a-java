package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * JSON-RPC request model for the 'tasks/pushNotificationConfig/get' method.
 *
 * <p>This request is used to retrieve the push notification configuration for a specific task. The
 * parameters can be either TaskIdParams (deprecated) or GetTaskPushNotificationConfigParams for
 * more specific filtering.
 */
@Schema(description = "JSON-RPC request model for the 'tasks/pushNotificationConfig/get' method")
public class GetTaskPushNotificationConfigRequest extends JSONRPCMessage {

  /** A String containing the name of the method to be invoked. */
  @JsonProperty("method")
  @NotNull
  @Schema(
      description = "A String containing the name of the method to be invoked",
      example = "tasks/pushNotificationConfig/get",
      required = true)
  private final String method = "tasks/pushNotificationConfig/get";

  /**
   * A Structured value that holds the parameter values to be used during the invocation of the
   * method. TaskIdParams type is deprecated for this method - use
   * GetTaskPushNotificationConfigParams instead.
   */
  @JsonProperty("params")
  @Valid
  @NotNull
  @Schema(
      description =
          "A Structured value that holds the parameter values to be used during the invocation of the method",
      required = true)
  private Object params; // Can be TaskIdParams or GetTaskPushNotificationConfigParams

  /** Default constructor. */
  public GetTaskPushNotificationConfigRequest() {
    super();
  }

  /**
   * Constructor with TaskIdParams (deprecated).
   *
   * @param params the task ID parameters
   */
  public GetTaskPushNotificationConfigRequest(TaskIdParams params) {
    super();
    this.params = params;
  }

  /**
   * Constructor with GetTaskPushNotificationConfigParams.
   *
   * @param params the push notification configuration parameters
   */
  public GetTaskPushNotificationConfigRequest(GetTaskPushNotificationConfigParams params) {
    super();
    this.params = params;
  }

  /**
   * Constructor with parameters and ID.
   *
   * @param params the parameters (TaskIdParams or GetTaskPushNotificationConfigParams)
   * @param id the request ID
   */
  public GetTaskPushNotificationConfigRequest(Object params, String id) {
    super(id);
    this.params = params;
  }

  /**
   * Gets the method name.
   *
   * @return always "tasks/pushNotificationConfig/get"
   */
  public String getMethod() {
    return method;
  }

  /**
   * Gets the request parameters.
   *
   * @return the parameters (TaskIdParams or GetTaskPushNotificationConfigParams)
   */
  public Object getParams() {
    return params;
  }

  /**
   * Sets the request parameters.
   *
   * @param params the parameters (TaskIdParams or GetTaskPushNotificationConfigParams)
   */
  public void setParams(Object params) {
    this.params = params;
  }

  /**
   * Gets the parameters as TaskIdParams if they are of that type.
   *
   * @return the parameters as TaskIdParams, or null if not of that type
   */
  public TaskIdParams getParamsAsTaskIdParams() {
    return params instanceof TaskIdParams ? (TaskIdParams) params : null;
  }

  /**
   * Gets the parameters as GetTaskPushNotificationConfigParams if they are of that type.
   *
   * @return the parameters as GetTaskPushNotificationConfigParams, or null if not of that type
   */
  public GetTaskPushNotificationConfigParams getParamsAsGetTaskPushNotificationConfigParams() {
    return params instanceof GetTaskPushNotificationConfigParams
        ? (GetTaskPushNotificationConfigParams) params
        : null;
  }

  /**
   * Checks if the parameters are TaskIdParams.
   *
   * @return true if the parameters are TaskIdParams
   */
  public boolean isTaskIdParams() {
    return params instanceof TaskIdParams;
  }

  /**
   * Checks if the parameters are GetTaskPushNotificationConfigParams.
   *
   * @return true if the parameters are GetTaskPushNotificationConfigParams
   */
  public boolean isGetTaskPushNotificationConfigParams() {
    return params instanceof GetTaskPushNotificationConfigParams;
  }

  /**
   * Creates a new GetTaskPushNotificationConfigRequest with TaskIdParams (deprecated).
   *
   * @param params the task ID parameters
   * @return a new GetTaskPushNotificationConfigRequest instance
   */
  public static GetTaskPushNotificationConfigRequest of(TaskIdParams params) {
    return new GetTaskPushNotificationConfigRequest(params);
  }

  /**
   * Creates a new GetTaskPushNotificationConfigRequest with GetTaskPushNotificationConfigParams.
   *
   * @param params the push notification configuration parameters
   * @return a new GetTaskPushNotificationConfigRequest instance
   */
  public static GetTaskPushNotificationConfigRequest of(
      GetTaskPushNotificationConfigParams params) {
    return new GetTaskPushNotificationConfigRequest(params);
  }

  /**
   * Creates a new GetTaskPushNotificationConfigRequest with just a task ID.
   *
   * @param taskId the task ID
   * @return a new GetTaskPushNotificationConfigRequest instance
   */
  public static GetTaskPushNotificationConfigRequest ofTaskId(String taskId) {
    return new GetTaskPushNotificationConfigRequest(GetTaskPushNotificationConfigParams.of(taskId));
  }

  /**
   * Creates a copy of this request with new parameters.
   *
   * @param newParams the new parameters
   * @return a new request with the specified parameters
   */
  public GetTaskPushNotificationConfigRequest withParams(Object newParams) {
    return new GetTaskPushNotificationConfigRequest(newParams, this.getId());
  }

  @Override
  public JSONRPCMessage withId(String newId) {
    return new GetTaskPushNotificationConfigRequest(this.params, newId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    GetTaskPushNotificationConfigRequest that = (GetTaskPushNotificationConfigRequest) obj;
    return Objects.equals(getId(), that.getId())
        && Objects.equals(method, that.method)
        && Objects.equals(params, that.params);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), method, params);
  }

  @Override
  public String toString() {
    return "GetTaskPushNotificationConfigRequest{"
        + "params="
        + params
        + ", method='"
        + method
        + '\''
        + ", id='"
        + getId()
        + '\''
        + '}';
  }
}
