package io.microagents.a2a.core.types.exceptions;

/**
 * Client exception for JSON errors during response parsing or validation.
 *
 * <p>This exception is thrown when an A2A client encounters errors while parsing JSON responses or
 * validating them against the expected schema. Common causes include malformed JSON, missing
 * required fields, or type mismatches.
 *
 * @since 0.1.0
 */
public class A2AClientJSONException extends A2AClientException {

  /**
   * Constructs a new A2A client JSON exception with the specified message.
   *
   * @param message a descriptive error message
   */
  public A2AClientJSONException(String message) {
    super(String.format("JSON Error: %s", message));
  }

  /**
   * Constructs a new A2A client JSON exception with the specified message and cause.
   *
   * @param message a descriptive error message
   * @param cause the underlying cause of this exception (e.g., JsonProcessingException)
   */
  public A2AClientJSONException(String message, Throwable cause) {
    super(String.format("JSON Error: %s", message), cause);
  }
}
