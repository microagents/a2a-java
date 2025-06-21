package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * JSON-RPC request model for the 'tasks/cancel' method.
 *
 * <p>This request is used to cancel a specific task. The agent will attempt to cancel the task and
 * return the updated task with a canceled status.
 */
@Schema(description = "JSON-RPC request model for the 'tasks/cancel' method")
public class CancelTaskRequest extends JSONRPCMessage {

  /** A String containing the name of the method to be invoked. */
  @JsonProperty("method")
  @NotNull
  @Schema(
      description = "A String containing the name of the method to be invoked",
      example = "tasks/cancel",
      required = true)
  private final String method = "tasks/cancel";

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
  private TaskIdParams params;

  /** Default constructor. */
  public CancelTaskRequest() {
    super();
  }

  /**
   * Constructor with params.
   *
   * @param params the task ID parameters
   */
  public CancelTaskRequest(TaskIdParams params) {
    super();
    this.params = params;
  }

  /**
   * Constructor with params and ID.
   *
   * @param params the task ID parameters
   * @param id the request ID
   */
  public CancelTaskRequest(TaskIdParams params, String id) {
    super(id);
    this.params = params;
  }

  /**
   * Gets the method name.
   *
   * @return always "tasks/cancel"
   */
  public String getMethod() {
    return method;
  }

  /**
   * Gets the task ID parameters.
   *
   * @return the task ID parameters
   */
  public TaskIdParams getParams() {
    return params;
  }

  /**
   * Sets the task ID parameters.
   *
   * @param params the task ID parameters
   */
  public void setParams(TaskIdParams params) {
    this.params = params;
  }

  /**
   * Creates a new CancelTaskRequest with the specified parameters.
   *
   * @param params the task ID parameters
   * @return a new CancelTaskRequest
   */
  public static CancelTaskRequest of(TaskIdParams params) {
    return new CancelTaskRequest(params);
  }

  /**
   * Creates a new CancelTaskRequest with the specified task ID.
   *
   * @param taskId the task ID
   * @return a new CancelTaskRequest
   */
  public static CancelTaskRequest of(String taskId) {
    return new CancelTaskRequest(TaskIdParams.of(taskId));
  }

  /**
   * Creates a copy of this request with new parameters.
   *
   * @param newParams the new parameters
   * @return a new request with the specified parameters
   */
  public CancelTaskRequest withParams(TaskIdParams newParams) {
    return new CancelTaskRequest(newParams, this.getId());
  }

  /**
   * Creates a copy of this request with a new task ID.
   *
   * @param newTaskId the new task ID
   * @return a new request with the specified task ID
   */
  public CancelTaskRequest withTaskId(String newTaskId) {
    TaskIdParams newParams = this.params.withId(newTaskId);
    return new CancelTaskRequest(newParams, this.getId());
  }

  @Override
  public JSONRPCMessage withId(String newId) {
    return new CancelTaskRequest(this.params, newId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    CancelTaskRequest that = (CancelTaskRequest) obj;
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
    return "CancelTaskRequest{"
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
