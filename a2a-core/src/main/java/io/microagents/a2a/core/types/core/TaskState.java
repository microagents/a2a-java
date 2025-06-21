package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonValue;
import io.microagents.a2a.core.types.notifications.*;

/**
 * Represents the possible lifecycle states of an A2A Task.
 *
 * <p>Tasks progress through various states during their execution lifecycle. Some states are
 * terminal (task will not change state again), while others allow for continued processing or
 * require client intervention.
 *
 * @see Task
 * @since 0.1.0
 */
public enum TaskState {

  /**
   * Task received by the server and acknowledged, but processing has not yet actively started. This
   * is typically the initial state when a task is first created.
   */
  SUBMITTED("submitted"),

  /**
   * Task is actively being processed by the agent. Client may expect further updates or a terminal
   * state.
   */
  WORKING("working"),

  /**
   * Agent requires additional input from the client/user to proceed. The task is effectively paused
   * and waiting for client response. This is an interrupted state that requires client action.
   */
  INPUT_REQUIRED("input-required"),

  /**
   * Task finished successfully. Results are typically available in Task.artifacts or
   * TaskStatus.message. This is a terminal state.
   */
  COMPLETED("completed"),

  /**
   * Task was canceled (e.g., by a tasks/cancel request or server-side policy). This is a terminal
   * state.
   */
  CANCELED("canceled"),

  /**
   * Task terminated due to an error during processing. TaskStatus.message may contain error
   * details. This is a terminal state.
   */
  FAILED("failed"),

  /**
   * Task terminated due to rejection by remote agent. TaskStatus.message may contain rejection
   * details. This is a terminal state.
   */
  REJECTED("rejected"),

  /**
   * Agent requires additional authentication from the client/user to proceed. The task is
   * effectively paused and waiting for authentication. This is an interrupted state that requires
   * client action.
   */
  AUTH_REQUIRED("auth-required"),

  /**
   * The state of the task cannot be determined (e.g., task ID is invalid, unknown, or has expired).
   * This is a terminal state.
   */
  UNKNOWN("unknown");

  private final String value;

  TaskState(String value) {
    this.value = value;
  }

  /**
   * Gets the string representation used in JSON serialization.
   *
   * @return the JSON value for this task state
   */
  @JsonValue
  public String getValue() {
    return value;
  }

  /**
   * Checks if this task state is terminal (no further state changes expected).
   *
   * @return true if this is a terminal state, false otherwise
   */
  public boolean isTerminal() {
    return switch (this) {
      case COMPLETED, CANCELED, FAILED, REJECTED, UNKNOWN -> true;
      case SUBMITTED, WORKING, INPUT_REQUIRED, AUTH_REQUIRED -> false;
    };
  }

  /**
   * Checks if this task state represents an interruption requiring client action.
   *
   * @return true if this state requires client intervention, false otherwise
   */
  public boolean isInterrupted() {
    return this == INPUT_REQUIRED || this == AUTH_REQUIRED;
  }

  /**
   * Creates a TaskState from its string value.
   *
   * @param value the string value
   * @return the corresponding TaskState
   * @throws IllegalArgumentException if the value is not recognized
   */
  public static TaskState fromValue(String value) {
    for (TaskState state : values()) {
      if (state.value.equals(value)) {
        return state;
      }
    }
    throw new IllegalArgumentException("Unknown TaskState value: " + value);
  }

  @Override
  public String toString() {
    return value;
  }
}
