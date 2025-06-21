package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.microagents.a2a.core.types.notifications.*;

/**
 * Union type representing all possible streaming events in the A2A protocol.
 *
 * <p>Events are emitted during streaming operations such as message/stream and tasks/resubscribe to
 * provide real-time updates about task progress, messages, and artifact changes.
 *
 * @since 0.1.0
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "kind",
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = Message.class, name = "message"),
  @JsonSubTypes.Type(value = Task.class, name = "task"),
  @JsonSubTypes.Type(value = TaskStatusUpdateEvent.class, name = "status-update"),
  @JsonSubTypes.Type(value = TaskArtifactUpdateEvent.class, name = "artifact-update")
})
public sealed interface Event
    permits Message, Task, TaskStatusUpdateEvent, TaskArtifactUpdateEvent {

  /**
   * Gets the kind discriminator for this event.
   *
   * @return the event kind name
   */
  default String getKind() {
    return switch (this) {
      case Message ignored -> "message";
      case Task ignored -> "task";
      case TaskStatusUpdateEvent ignored -> "status-update";
      case TaskArtifactUpdateEvent ignored -> "artifact-update";
    };
  }

  /**
   * Factory method to create a message event.
   *
   * @param message the message
   * @return the message as an event
   */
  static Event message(Message message) {
    return message;
  }

  /**
   * Factory method to create a task event.
   *
   * @param task the task
   * @return the task as an event
   */
  static Event task(Task task) {
    return task;
  }

  /**
   * Factory method to create a task status update event.
   *
   * @param event the task status update event
   * @return the event
   */
  static Event taskStatusUpdate(TaskStatusUpdateEvent event) {
    return event;
  }

  /**
   * Factory method to create a task artifact update event.
   *
   * @param event the task artifact update event
   * @return the event
   */
  static Event taskArtifactUpdate(TaskArtifactUpdateEvent event) {
    return event;
  }
}
