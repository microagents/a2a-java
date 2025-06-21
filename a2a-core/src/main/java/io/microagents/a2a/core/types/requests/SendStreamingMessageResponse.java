package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * JSON-RPC response model for the 'message/stream' method.
 *
 * <p>This is a wrapper type that can contain either a success response or an error response. This
 * follows the JSON-RPC 2.0 specification where a response contains either a "result" field
 * (success) or an "error" field (error), but never both.
 *
 * <p>Based on the Python implementation, this acts as a wrapper that can contain:
 *
 * <ul>
 *   <li>{@link JSONRPCErrorResponse} - when an error occurs
 *   <li>{@link SendStreamingMessageSuccessResponse} - when the operation succeeds
 * </ul>
 */
@Schema(description = "JSON-RPC response model for the 'message/stream' method")
public class SendStreamingMessageResponse {

  @JsonUnwrapped private final JSONRPCMessage response;

  /**
   * Constructor with a JSONRPCMessage (success or error).
   *
   * @param response the underlying response
   */
  private SendStreamingMessageResponse(JSONRPCMessage response) {
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
    return response instanceof SendStreamingMessageSuccessResponse;
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
  public SendStreamingMessageSuccessResponse asSuccessResponse() {
    return isSuccess() ? (SendStreamingMessageSuccessResponse) response : null;
  }

  /**
   * Creates a SendStreamingMessageResponse from an error response.
   *
   * @param errorResponse the error response
   * @return a SendStreamingMessageResponse containing the error
   */
  public static SendStreamingMessageResponse fromError(JSONRPCErrorResponse errorResponse) {
    return new SendStreamingMessageResponse(errorResponse);
  }

  /**
   * Creates a SendStreamingMessageResponse from a success response.
   *
   * @param successResponse the success response
   * @return a SendStreamingMessageResponse containing the success result
   */
  public static SendStreamingMessageResponse fromSuccess(
      SendStreamingMessageSuccessResponse successResponse) {
    return new SendStreamingMessageResponse(successResponse);
  }

  /**
   * Creates an error response.
   *
   * @param error the error details
   * @return a SendStreamingMessageResponse containing the error
   */
  public static SendStreamingMessageResponse error(JSONRPCError error) {
    return new SendStreamingMessageResponse(JSONRPCErrorResponse.of(error));
  }

  /**
   * Creates an error response with ID.
   *
   * @param error the error details
   * @param id the response ID
   * @return a SendStreamingMessageResponse containing the error
   */
  public static SendStreamingMessageResponse error(JSONRPCError error, String id) {
    return new SendStreamingMessageResponse(JSONRPCErrorResponse.of(error, id));
  }

  /**
   * Creates a SendStreamingMessageResponse from a Task result.
   *
   * @param task the task result
   * @return a SendStreamingMessageResponse containing the task
   */
  public static SendStreamingMessageResponse success(Task task) {
    return new SendStreamingMessageResponse(SendStreamingMessageSuccessResponse.ofTask(task));
  }

  /**
   * Creates a SendStreamingMessageResponse from a Message result.
   *
   * @param message the message result
   * @return a SendStreamingMessageResponse containing the message
   */
  public static SendStreamingMessageResponse success(Message message) {
    return new SendStreamingMessageResponse(SendStreamingMessageSuccessResponse.ofMessage(message));
  }

  /**
   * Creates a SendStreamingMessageResponse from a TaskStatusUpdateEvent result.
   *
   * @param event the task status update event result
   * @return a SendStreamingMessageResponse containing the event
   */
  public static SendStreamingMessageResponse success(TaskStatusUpdateEvent event) {
    return new SendStreamingMessageResponse(
        SendStreamingMessageSuccessResponse.ofTaskStatusUpdateEvent(event));
  }

  /**
   * Creates a SendStreamingMessageResponse from a TaskArtifactUpdateEvent result.
   *
   * @param event the task artifact update event result
   * @return a SendStreamingMessageResponse containing the event
   */
  public static SendStreamingMessageResponse success(TaskArtifactUpdateEvent event) {
    return new SendStreamingMessageResponse(
        SendStreamingMessageSuccessResponse.ofTaskArtifactUpdateEvent(event));
  }

  @Override
  public String toString() {
    return "SendStreamingMessageResponse{" + response + "}";
  }
}
