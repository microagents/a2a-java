package io.microagents.a2a.core.types.exceptions;

/**
 * Base exception for all A2A server-side errors.
 *
 * <p>This exception class represents errors that occur on the server side when processing A2A
 * requests, such as validation failures, method not found, internal errors, or business logic
 * violations. It includes JSON-RPC 2.0 compatible error code and data fields.
 *
 * @since 0.1.0
 */
public class A2AServerException extends A2AException {

  private final int errorCode;
  private final Object data;

  /**
   * Constructs a new A2A server exception with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the getMessage()
   *     method)
   */
  public A2AServerException(String message) {
    super(message);
    this.errorCode = -32603; // Internal error
    this.data = null;
  }

  /**
   * Constructs a new A2A server exception with the specified detail message and cause.
   *
   * @param message the detail message (which is saved for later retrieval by the getMessage()
   *     method)
   * @param cause the cause (which is saved for later retrieval by the getCause() method)
   */
  public A2AServerException(String message, Throwable cause) {
    super(message, cause);
    this.errorCode = -32603; // Internal error
    this.data = null;
  }

  /**
   * Constructs a new A2A server exception with the specified cause and a detail message of
   * (cause==null ? null : cause.toString()).
   *
   * @param cause the cause (which is saved for later retrieval by the getCause() method)
   */
  public A2AServerException(Throwable cause) {
    super(cause);
    this.errorCode = -32603; // Internal error
    this.data = null;
  }

  /**
   * Constructs a new A2A server exception with the specified detail message, error code, and data.
   *
   * @param message the detail message
   * @param errorCode the JSON-RPC 2.0 error code
   * @param data additional error data (can be null)
   */
  public A2AServerException(String message, int errorCode, Object data) {
    super(message);
    this.errorCode = errorCode;
    this.data = data;
  }

  /**
   * Constructs a new A2A server exception with the specified detail message, cause, error code, and
   * data.
   *
   * @param message the detail message
   * @param cause the cause
   * @param errorCode the JSON-RPC 2.0 error code
   * @param data additional error data (can be null)
   */
  public A2AServerException(String message, Throwable cause, int errorCode, Object data) {
    super(message, cause);
    this.errorCode = errorCode;
    this.data = data;
  }

  /**
   * Gets the JSON-RPC 2.0 error code.
   *
   * @return the error code
   */
  public int getErrorCode() {
    return errorCode;
  }

  /**
   * Gets the additional error data.
   *
   * @return the error data (may be null)
   */
  public Object getData() {
    return data;
  }
}
