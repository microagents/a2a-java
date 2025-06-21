package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * JSON-RPC response for the 'tasks/pushNotificationConfig/set' method.
 *
 * <p>This is a wrapper type that can contain either a success response or an error response. This
 * follows the JSON-RPC 2.0 specification where a response contains either a "result" field
 * (success) or an "error" field (error), but never both.
 *
 * <p>Based on the Python implementation, this acts as a wrapper that can contain:
 *
 * <ul>
 *   <li>{@link JSONRPCErrorResponse} - when an error occurs
 *   <li>{@link SetTaskPushNotificationConfigSuccessResponse} - when the operation succeeds
 * </ul>
 */
@Schema(description = "JSON-RPC response for the 'tasks/pushNotificationConfig/set' method")
public class SetTaskPushNotificationConfigResponse {

  @JsonUnwrapped private final JSONRPCMessage response;

  /**
   * Constructor with a JSONRPCMessage (success or error).
   *
   * @param response the underlying response
   */
  private SetTaskPushNotificationConfigResponse(JSONRPCMessage response) {
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
    return response instanceof SetTaskPushNotificationConfigSuccessResponse;
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
  public SetTaskPushNotificationConfigSuccessResponse asSuccessResponse() {
    return isSuccess() ? (SetTaskPushNotificationConfigSuccessResponse) response : null;
  }

  /**
   * Gets the push notification configuration result if this is a success response.
   *
   * @return the push notification configuration result, or null if this is an error response
   */
  @JsonIgnore
  public TaskPushNotificationConfig getTaskPushNotificationConfig() {
    SetTaskPushNotificationConfigSuccessResponse successResponse = asSuccessResponse();
    return successResponse != null ? successResponse.getResult() : null;
  }

  /**
   * Creates a SetTaskPushNotificationConfigResponse from an error response.
   *
   * @param errorResponse the error response
   * @return a SetTaskPushNotificationConfigResponse containing the error
   */
  public static SetTaskPushNotificationConfigResponse fromError(
      JSONRPCErrorResponse errorResponse) {
    return new SetTaskPushNotificationConfigResponse(errorResponse);
  }

  /**
   * Creates a SetTaskPushNotificationConfigResponse from a success response.
   *
   * @param successResponse the success response
   * @return a SetTaskPushNotificationConfigResponse containing the success result
   */
  public static SetTaskPushNotificationConfigResponse fromSuccess(
      SetTaskPushNotificationConfigSuccessResponse successResponse) {
    return new SetTaskPushNotificationConfigResponse(successResponse);
  }

  /**
   * Creates an error response.
   *
   * @param error the error details
   * @return a SetTaskPushNotificationConfigResponse containing the error
   */
  public static SetTaskPushNotificationConfigResponse error(JSONRPCError error) {
    return new SetTaskPushNotificationConfigResponse(JSONRPCErrorResponse.of(error));
  }

  /**
   * Creates an error response with ID.
   *
   * @param error the error details
   * @param id the response ID
   * @return a SetTaskPushNotificationConfigResponse containing the error
   */
  public static SetTaskPushNotificationConfigResponse error(JSONRPCError error, String id) {
    return new SetTaskPushNotificationConfigResponse(JSONRPCErrorResponse.of(error, id));
  }

  /**
   * Creates a SetTaskPushNotificationConfigResponse from a TaskPushNotificationConfig result.
   *
   * @param config the push notification configuration result
   * @return a SetTaskPushNotificationConfigResponse containing the configuration
   */
  public static SetTaskPushNotificationConfigResponse success(TaskPushNotificationConfig config) {
    return new SetTaskPushNotificationConfigResponse(
        SetTaskPushNotificationConfigSuccessResponse.of(config));
  }

  /**
   * Creates a SetTaskPushNotificationConfigResponse from a TaskPushNotificationConfig result with
   * ID.
   *
   * @param config the push notification configuration result
   * @param id the response ID
   * @return a SetTaskPushNotificationConfigResponse containing the configuration
   */
  public static SetTaskPushNotificationConfigResponse success(
      TaskPushNotificationConfig config, String id) {
    return new SetTaskPushNotificationConfigResponse(
        SetTaskPushNotificationConfigSuccessResponse.of(config, id));
  }

  @Override
  public String toString() {
    return "SetTaskPushNotificationConfigResponse{" + response + "}";
  }
}
