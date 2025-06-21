package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * JSON-RPC success response model for the 'tasks/cancel' method.
 *
 * <p>This response contains the updated task after a cancellation request has been processed,
 * showing the new task state and status.
 */
@Schema(description = "JSON-RPC success response model for the 'tasks/cancel' method")
public class CancelTaskSuccessResponse extends JSONRPCMessage {

  /** The result object on success - the updated Task after cancellation */
  @JsonProperty("result")
  @Valid
  @NotNull
  @Schema(
      description = "The result object on success - the updated Task after cancellation",
      required = true)
  private Task result;

  /** Default constructor. */
  public CancelTaskSuccessResponse() {
    super();
  }

  /**
   * Constructor with result.
   *
   * @param result the task result
   */
  public CancelTaskSuccessResponse(Task result) {
    super();
    this.result = result;
  }

  /**
   * Constructor with result and ID.
   *
   * @param result the task result
   * @param id the response ID
   */
  public CancelTaskSuccessResponse(Task result, String id) {
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
   * Creates a new CancelTaskSuccessResponse with a Task result.
   *
   * @param task the task result
   * @return a new CancelTaskSuccessResponse
   */
  public static CancelTaskSuccessResponse of(Task task) {
    return new CancelTaskSuccessResponse(task);
  }

  /**
   * Creates a new CancelTaskSuccessResponse with a Task result and ID.
   *
   * @param task the task result
   * @param id the response ID
   * @return a new CancelTaskSuccessResponse
   */
  public static CancelTaskSuccessResponse of(Task task, String id) {
    return new CancelTaskSuccessResponse(task, id);
  }

  /**
   * Creates a copy of this response with a new result.
   *
   * @param newResult the new task result
   * @return a new response with the specified result
   */
  public CancelTaskSuccessResponse withResult(Task newResult) {
    return new CancelTaskSuccessResponse(newResult, this.getId());
  }

  @Override
  public JSONRPCMessage withId(String newId) {
    return new CancelTaskSuccessResponse(this.result, newId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    CancelTaskSuccessResponse that = (CancelTaskSuccessResponse) obj;
    return Objects.equals(getId(), that.getId()) && Objects.equals(result, that.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), result);
  }

  @Override
  public String toString() {
    return "CancelTaskSuccessResponse{" + "result=" + result + ", id='" + getId() + '\'' + '}';
  }
}
