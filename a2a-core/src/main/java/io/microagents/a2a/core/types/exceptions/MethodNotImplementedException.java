package io.microagents.a2a.core.types.exceptions;

/**
 * Exception raised for methods that are not implemented by the server handler.
 *
 * <p>This exception is thrown when a client requests an A2A method that the server recognizes but
 * has not implemented in its request handler. This is different from a "method not found" error -
 * the method exists in the A2A specification but the server doesn't provide an implementation.
 *
 * @since 0.1.0
 */
public class MethodNotImplementedException extends A2AServerException {

  /** Constructs a new method not implemented exception with a default message. */
  public MethodNotImplementedException() {
    this("This method is not implemented by the server");
  }

  /**
   * Constructs a new method not implemented exception with the specified message.
   *
   * @param message a descriptive error message
   */
  public MethodNotImplementedException(String message) {
    super(String.format("Not Implemented operation Error: %s", message));
  }

  /**
   * Constructs a new method not implemented exception for a specific method name.
   *
   * @param methodName the name of the unimplemented method
   * @param message additional descriptive error message
   */
  public MethodNotImplementedException(String methodName, String message) {
    super(String.format("Method '%s' not implemented: %s", methodName, message));
  }
}
