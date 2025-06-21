package io.microagents.a2a.core.server;

import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Mono;

/**
 * Interface for managing the event queue lifecycles per task.
 *
 * <p>The QueueManager is responsible for creating, retrieving, and managing EventQueue instances
 * associated with specific task IDs. It provides queue lifecycle management including creation,
 * tapping (child queue creation), and cleanup operations.
 *
 * <p>Implementations of this interface can provide different storage mechanisms such as in-memory
 * storage for single-instance deployments or distributed storage for scalable multi-instance
 * deployments.
 */
public interface QueueManager {

  /**
   * Adds a new event queue associated with a task ID.
   *
   * <p>Creates a new queue entry for the specified task ID. This operation will fail if a queue
   * already exists for the given task ID.
   *
   * @param taskId the unique identifier for the task
   * @param queue the EventQueue instance to associate with the task ID
   * @return a Mono that completes when the queue has been added
   * @throws TaskQueueExistsException if a queue for the given task ID already exists
   */
  Mono<Void> add(@NotNull String taskId, @NotNull EventQueue queue);

  /**
   * Retrieves the event queue for a task ID.
   *
   * <p>Returns the primary EventQueue instance associated with the specified task ID, or null if no
   * queue exists for that task ID.
   *
   * @param taskId the unique identifier for the task
   * @return a Mono containing the EventQueue instance for the task ID, or null if not found
   */
  Mono<EventQueue> get(@NotNull String taskId);

  /**
   * Creates a child event queue (tap) for an existing task ID.
   *
   * <p>Creates a new child queue that receives the same events as the parent queue. This is useful
   * for multiple consumers that need to independently process the same event stream.
   *
   * @param taskId the unique identifier for the task
   * @return a Mono containing a new child EventQueue instance, or null if the task ID is not found
   */
  Mono<EventQueue> tap(@NotNull String taskId);

  /**
   * Closes and removes the event queue for a task ID.
   *
   * <p>Closes the EventQueue instance and removes it from management. This operation will fail if
   * no queue exists for the given task ID.
   *
   * @param taskId the unique identifier for the task
   * @return a Mono that completes when the queue has been closed and removed
   * @throws NoTaskQueueException if no queue exists for the given task ID
   */
  Mono<Void> close(@NotNull String taskId);

  /**
   * Creates a queue if one doesn't exist, otherwise taps the existing one.
   *
   * <p>This is a convenience method that combines the logic of checking for an existing queue and
   * either creating a new one or tapping an existing one. This is the most commonly used method for
   * obtaining a queue reference.
   *
   * @param taskId the unique identifier for the task
   * @return a Mono containing a new or child EventQueue instance for the task ID
   */
  Mono<EventQueue> createOrTap(@NotNull String taskId);
}
