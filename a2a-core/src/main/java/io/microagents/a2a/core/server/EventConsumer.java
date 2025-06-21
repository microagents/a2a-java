package io.microagents.a2a.core.server;

import io.microagents.a2a.core.types.core.Event;
import io.microagents.a2a.core.types.core.Message;
import io.microagents.a2a.core.types.core.Task;
import io.microagents.a2a.core.types.core.TaskState;
import io.microagents.a2a.core.types.core.TaskStatusUpdateEvent;
import io.microagents.a2a.core.types.exceptions.A2AServerException;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Consumer to read events from the agent event queue.
 *
 * <p>The EventConsumer provides methods to consume events from an {@link EventQueue} either one at
 * a time or as a continuous stream. It handles queue lifecycle management and provides error
 * handling for agent execution failures.
 *
 * <p>The consumer supports both blocking and non-blocking consumption patterns:
 *
 * <ul>
 *   <li>{@link #consumeOne()} - Consumes a single event immediately
 *   <li>{@link #consumeAll()} - Returns a Flux of all events until completion
 * </ul>
 *
 * @since 0.1.0
 */
public class EventConsumer {
  private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

  /** Default timeout for polling events from the queue. */
  private static final Duration DEFAULT_TIMEOUT = Duration.ofMillis(500);

  /** Task states that indicate the task has reached a final state. */
  private static final Set<TaskState> FINAL_TASK_STATES =
      Set.of(
          TaskState.COMPLETED,
          TaskState.CANCELED,
          TaskState.FAILED,
          TaskState.REJECTED,
          TaskState.UNKNOWN);

  private final EventQueue queue;
  private final Duration timeout;
  private final AtomicReference<Throwable> exception = new AtomicReference<>();

  /**
   * Creates a new EventConsumer for the given queue.
   *
   * @param queue the EventQueue instance to consume events from
   */
  public EventConsumer(EventQueue queue) {
    this(queue, DEFAULT_TIMEOUT);
  }

  /**
   * Creates a new EventConsumer with a custom timeout.
   *
   * @param queue the EventQueue instance to consume events from
   * @param timeout the timeout for polling events
   */
  public EventConsumer(EventQueue queue, Duration timeout) {
    this.queue = queue;
    this.timeout = timeout;
    logger.debug("EventConsumer initialized with timeout: {}", timeout);
  }

  /**
   * Consumes one event from the agent event queue immediately.
   *
   * <p>This method attempts to get the next available event without waiting. If no events are
   * available, it throws an exception.
   *
   * @return a Mono containing the next event from the queue
   * @throws A2AServerException if the queue is empty or closed
   */
  public Mono<Event> consumeOne() {
    logger.debug("Attempting to consume one event");

    return queue
        .asFlux()
        .take(1)
        .single()
        .doOnNext(
            event -> logger.debug("Consumed event of type: {}", event.getClass().getSimpleName()))
        .onErrorMap(
            throwable -> {
              logger.warn("Event queue was empty in consumeOne", throwable);
              return new A2AServerException(
                  "Agent did not return any response",
                  -32603, // Internal error
                  null);
            });
  }

  /**
   * Consumes all the generated streaming events from the agent.
   *
   * <p>This method returns a Flux that yields events as they become available from the queue until
   * a final event is received or the queue is closed. It also monitors for exceptions set by agent
   * execution callbacks.
   *
   * <p>Final events are determined by:
   *
   * <ul>
   *   <li>TaskStatusUpdateEvent with final flag set
   *   <li>Message events (treated as final responses)
   *   <li>Task events with final states (completed, failed, etc.)
   * </ul>
   *
   * @return a Flux of events from the queue until completion
   */
  public Flux<Event> consumeAll() {
    logger.debug("Starting to consume all events from the queue");

    return queue
        .asFlux()
        .doOnNext(
            event -> logger.debug("Consumed event of type: {}", event.getClass().getSimpleName()))
        .takeUntil(this::isFinalEvent)
        .concatWith(
            Mono.defer(
                () -> {
                  // Check for any exceptions from agent execution
                  Throwable ex = exception.get();
                  if (ex != null) {
                    logger.error("Agent execution failed", ex);
                    return Mono.error(ex);
                  }
                  return Mono.empty();
                }))
        .doOnComplete(
            () -> {
              logger.debug("Completed consuming all events");
              queue
                  .close()
                  .subscribe(
                      null, // onNext not needed for close
                      error -> logger.warn("Error closing queue", error),
                      () -> logger.debug("Queue closed successfully"));
            })
        .doOnError(error -> logger.error("Error consuming events", error));
  }

  /**
   * Sets an exception that occurred during agent execution.
   *
   * <p>This exception will be thrown by {@link #consumeAll()} when no more events are available,
   * allowing the consumer to propagate agent failures to the client.
   *
   * @param throwable the exception that occurred during agent execution
   */
  public void setException(Throwable throwable) {
    logger.debug("Setting exception for EventConsumer", throwable);
    exception.set(throwable);
  }

  /** Clears any previously set exception. */
  public void clearException() {
    logger.debug("Clearing exception for EventConsumer");
    exception.set(null);
  }

  /**
   * Gets the current exception, if any.
   *
   * @return the current exception, or null if none is set
   */
  public Throwable getException() {
    return exception.get();
  }

  /**
   * Creates a callback that can be used to handle agent execution failures.
   *
   * <p>This method returns a {@link Runnable} that can be used as a callback for agent execution
   * tasks. When called, it will set the exception for this consumer.
   *
   * @param throwable the exception that occurred
   * @return a Runnable that sets the exception
   */
  public Runnable createExceptionCallback(Throwable throwable) {
    return () -> setException(throwable);
  }

  /**
   * Determines if an event represents the final event in a stream.
   *
   * <p>Final events include:
   *
   * <ul>
   *   <li>TaskStatusUpdateEvent with final flag set to true
   *   <li>Message events (always considered final)
   *   <li>Task events with terminal states
   * </ul>
   *
   * @param event the event to check
   * @return true if this is a final event, false otherwise
   */
  private boolean isFinalEvent(Event event) {
    boolean isFinal = false;

    if (event instanceof TaskStatusUpdateEvent statusEvent) {
      isFinal = statusEvent.isFinal();
    } else if (event instanceof Message) {
      isFinal = true; // Messages are always considered final
    } else if (event instanceof Task task) {
      isFinal = FINAL_TASK_STATES.contains(task.getStatus().getState());
    }

    if (isFinal) {
      logger.debug("Final event detected: {}", event.getClass().getSimpleName());
    }

    return isFinal;
  }
}
