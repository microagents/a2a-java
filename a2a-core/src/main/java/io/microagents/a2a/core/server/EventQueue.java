package io.microagents.a2a.core.server;

import io.microagents.a2a.core.types.core.Event;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

/**
 * Event queue for A2A responses from agents.
 *
 * <p>Acts as a buffer between the agent's asynchronous execution and the server's response handling
 * (e.g., streaming via SSE). Supports tapping to create child queues that receive the same events.
 *
 * <p>This implementation uses Reactor's {@link Sinks} for reactive streaming and backpressure
 * handling. The queue can be configured with a maximum capacity to prevent memory issues with slow
 * consumers.
 *
 * @since 0.1.0
 */
public class EventQueue {
  private static final Logger logger = LoggerFactory.getLogger(EventQueue.class);

  /** Default maximum queue size to prevent unbounded growth. */
  public static final int DEFAULT_MAX_QUEUE_SIZE = 1024;

  private final Sinks.Many<Event> sink;
  private final List<EventQueue> children = new ArrayList<>();
  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private volatile boolean closed = false;

  /** Creates a new EventQueue with the default maximum queue size. */
  public EventQueue() {
    this(DEFAULT_MAX_QUEUE_SIZE);
  }

  /**
   * Creates a new EventQueue with the specified maximum queue size.
   *
   * @param maxQueueSize the maximum number of events that can be queued
   * @throws IllegalArgumentException if maxQueueSize is less than or equal to 0
   */
  public EventQueue(int maxQueueSize) {
    if (maxQueueSize <= 0) {
      throw new IllegalArgumentException("maxQueueSize must be greater than 0");
    }

    // Use multicast sink with replay for backpressure handling
    this.sink = Sinks.many().multicast().onBackpressureBuffer(maxQueueSize);
    logger.debug("EventQueue initialized with max size: {}", maxQueueSize);
  }

  /**
   * Enqueues an event to this queue and all its children.
   *
   * <p>The event will be emitted to all current subscribers. If the queue is closed, the event will
   * be dropped and a warning will be logged.
   *
   * @param event the event object to enqueue
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> enqueueEvent(Event event) {
    return Mono.fromRunnable(
        () -> {
          lock.readLock().lock();
          try {
            if (closed) {
              logger.warn(
                  "Queue is closed. Event will not be enqueued: {}",
                  event.getClass().getSimpleName());
              return;
            }

            logger.debug("Enqueuing event of type: {}", event.getClass().getSimpleName());

            // Emit to main sink
            Sinks.EmitResult result = sink.tryEmitNext(event);
            if (result.isFailure()) {
              logger.warn(
                  "Failed to enqueue event: {} - {}", event.getClass().getSimpleName(), result);
            } else {
              logger.debug("Successfully enqueued event: {}", event.getClass().getSimpleName());
            }

            // Enqueue to all children
            for (EventQueue child : children) {
              child.enqueueEvent(event).subscribe(); // Fire and forget for children
            }

          } finally {
            lock.readLock().unlock();
          }
        });
  }

  /**
   * Creates a Flux to consume events from this queue.
   *
   * <p>The returned Flux will emit all events enqueued to this queue until the queue is closed.
   * Multiple subscribers can consume from the same queue, each receiving all events.
   *
   * @return a Flux of events from this queue
   */
  public Flux<Event> asFlux() {
    return sink.asFlux()
        .doOnSubscribe(subscription -> logger.debug("New subscriber to EventQueue"))
        .doOnNext(
            event -> logger.debug("Emitting event of type: {}", event.getClass().getSimpleName()))
        .doOnComplete(() -> logger.debug("EventQueue flux completed"))
        .doOnError(error -> logger.error("EventQueue flux error", error));
  }

  /**
   * Taps the event queue to create a new child queue that receives all future events.
   *
   * <p>The child queue will receive all events enqueued to this parent queue from this point
   * forward. Child queues are automatically closed when the parent queue is closed.
   *
   * @return a new EventQueue instance that will receive all events
   */
  public EventQueue tap() {
    lock.writeLock().lock();
    try {
      logger.debug("Tapping EventQueue to create a child queue");
      EventQueue childQueue = new EventQueue();
      children.add(childQueue);
      return childQueue;
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * Closes the queue for future events.
   *
   * <p>Once closed, no new events can be enqueued. Existing subscribers will receive a completion
   * signal. All child queues are also closed.
   *
   * @return a Mono that completes when the queue is closed
   */
  public Mono<Void> close() {
    return Mono.fromRunnable(
        () -> {
          lock.writeLock().lock();
          try {
            if (closed) {
              logger.debug("EventQueue already closed");
              return;
            }

            logger.debug("Closing EventQueue");
            closed = true;

            // Complete the sink to signal no more events
            sink.tryEmitComplete();

            // Close all children
            for (EventQueue child : children) {
              child.close().subscribe(); // Fire and forget
            }

            logger.debug("EventQueue closed successfully");

          } finally {
            lock.writeLock().unlock();
          }
        });
  }

  /**
   * Checks if the queue is closed.
   *
   * @return true if the queue is closed, false otherwise
   */
  public boolean isClosed() {
    return closed;
  }

  /**
   * Returns the number of child queues created by tapping.
   *
   * <p>This method is primarily intended for monitoring and debugging purposes.
   *
   * @return the number of child queues
   */
  public int getChildCount() {
    lock.readLock().lock();
    try {
      return children.size();
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Completes the queue exceptionally with the given error.
   *
   * <p>This will cause all current and future subscribers to receive the error signal.
   *
   * @param error the error to emit
   * @return a Mono that completes when the error has been emitted
   */
  public Mono<Void> completeExceptionally(Throwable error) {
    return Mono.fromRunnable(
        () -> {
          lock.writeLock().lock();
          try {
            if (closed) {
              logger.debug("EventQueue already closed, cannot emit error");
              return;
            }

            logger.debug("Completing EventQueue exceptionally", error);
            closed = true;

            // Emit error to sink
            sink.tryEmitError(error);

            // Close all children with same error
            for (EventQueue child : children) {
              child.completeExceptionally(error).subscribe();
            }

          } finally {
            lock.writeLock().unlock();
          }
        });
  }
}
