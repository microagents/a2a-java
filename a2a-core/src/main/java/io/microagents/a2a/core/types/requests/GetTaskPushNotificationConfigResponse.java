package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * JSON-RPC response for the 'tasks/pushNotificationConfig/get' method.
 *
 * <p>This is a wrapper type that can contain either a success response or an error response. This
 * follows the JSON-RPC 2.0 specification where a response contains either a "result" field
 * (success) or an "error" field (error), but never both.
 *
 * <p>Based on the Python implementation, this acts as a wrapper that can contain:
 *
 * <ul>
 *   <li>{@link JSONRPCErrorResponse} - when an error occurs
 *   <li>{@link GetTaskPushNotificationConfigSuccessResponse} - when the operation succeeds
 * </ul>
 */
@Schema(description = "JSON-RPC response for the 'tasks/pushNotificationConfig/get' method")
public class GetTaskPushNotificationConfigResponse {

  @JsonUnwrapped private final JSONRPCMessage response;

  /**
   * Constructor with a JSONRPCMessage (success or error).
   *
   * @param response the underlying response
   */
  private GetTaskPushNotificationConfigResponse(JSONRPCMessage response) {
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
    return response instanceof GetTaskPushNotificationConfigSuccessResponse;
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
  public GetTaskPushNotificationConfigSuccessResponse asSuccessResponse() {
    return isSuccess() ? (GetTaskPushNotificationConfigSuccessResponse) response : null;
  }

  /**
   * Gets the push notification configuration result if this is a success response.
   *
   * @return the push notification configuration result, or null if this is an error response
   */
  @JsonIgnore
  public TaskPushNotificationConfig getTaskPushNotificationConfig() {
    GetTaskPushNotificationConfigSuccessResponse successResponse = asSuccessResponse();
    return successResponse != null ? successResponse.getResult() : null;
  }

  /**
   * Creates a GetTaskPushNotificationConfigResponse from an error response.
   *
   * @param errorResponse the error response
   * @return a GetTaskPushNotificationConfigResponse containing the error
   */
  public static GetTaskPushNotificationConfigResponse fromError(
      JSONRPCErrorResponse errorResponse) {
    return new GetTaskPushNotificationConfigResponse(errorResponse);
  }

  /**
   * Creates a GetTaskPushNotificationConfigResponse from a success response.
   *
   * @param successResponse the success response
   * @return a GetTaskPushNotificationConfigResponse containing the success result
   */
  public static GetTaskPushNotificationConfigResponse fromSuccess(
      GetTaskPushNotificationConfigSuccessResponse successResponse) {
    return new GetTaskPushNotificationConfigResponse(successResponse);
  }

  /**
   * Creates an error response.
   *
   * @param error the error details
   * @return a GetTaskPushNotificationConfigResponse containing the error
   */
  public static GetTaskPushNotificationConfigResponse error(JSONRPCError error) {
    return new GetTaskPushNotificationConfigResponse(JSONRPCErrorResponse.of(error));
  }

  /**
   * Creates an error response with ID.
   *
   * @param error the error details
   * @param id the response ID
   * @return a GetTaskPushNotificationConfigResponse containing the error
   */
  public static GetTaskPushNotificationConfigResponse error(JSONRPCError error, String id) {
    return new GetTaskPushNotificationConfigResponse(JSONRPCErrorResponse.of(error, id));
  }

  /**
   * Creates a GetTaskPushNotificationConfigResponse from a TaskPushNotificationConfig result.
   *
   * @param config the push notification configuration result
   * @return a GetTaskPushNotificationConfigResponse containing the configuration
   */
  public static GetTaskPushNotificationConfigResponse success(TaskPushNotificationConfig config) {
    return new GetTaskPushNotificationConfigResponse(
        GetTaskPushNotificationConfigSuccessResponse.of(config));
  }

  /**
   * Creates a GetTaskPushNotificationConfigResponse from a TaskPushNotificationConfig result with
   * ID.
   *
   * @param config the push notification configuration result
   * @param id the response ID
   * @return a GetTaskPushNotificationConfigResponse containing the configuration
   */
  public static GetTaskPushNotificationConfigResponse success(
      TaskPushNotificationConfig config, String id) {
    return new GetTaskPushNotificationConfigResponse(
        GetTaskPushNotificationConfigSuccessResponse.of(config, id));
  }

  @Override
  public String toString() {
    return "GetTaskPushNotificationConfigResponse{" + response + "}";
  }
}
