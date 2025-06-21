package io.microagents.a2a.core.server;

import io.microagents.a2a.core.types.core.Task;
import reactor.core.publisher.Mono;

/**
 * Agent Task Store interface.
 *
 * <p>Defines the methods for persisting and retrieving {@link Task} objects. This interface
 * provides a reactive abstraction for task storage operations, allowing for different storage
 * implementations (in-memory, database, etc.).
 *
 * <p>All operations return {@link Mono} to support reactive programming patterns and non-blocking
 * I/O operations.
 *
 * @since 0.1.0
 */
public interface TaskStore {

  /**
   * Saves or updates a task in the store.
   *
   * <p>If a task with the same ID already exists, it will be updated. Otherwise, a new task will be
   * created.
   *
   * @param task the task to save or update, must not be null
   * @return a Mono that completes when the task is saved
   * @throws IllegalArgumentException if the task is null
   */
  Mono<Void> save(Task task);

  /**
   * Retrieves a task from the store by ID.
   *
   * <p>Returns an empty Mono if no task with the given ID exists.
   *
   * @param taskId the unique identifier of the task to retrieve, must not be null or empty
   * @return a Mono containing the task if found, or empty Mono if not found
   * @throws IllegalArgumentException if the taskId is null or empty
   */
  Mono<Task> get(String taskId);

  /**
   * Deletes a task from the store by ID.
   *
   * <p>This operation succeeds even if no task with the given ID exists.
   *
   * @param taskId the unique identifier of the task to delete, must not be null or empty
   * @return a Mono that completes when the task is deleted
   * @throws IllegalArgumentException if the taskId is null or empty
   */
  Mono<Void> delete(String taskId);

  /**
   * Checks if a task with the given ID exists in the store.
   *
   * <p>This method provides a more efficient way to check for task existence without retrieving the
   * full task object.
   *
   * @param taskId the unique identifier of the task to check, must not be null or empty
   * @return a Mono containing true if the task exists, false otherwise
   * @throws IllegalArgumentException if the taskId is null or empty
   */
  Mono<Boolean> exists(String taskId);
}
