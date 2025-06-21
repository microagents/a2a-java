package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * JSON-RPC success response for the 'tasks/get' method.
 *
 * <p>This response contains the requested task with its current state, status, history, and
 * associated artifacts.
 */
@Schema(description = "JSON-RPC success response for the 'tasks/get' method")
public class GetTaskSuccessResponse extends JSONRPCMessage {

  /** The result object on success - the requested Task */
  @JsonProperty("result")
  @Valid
  @NotNull
  @Schema(description = "The result object on success - the requested Task", required = true)
  private Task result;

  /** Default constructor. */
  public GetTaskSuccessResponse() {
    super();
  }

  /**
   * Constructor with result.
   *
   * @param result the task result
   */
  public GetTaskSuccessResponse(Task result) {
    super();
    this.result = result;
  }

  /**
   * Constructor with result and ID.
   *
   * @param result the task result
   * @param id the response ID
   */
  public GetTaskSuccessResponse(Task result, String id) {
    super(id);
    this.result = result;
  }

  /**
   * Gets the result task.
   *
   * @return the task result
   */
  public Task getResult() {
    return result;
  }

  /**
   * Sets the result task.
   *
   * @param result the task result
   */
  public void setResult(Task result) {
    this.result = result;
  }

  /**
   * Creates a new GetTaskSuccessResponse with a Task result.
   *
   * @param task the task result
   * @return a new GetTaskSuccessResponse
   */
  public static GetTaskSuccessResponse of(Task task) {
    return new GetTaskSuccessResponse(task);
  }

  /**
   * Creates a new GetTaskSuccessResponse with a Task result and ID.
   *
   * @param task the task result
   * @param id the response ID
   * @return a new GetTaskSuccessResponse
   */
  public static GetTaskSuccessResponse of(Task task, String id) {
    return new GetTaskSuccessResponse(task, id);
  }

  /**
   * Creates a copy of this response with a new result.
   *
   * @param newResult the new task result
   * @return a new response with the specified result
   */
  public GetTaskSuccessResponse withResult(Task newResult) {
    return new GetTaskSuccessResponse(newResult, this.getId());
  }

  @Override
  public JSONRPCMessage withId(String newId) {
    return new GetTaskSuccessResponse(this.result, newId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    GetTaskSuccessResponse that = (GetTaskSuccessResponse) obj;
    return Objects.equals(getId(), that.getId()) && Objects.equals(result, that.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), result);
  }

  @Override
  public String toString() {
    return "GetTaskSuccessResponse{" + "result=" + result + ", id='" + getId() + '\'' + '}';
  }
}
