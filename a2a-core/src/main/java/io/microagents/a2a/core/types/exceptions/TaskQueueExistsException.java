package io.microagents.a2a.core.types.exceptions;

/**
 * Exception raised when attempting to add a queue for a task ID that already exists.
 *
 * <p>This exception is thrown by QueueManager implementations when trying to add a new EventQueue
 * for a task ID that already has an associated queue.
 */
public class TaskQueueExistsException extends A2AServerException {

  /**
   * Creates a new TaskQueueExistsException with a default message.
   *
   * @param taskId the task ID that already has an associated queue
   */
  public TaskQueueExistsException(String taskId) {
    super("Queue already exists for task ID: " + taskId);
  }

  /**
   * Creates a new TaskQueueExistsException with a custom message and cause.
   *
   * @param message the exception message
   * @param cause the underlying cause
   */
  public TaskQueueExistsException(String message, Throwable cause) {
    super(message, cause);
  }
}
