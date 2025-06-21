package io.microagents.a2a.core.types.exceptions;

import io.microagents.a2a.core.types.requests.JSONRPCError;

/**
 * Wrapper exception for A2A or JSON-RPC errors originating from the server's logic.
 *
 * <p>This exception is used internally by request handlers and other server components to signal a
 * specific error that should be formatted as a JSON-RPC error response. It carries the structured
 * error information needed to generate a proper A2A response.
 *
 * @since 0.1.0
 */
public class A2AProtocolException extends A2AServerException {

  private final JSONRPCError jsonRpcError;

  /**
   * Constructs a new A2A protocol exception with the specified JSON-RPC error.
   *
   * @param jsonRpcError the specific A2A or JSON-RPC error model instance
   */
  public A2AProtocolException(JSONRPCError jsonRpcError) {
    super(
        jsonRpcError != null
            ? String.format(
                "A2A Protocol Error [%d]: %s", jsonRpcError.getCode(), jsonRpcError.getMessage())
            : "A2A Protocol Error: Internal Error");
    this.jsonRpcError = jsonRpcError;
  }

  /**
   * Constructs a new A2A protocol exception with the specified JSON-RPC error and cause.
   *
   * @param jsonRpcError the specific A2A or JSON-RPC error model instance
   * @param cause the underlying cause of this exception
   */
  public A2AProtocolException(JSONRPCError jsonRpcError, Throwable cause) {
    super(
        jsonRpcError != null
            ? String.format(
                "A2A Protocol Error [%d]: %s", jsonRpcError.getCode(), jsonRpcError.getMessage())
            : "A2A Protocol Error: Internal Error",
        cause);
    this.jsonRpcError = jsonRpcError;
  }

  /**
   * Returns the JSON-RPC error associated with this exception.
   *
   * <p>This error object contains the structured error information that should be included in the
   * JSON-RPC error response.
   *
   * @return the JSON-RPC error, or null if an internal error should be used
   */
  public JSONRPCError getJsonRpcError() {
    return jsonRpcError;
  }

  /**
   * Checks if this exception has a JSON-RPC error attached.
   *
   * @return true if a JSON-RPC error is present, false otherwise
   */
  public boolean hasJsonRpcError() {
    return jsonRpcError != null;
  }
}
