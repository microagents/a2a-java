package io.microagents.a2a.core.server;

import io.microagents.a2a.core.types.core.Task;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * In-memory implementation of TaskStore.
 *
 * <p>Stores task objects in a {@link ConcurrentHashMap} in memory. Task data is lost when the
 * server process stops. This implementation is thread-safe and suitable for development, testing,
 * and single-node deployments.
 *
 * <p>For production use with multiple server instances or persistence requirements, consider
 * implementing a database-backed TaskStore instead.
 *
 * @since 0.1.0
 */
public class InMemoryTaskStore implements TaskStore {
  private static final Logger logger = LoggerFactory.getLogger(InMemoryTaskStore.class);

  private final ConcurrentMap<String, Task> tasks = new ConcurrentHashMap<>();

  /** Creates a new InMemoryTaskStore instance. */
  public InMemoryTaskStore() {
    logger.debug("Initializing InMemoryTaskStore");
  }

  @Override
  public Mono<Void> save(Task task) {
    if (task == null) {
      return Mono.error(new IllegalArgumentException("Task cannot be null"));
    }

    return Mono.fromRunnable(
        () -> {
          tasks.put(task.getId(), task);
          logger.debug("Task {} saved successfully", task.getId());
        });
  }

  @Override
  public Mono<Task> get(String taskId) {
    if (taskId == null || taskId.trim().isEmpty()) {
      return Mono.error(new IllegalArgumentException("Task ID cannot be null or empty"));
    }

    return Mono.fromCallable(
        () -> {
          logger.debug("Attempting to get task with id: {}", taskId);
          Task task = tasks.get(taskId);
          if (task != null) {
            logger.debug("Task {} retrieved successfully", taskId);
          } else {
            logger.debug("Task {} not found in store", taskId);
          }
          return task;
        });
  }

  @Override
  public Mono<Void> delete(String taskId) {
    if (taskId == null || taskId.trim().isEmpty()) {
      return Mono.error(new IllegalArgumentException("Task ID cannot be null or empty"));
    }

    return Mono.fromRunnable(
        () -> {
          logger.debug("Attempting to delete task with id: {}", taskId);
          Task removedTask = tasks.remove(taskId);
          if (removedTask != null) {
            logger.debug("Task {} deleted successfully", taskId);
          } else {
            logger.warn("Attempted to delete nonexistent task with id: {}", taskId);
          }
        });
  }

  @Override
  public Mono<Boolean> exists(String taskId) {
    if (taskId == null || taskId.trim().isEmpty()) {
      return Mono.error(new IllegalArgumentException("Task ID cannot be null or empty"));
    }

    return Mono.fromCallable(
        () -> {
          boolean exists = tasks.containsKey(taskId);
          logger.debug("Task {} exists: {}", taskId, exists);
          return exists;
        });
  }

  /**
   * Returns the number of tasks currently stored.
   *
   * <p>This method is primarily intended for monitoring and debugging purposes.
   *
   * @return the number of tasks in the store
   */
  public int size() {
    return tasks.size();
  }

  /**
   * Clears all tasks from the store.
   *
   * <p>This method is primarily intended for testing purposes. Use with caution in production
   * environments.
   */
  public void clear() {
    int size = tasks.size();
    tasks.clear();
    logger.info("Cleared {} tasks from store", size);
  }
}
