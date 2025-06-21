package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * JSON-RPC response model for the 'message/send' method.
 *
 * <p>This is a wrapper type that can contain either a success response (with Task or Message
 * result) or an error response. This follows the JSON-RPC 2.0 specification where a response
 * contains either a "result" field (success) or an "error" field (error), but never both.
 */
@Schema(description = "JSON-RPC response model for the 'message/send' method")
public class SendMessageResponse {

  @JsonUnwrapped private final JSONRPCMessage response;

  /**
   * Constructor with a JSONRPCMessage (success or error).
   *
   * @param response the underlying response
   */
  private SendMessageResponse(JSONRPCMessage response) {
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
   * Creates a success response with a Task result.
   *
   * @param task the task result
   * @return a SendMessageResponse containing the task
   */
  public static SendMessageResponse success(Task task) {
    return new SendMessageResponse(SendMessageSuccessResponse.ofTask(task));
  }

  /**
   * Creates a success response with a Message result.
   *
   * @param message the message result
   * @return a SendMessageResponse containing the message
   */
  public static SendMessageResponse success(Message message) {
    return new SendMessageResponse(SendMessageSuccessResponse.ofMessage(message));
  }

  /**
   * Creates a success response with a Task result and ID.
   *
   * @param task the task result
   * @param id the response ID
   * @return a SendMessageResponse containing the task
   */
  public static SendMessageResponse success(Task task, String id) {
    return new SendMessageResponse(SendMessageSuccessResponse.ofTask(task, id));
  }

  /**
   * Creates a success response with a Message result and ID.
   *
   * @param message the message result
   * @param id the response ID
   * @return a SendMessageResponse containing the message
   */
  public static SendMessageResponse success(Message message, String id) {
    return new SendMessageResponse(SendMessageSuccessResponse.ofMessage(message, id));
  }

  /**
   * Creates an error response.
   *
   * @param error the error details
   * @return a SendMessageResponse containing the error
   */
  public static SendMessageResponse error(JSONRPCError error) {
    return new SendMessageResponse(JSONRPCErrorResponse.of(error));
  }

  /**
   * Creates an error response with ID.
   *
   * @param error the error details
   * @param id the response ID
   * @return a SendMessageResponse containing the error
   */
  public static SendMessageResponse error(JSONRPCError error, String id) {
    return new SendMessageResponse(JSONRPCErrorResponse.of(error, id));
  }

  /**
   * Checks if this response is a success response.
   *
   * @return true if this is a success response
   */
  @JsonIgnore
  public boolean isSuccess() {
    return response instanceof SendMessageSuccessResponse;
  }

  /**
   * Checks if this response is an error response.
   *
   * @return true if this is an error response
   */
  @JsonIgnore
  public boolean isError() {
    return response instanceof JSONRPCErrorResponse;
  }

  /**
   * Gets this response as a success response.
   *
   * @return the success response, or null if this is an error response
   */
  @JsonIgnore
  public SendMessageSuccessResponse asSuccess() {
    return isSuccess() ? (SendMessageSuccessResponse) response : null;
  }

  /**
   * Gets this response as an error response.
   *
   * @return the error response, or null if this is a success response
   */
  @JsonIgnore
  public JSONRPCErrorResponse asError() {
    return isError() ? (JSONRPCErrorResponse) response : null;
  }

  @Override
  public String toString() {
    return "SendMessageResponse{" + response + "}";
  }
}
