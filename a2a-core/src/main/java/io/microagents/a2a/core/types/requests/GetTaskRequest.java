package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * JSON-RPC request model for the 'tasks/get' method.
 *
 * <p>This request is used to retrieve the current state and history of a specific task. The
 * response will include task details, current status, and optionally recent messages.
 */
@Schema(description = "JSON-RPC request model for the 'tasks/get' method")
public class GetTaskRequest extends JSONRPCMessage {

  /** A String containing the name of the method to be invoked. */
  @JsonProperty("method")
  @NotNull
  @Schema(
      description = "A String containing the name of the method to be invoked",
      example = "tasks/get",
      required = true)
  private final String method = "tasks/get";

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
  private TaskQueryParams params;

  /** Default constructor. */
  public GetTaskRequest() {
    super();
  }

  /**
   * Constructor with params.
   *
   * @param params the task query parameters
   */
  public GetTaskRequest(TaskQueryParams params) {
    super();
    this.params = params;
  }

  /**
   * Constructor with params and ID.
   *
   * @param params the task query parameters
   * @param id the request ID
   */
  public GetTaskRequest(TaskQueryParams params, String id) {
    super(id);
    this.params = params;
  }

  /**
   * Gets the method name.
   *
   * @return always "tasks/get"
   */
  public String getMethod() {
    return method;
  }

  /**
   * Gets the task query parameters.
   *
   * @return the task query parameters
   */
  public TaskQueryParams getParams() {
    return params;
  }

  /**
   * Sets the task query parameters.
   *
   * @param params the task query parameters
   */
  public void setParams(TaskQueryParams params) {
    this.params = params;
  }

  /**
   * Creates a new GetTaskRequest with the specified parameters.
   *
   * @param params the task query parameters
   * @return a new GetTaskRequest
   */
  public static GetTaskRequest of(TaskQueryParams params) {
    return new GetTaskRequest(params);
  }

  /**
   * Creates a new GetTaskRequest with the specified task ID.
   *
   * @param taskId the task ID
   * @return a new GetTaskRequest
   */
  public static GetTaskRequest of(String taskId) {
    return new GetTaskRequest(TaskQueryParams.of(taskId));
  }

  /**
   * Creates a new GetTaskRequest with the specified task ID and history length.
   *
   * @param taskId the task ID
   * @param historyLength the number of recent messages to retrieve
   * @return a new GetTaskRequest
   */
  public static GetTaskRequest of(String taskId, Integer historyLength) {
    return new GetTaskRequest(TaskQueryParams.of(taskId, historyLength));
  }

  /**
   * Creates a copy of this request with new parameters.
   *
   * @param newParams the new parameters
   * @return a new request with the specified parameters
   */
  public GetTaskRequest withParams(TaskQueryParams newParams) {
    return new GetTaskRequest(newParams, this.getId());
  }

  /**
   * Creates a copy of this request with a new task ID.
   *
   * @param newTaskId the new task ID
   * @return a new request with the specified task ID
   */
  public GetTaskRequest withTaskId(String newTaskId) {
    TaskQueryParams newParams = this.params.withId(newTaskId);
    return new GetTaskRequest(newParams, this.getId());
  }

  /**
   * Creates a copy of this request with a new history length.
   *
   * @param newHistoryLength the new history length
   * @return a new request with the specified history length
   */
  public GetTaskRequest withHistoryLength(Integer newHistoryLength) {
    TaskQueryParams newParams = this.params.withHistoryLength(newHistoryLength);
    return new GetTaskRequest(newParams, this.getId());
  }

  @Override
  public JSONRPCMessage withId(String newId) {
    return new GetTaskRequest(this.params, newId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    GetTaskRequest that = (GetTaskRequest) obj;
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
    return "GetTaskRequest{"
        + "method='"
        + method
        + '\''
        + ", params="
        + params
        + ", id='"
        + getId()
        + '\''
        + '}';
  }
}
