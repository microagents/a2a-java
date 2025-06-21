package io.microagents.a2a.core.types.exceptions;

/**
 * Base exception for all A2A-related errors.
 *
 * <p>This is the root exception class for all Agent2Agent protocol errors, providing a common
 * hierarchy for both client-side and server-side exceptions.
 *
 * @since 0.1.0
 */
public class A2AException extends RuntimeException {

  /**
   * Constructs a new A2A exception with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the getMessage()
   *     method)
   */
  public A2AException(String message) {
    super(message);
  }

  /**
   * Constructs a new A2A exception with the specified detail message and cause.
   *
   * @param message the detail message (which is saved for later retrieval by the getMessage()
   *     method)
   * @param cause the cause (which is saved for later retrieval by the getCause() method)
   */
  public A2AException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new A2A exception with the specified cause and a detail message of (cause==null ?
   * null : cause.toString()).
   *
   * @param cause the cause (which is saved for later retrieval by the getCause() method)
   */
  public A2AException(Throwable cause) {
    super(cause);
  }
}
