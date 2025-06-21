package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * JSON-RPC response for the 'tasks/get' method.
 *
 * <p>This is a wrapper type that can contain either a success response or an error response. This
 * follows the JSON-RPC 2.0 specification where a response contains either a "result" field
 * (success) or an "error" field (error), but never both.
 *
 * <p>Based on the Python implementation, this acts as a wrapper that can contain:
 *
 * <ul>
 *   <li>{@link JSONRPCErrorResponse} - when an error occurs
 *   <li>{@link GetTaskSuccessResponse} - when the operation succeeds
 * </ul>
 */
@Schema(description = "JSON-RPC response for the 'tasks/get' method")
public class GetTaskResponse {

  @JsonUnwrapped private final JSONRPCMessage response;

  /**
   * Constructor with a JSONRPCMessage (success or error).
   *
   * @param response the underlying response
   */
  private GetTaskResponse(JSONRPCMessage response) {
    this.response = response;
  }

  /**
   * Gets the underlying response.
   *
   * @return the underlying JSONRPCMessage
   */
  @JsonIgnore
  public JSONRPCMessage getResponse() {
    return response;
  }

  /**
   * Checks if this response represents an error.
   *
   * @return true if this is an error response
   */
  @JsonIgnore
  public boolean isError() {
    return response instanceof JSONRPCErrorResponse;
  }

  /**
   * Checks if this response represents a success.
   *
   * @return true if this is a success response
   */
  @JsonIgnore
  public boolean isSuccess() {
    return response instanceof GetTaskSuccessResponse;
  }

  /**
   * Gets this response as an error response if it is one.
   *
   * @return the error response, or null if this is not an error response
   */
  @JsonIgnore
  public JSONRPCErrorResponse asErrorResponse() {
    return isError() ? (JSONRPCErrorResponse) response : null;
  }

  /**
   * Gets this response as a success response if it is one.
   *
   * @return the success response, or null if this is not a success response
   */
  @JsonIgnore
  public GetTaskSuccessResponse asSuccessResponse() {
    return isSuccess() ? (GetTaskSuccessResponse) response : null;
  }

  /**
   * Gets the task result if this is a success response.
   *
   * @return the task result, or null if this is an error response
   */
  @JsonIgnore
  public Task getTask() {
    GetTaskSuccessResponse successResponse = asSuccessResponse();
    return successResponse != null ? successResponse.getResult() : null;
  }

  /**
   * Creates a GetTaskResponse from an error response.
   *
   * @param errorResponse the error response
   * @return a GetTaskResponse containing the error
   */
  public static GetTaskResponse fromError(JSONRPCErrorResponse errorResponse) {
    return new GetTaskResponse(errorResponse);
  }

  /**
   * Creates a GetTaskResponse from a success response.
   *
   * @param successResponse the success response
   * @return a GetTaskResponse containing the success result
   */
  public static GetTaskResponse fromSuccess(GetTaskSuccessResponse successResponse) {
    return new GetTaskResponse(successResponse);
  }

  /**
   * Creates an error response.
   *
   * @param error the error details
   * @return a GetTaskResponse containing the error
   */
  public static GetTaskResponse error(JSONRPCError error) {
    return new GetTaskResponse(JSONRPCErrorResponse.of(error));
  }

  /**
   * Creates an error response with ID.
   *
   * @param error the error details
   * @param id the response ID
   * @return a GetTaskResponse containing the error
   */
  public static GetTaskResponse error(JSONRPCError error, String id) {
    return new GetTaskResponse(JSONRPCErrorResponse.of(error, id));
  }

  /**
   * Creates a GetTaskResponse from a Task result.
   *
   * @param task the task result
   * @return a GetTaskResponse containing the task
   */
  public static GetTaskResponse success(Task task) {
    return new GetTaskResponse(GetTaskSuccessResponse.of(task));
  }

  /**
   * Creates a GetTaskResponse from a Task result with ID.
   *
   * @param task the task result
   * @param id the response ID
   * @return a GetTaskResponse containing the task
   */
  public static GetTaskResponse success(Task task, String id) {
    return new GetTaskResponse(GetTaskSuccessResponse.of(task, id));
  }

  @Override
  public String toString() {
    return "GetTaskResponse{" + response + "}";
  }
}
