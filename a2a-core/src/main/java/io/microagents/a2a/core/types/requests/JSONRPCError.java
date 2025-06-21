package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Represents a JSON-RPC 2.0 Error object.
 *
 * <p>This is typically included in a JSONRPCErrorResponse when an error occurs. The error object
 * contains:
 *
 * <ul>
 *   <li>code - A Number that indicates the error type
 *   <li>message - A String providing a short description of the error
 *   <li>data - Optional additional information about the error
 * </ul>
 *
 * @see <a href="https://www.jsonrpc.org/specification">JSON-RPC 2.0 Specification</a>
 */
@Schema(description = "Represents a JSON-RPC 2.0 Error object")
public class JSONRPCError {

  /**
   * A Number that indicates the error type that occurred.
   *
   * <p>Standard JSON-RPC 2.0 error codes:
   *
   * <ul>
   *   <li>-32700: Parse error
   *   <li>-32600: Invalid Request
   *   <li>-32601: Method not found
   *   <li>-32602: Invalid params
   *   <li>-32603: Internal error
   *   <li>-32099 to -32000: Server error (reserved for implementation-defined errors)
   * </ul>
   */
  @JsonProperty("code")
  @NotNull
  @Schema(
      description = "A Number that indicates the error type that occurred",
      example = "-32602",
      required = true)
  private Integer code;

  /** A String providing a short description of the error. */
  @JsonProperty("message")
  @NotNull
  @Schema(
      description = "A String providing a short description of the error",
      example = "Invalid parameters",
      required = true)
  private String message;

  /**
   * A Primitive or Structured value that contains additional information about the error. This may
   * be omitted.
   */
  @JsonProperty("data")
  @Schema(
      description =
          "A Primitive or Structured value that contains additional information about the error")
  private Object data;

  /** Default constructor. */
  public JSONRPCError() {}

  /**
   * Constructor with code and message.
   *
   * @param code the error code
   * @param message the error message
   */
  public JSONRPCError(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  /**
   * Full constructor.
   *
   * @param code the error code
   * @param message the error message
   * @param data additional error data
   */
  public JSONRPCError(Integer code, String message, Object data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  /**
   * Gets the error code.
   *
   * @return the error code
   */
  public Integer getCode() {
    return code;
  }

  /**
   * Sets the error code.
   *
   * @param code the error code
   */
  public void setCode(Integer code) {
    this.code = code;
  }

  /**
   * Gets the error message.
   *
   * @return the error message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the error message.
   *
   * @param message the error message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Gets the additional error data.
   *
   * @return the error data, may be null
   */
  public Object getData() {
    return data;
  }

  /**
   * Sets the additional error data.
   *
   * @param data the error data
   */
  public void setData(Object data) {
    this.data = data;
  }

  // Standard JSON-RPC 2.0 error codes
  public static final int PARSE_ERROR = -32700;
  public static final int INVALID_REQUEST = -32600;
  public static final int METHOD_NOT_FOUND = -32601;
  public static final int INVALID_PARAMS = -32602;
  public static final int INTERNAL_ERROR = -32603;

  // A2A-specific error codes (from Python implementation)
  public static final int TASK_NOT_FOUND = -32001;
  public static final int TASK_NOT_CANCELABLE = -32002;
  public static final int PUSH_NOTIFICATION_NOT_SUPPORTED = -32003;
  public static final int UNSUPPORTED_OPERATION = -32004;
  public static final int CONTENT_TYPE_NOT_SUPPORTED = -32005;
  public static final int INVALID_AGENT_RESPONSE = -32006;

  /**
   * Creates a parse error.
   *
   * @return a JSONRPCError for parse errors
   */
  public static JSONRPCError parseError() {
    return new JSONRPCError(PARSE_ERROR, "Invalid JSON payload");
  }

  /**
   * Creates an invalid request error.
   *
   * @return a JSONRPCError for invalid requests
   */
  public static JSONRPCError invalidRequest() {
    return new JSONRPCError(INVALID_REQUEST, "Request payload validation error");
  }

  /**
   * Creates a method not found error.
   *
   * @return a JSONRPCError for method not found
   */
  public static JSONRPCError methodNotFound() {
    return new JSONRPCError(METHOD_NOT_FOUND, "Method not found");
  }

  /**
   * Creates an invalid parameters error.
   *
   * @return a JSONRPCError for invalid parameters
   */
  public static JSONRPCError invalidParams() {
    return new JSONRPCError(INVALID_PARAMS, "Invalid parameters");
  }

  /**
   * Creates an internal error.
   *
   * @return a JSONRPCError for internal errors
   */
  public static JSONRPCError internalError() {
    return new JSONRPCError(INTERNAL_ERROR, "Internal error");
  }

  /**
   * Creates a task not found error.
   *
   * @return a JSONRPCError for task not found
   */
  public static JSONRPCError taskNotFound() {
    return new JSONRPCError(TASK_NOT_FOUND, "Task not found");
  }

  /**
   * Creates a task not cancelable error.
   *
   * @return a JSONRPCError for task not cancelable
   */
  public static JSONRPCError taskNotCancelable() {
    return new JSONRPCError(TASK_NOT_CANCELABLE, "Task cannot be canceled");
  }

  /**
   * Creates a push notification not supported error.
   *
   * @return a JSONRPCError for push notification not supported
   */
  public static JSONRPCError pushNotificationNotSupported() {
    return new JSONRPCError(PUSH_NOTIFICATION_NOT_SUPPORTED, "Push Notification is not supported");
  }

  /**
   * Creates an unsupported operation error.
   *
   * @return a JSONRPCError for unsupported operations
   */
  public static JSONRPCError unsupportedOperation() {
    return new JSONRPCError(UNSUPPORTED_OPERATION, "This operation is not supported");
  }

  /**
   * Creates a content type not supported error.
   *
   * @return a JSONRPCError for content type not supported
   */
  public static JSONRPCError contentTypeNotSupported() {
    return new JSONRPCError(CONTENT_TYPE_NOT_SUPPORTED, "Incompatible content types");
  }

  /**
   * Creates an invalid agent response error.
   *
   * @return a JSONRPCError for invalid agent response
   */
  public static JSONRPCError invalidAgentResponse() {
    return new JSONRPCError(INVALID_AGENT_RESPONSE, "Invalid agent response");
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    JSONRPCError that = (JSONRPCError) obj;
    return Objects.equals(code, that.code)
        && Objects.equals(message, that.message)
        && Objects.equals(data, that.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, message, data);
  }

  @Override
  public String toString() {
    return "JSONRPCError{"
        + "code="
        + code
        + ", message='"
        + message
        + '\''
        + ", data="
        + data
        + '}';
  }
}
