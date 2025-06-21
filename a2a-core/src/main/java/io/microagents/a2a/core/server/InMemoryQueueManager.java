package io.microagents.a2a.core.server;

import io.microagents.a2a.core.types.exceptions.NoTaskQueueException;
import io.microagents.a2a.core.types.exceptions.TaskQueueExistsException;
import jakarta.validation.constraints.NotNull;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * InMemoryQueueManager is used for single binary management.
 *
 * <p>This implements the QueueManager interface using in-memory storage for event queues. It
 * requires all incoming interactions for a given task ID to hit the same binary instance.
 *
 * <p>This implementation is suitable for single-instance deployments but needs a distributed
 * approach for scalable deployments.
 *
 * <p>Thread-safe operations are guaranteed through the use of ReentrantLock for critical sections
 * involving queue lifecycle management.
 */
public class InMemoryQueueManager implements QueueManager {

  private static final Logger logger = LoggerFactory.getLogger(InMemoryQueueManager.class);

  private final ConcurrentHashMap<String, EventQueue> taskQueues;
  private final ReentrantLock lock;

  /** Creates a new InMemoryQueueManager. */
  public InMemoryQueueManager() {
    this.taskQueues = new ConcurrentHashMap<>();
    this.lock = new ReentrantLock();
  }

  @Override
  public Mono<Void> add(@NotNull String taskId, @NotNull EventQueue queue) {
    return Mono.fromCallable(
            () -> {
              lock.lock();
              try {
                if (taskQueues.containsKey(taskId)) {
                  throw new TaskQueueExistsException(taskId);
                }
                taskQueues.put(taskId, queue);
                logger.debug("Added queue for task ID: {}", taskId);
                return null;
              } finally {
                lock.unlock();
              }
            })
        .then();
  }

  @Override
  public Mono<EventQueue> get(@NotNull String taskId) {
    return Mono.fromCallable(
        () -> {
          lock.lock();
          try {
            EventQueue queue = taskQueues.get(taskId);
            if (queue == null) {
              logger.debug("No queue found for task ID: {}", taskId);
              return null;
            }
            logger.debug("Retrieved queue for task ID: {}", taskId);
            return queue;
          } finally {
            lock.unlock();
          }
        });
  }

  @Override
  public Mono<EventQueue> tap(@NotNull String taskId) {
    return Mono.fromCallable(
        () -> {
          lock.lock();
          try {
            EventQueue parentQueue = taskQueues.get(taskId);
            if (parentQueue == null) {
              logger.debug("No queue found to tap for task ID: {}", taskId);
              return null;
            }
            EventQueue childQueue = parentQueue.tap();
            logger.debug("Created tap queue for task ID: {}", taskId);
            return childQueue;
          } finally {
            lock.unlock();
          }
        });
  }

  @Override
  public Mono<Void> close(@NotNull String taskId) {
    return Mono.fromCallable(
            () -> {
              lock.lock();
              try {
                EventQueue queue = taskQueues.remove(taskId);
                if (queue == null) {
                  throw new NoTaskQueueException(taskId);
                }
                logger.debug("Closed and removed queue for task ID: {}", taskId);
                return queue;
              } finally {
                lock.unlock();
              }
            })
        .flatMap(queue -> queue.close())
        .then();
  }

  @Override
  public Mono<EventQueue> createOrTap(@NotNull String taskId) {
    return Mono.fromCallable(
        () -> {
          lock.lock();
          try {
            EventQueue existingQueue = taskQueues.get(taskId);
            if (existingQueue != null) {
              logger.debug("Tapping existing queue for task ID: {}", taskId);
              return existingQueue.tap();
            } else {
              EventQueue newQueue = new EventQueue();
              taskQueues.put(taskId, newQueue);
              logger.debug("Created new queue for task ID: {}", taskId);
              return newQueue;
            }
          } finally {
            lock.unlock();
          }
        });
  }

  /**
   * Gets the current number of managed queues.
   *
   * <p>This method is useful for monitoring and debugging purposes.
   *
   * @return the number of currently managed task queues
   */
  public int getQueueCount() {
    return taskQueues.size();
  }

  /**
   * Checks if a queue exists for the given task ID.
   *
   * @param taskId the task ID to check
   * @return true if a queue exists for the task ID, false otherwise
   */
  public boolean hasQueue(@NotNull String taskId) {
    return taskQueues.containsKey(taskId);
  }

  /**
   * Removes all queues and closes them.
   *
   * <p>This method is primarily intended for cleanup during shutdown or testing scenarios.
   *
   * @return a Mono that completes when all queues have been closed
   */
  public Mono<Void> closeAll() {
    return Mono.fromCallable(
            () -> {
              lock.lock();
              try {
                var queues = taskQueues.values().toArray(new EventQueue[0]);
                taskQueues.clear();
                logger.debug("Cleared all {} queues", queues.length);
                return queues;
              } finally {
                lock.unlock();
              }
            })
        .flatMap(
            queues ->
                Mono.when(
                    java.util.Arrays.stream(queues).map(EventQueue::close).toArray(Mono[]::new)));
  }
}
