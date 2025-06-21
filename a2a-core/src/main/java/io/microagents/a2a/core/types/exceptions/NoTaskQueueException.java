package io.microagents.a2a.core.types.exceptions;

/**
 * Exception raised when attempting to access or close a queue for a task ID that does not exist.
 *
 * <p>This exception is thrown by QueueManager implementations when trying to access, retrieve, or
 * close an EventQueue for a task ID that has no associated queue.
 */
public class NoTaskQueueException extends A2AServerException {

  /**
   * Creates a new NoTaskQueueException with a default message.
   *
   * @param taskId the task ID that has no associated queue
   */
  public NoTaskQueueException(String taskId) {
    super("No queue exists for task ID: " + taskId);
  }

  /**
   * Creates a new NoTaskQueueException with a custom message and cause.
   *
   * @param message the exception message
   * @param cause the underlying cause
   */
  public NoTaskQueueException(String message, Throwable cause) {
    super(message, cause);
  }
}
