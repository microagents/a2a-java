package io.microagents.a2a.core.types.exceptions;

/**
 * Client exception for HTTP errors received from A2A agents.
 *
 * <p>This exception is thrown when an A2A client receives an HTTP error response from an agent
 * server, including status codes like 4xx and 5xx.
 *
 * @since 0.1.0
 */
public class A2AClientHTTPException extends A2AClientException {

  private final int statusCode;

  /**
   * Constructs a new A2A client HTTP exception with the specified status code and message.
   *
   * @param statusCode the HTTP status code of the response
   * @param message a descriptive error message
   */
  public A2AClientHTTPException(int statusCode, String message) {
    super(String.format("HTTP Error %d: %s", statusCode, message));
    this.statusCode = statusCode;
  }

  /**
   * Constructs a new A2A client HTTP exception with the specified status code, message, and cause.
   *
   * @param statusCode the HTTP status code of the response
   * @param message a descriptive error message
   * @param cause the underlying cause of this exception
   */
  public A2AClientHTTPException(int statusCode, String message, Throwable cause) {
    super(String.format("HTTP Error %d: %s", statusCode, message), cause);
    this.statusCode = statusCode;
  }

  /**
   * Returns the HTTP status code that caused this exception.
   *
   * @return the HTTP status code
   */
  public int getStatusCode() {
    return statusCode;
  }

  /**
   * Checks if this is a client error (4xx status code).
   *
   * @return true if the status code is in the 4xx range
   */
  public boolean isClientError() {
    return statusCode >= 400 && statusCode < 500;
  }

  /**
   * Checks if this is a server error (5xx status code).
   *
   * @return true if the status code is in the 5xx range
   */
  public boolean isServerError() {
    return statusCode >= 500 && statusCode < 600;
  }
}
