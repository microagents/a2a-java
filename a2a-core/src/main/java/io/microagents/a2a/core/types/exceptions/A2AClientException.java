package io.microagents.a2a.core.types.exceptions;

/**
 * Base exception for all A2A client-side errors.
 *
 * <p>This exception class represents errors that occur on the client side when communicating with
 * A2A agents, such as network issues, HTTP errors, or JSON parsing failures.
 *
 * @since 0.1.0
 */
public class A2AClientException extends A2AException {

  /**
   * Constructs a new A2A client exception with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the getMessage()
   *     method)
   */
  public A2AClientException(String message) {
    super(message);
  }

  /**
   * Constructs a new A2A client exception with the specified detail message and cause.
   *
   * @param message the detail message (which is saved for later retrieval by the getMessage()
   *     method)
   * @param cause the cause (which is saved for later retrieval by the getCause() method)
   */
  public A2AClientException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new A2A client exception with the specified cause and a detail message of
   * (cause==null ? null : cause.toString()).
   *
   * @param cause the cause (which is saved for later retrieval by the getCause() method)
   */
  public A2AClientException(Throwable cause) {
    super(cause);
  }
}
