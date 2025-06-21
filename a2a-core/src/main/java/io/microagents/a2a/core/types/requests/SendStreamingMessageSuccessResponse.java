package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * JSON-RPC success response model for the 'message/stream' method.
 *
 * <p>This response is returned for each event in a streaming message response. The result can be a
 * Task, Message, TaskStatusUpdateEvent, or TaskArtifactUpdateEvent.
 */
@Schema(description = "JSON-RPC success response model for the 'message/stream' method")
public class SendStreamingMessageSuccessResponse extends JSONRPCMessage {

  /**
   * The result object on success - can be Task, Message, TaskStatusUpdateEvent, or
   * TaskArtifactUpdateEvent
   */
  @JsonProperty("result")
  @Valid
  @NotNull
  @Schema(description = "The result object on success", required = true)
  private Object result;

  /** Default constructor. */
  public SendStreamingMessageSuccessResponse() {
    super();
  }

  /**
   * Constructor with result.
   *
   * @param result the result object
   */
  public SendStreamingMessageSuccessResponse(Object result) {
    super();
    this.result = result;
  }

  /**
   * Constructor with result and ID.
   *
   * @param result the result object
   * @param id the response ID
   */
  public SendStreamingMessageSuccessResponse(Object result, String id) {
    super(id);
    this.result = result;
  }

  /**
   * Gets the result object.
   *
   * @return the result object
   */
  public Object getResult() {
    return result;
  }

  /**
   * Sets the result object.
   *
   * @param result the result object
   */
  public void setResult(Object result) {
    this.result = result;
  }

  /**
   * Gets the result as a Task if it is one.
   *
   * @return the result as a Task, or null if not a Task
   */
  public Task getResultAsTask() {
    return result instanceof Task ? (Task) result : null;
  }

  /**
   * Gets the result as a Message if it is one.
   *
   * @return the result as a Message, or null if not a Message
   */
  public Message getResultAsMessage() {
    return result instanceof Message ? (Message) result : null;
  }

  /**
   * Gets the result as a TaskStatusUpdateEvent if it is one.
   *
   * @return the result as a TaskStatusUpdateEvent, or null if not a TaskStatusUpdateEvent
   */
  public TaskStatusUpdateEvent getResultAsTaskStatusUpdateEvent() {
    return result instanceof TaskStatusUpdateEvent ? (TaskStatusUpdateEvent) result : null;
  }

  /**
   * Gets the result as a TaskArtifactUpdateEvent if it is one.
   *
   * @return the result as a TaskArtifactUpdateEvent, or null if not a TaskArtifactUpdateEvent
   */
  public TaskArtifactUpdateEvent getResultAsTaskArtifactUpdateEvent() {
    return result instanceof TaskArtifactUpdateEvent ? (TaskArtifactUpdateEvent) result : null;
  }

  /**
   * Checks if the result is a Task.
   *
   * @return true if the result is a Task
   */
  public boolean isTask() {
    return result instanceof Task;
  }

  /**
   * Checks if the result is a Message.
   *
   * @return true if the result is a Message
   */
  public boolean isMessage() {
    return result instanceof Message;
  }

  /**
   * Checks if the result is a TaskStatusUpdateEvent.
   *
   * @return true if the result is a TaskStatusUpdateEvent
   */
  public boolean isTaskStatusUpdateEvent() {
    return result instanceof TaskStatusUpdateEvent;
  }

  /**
   * Checks if the result is a TaskArtifactUpdateEvent.
   *
   * @return true if the result is a TaskArtifactUpdateEvent
   */
  public boolean isTaskArtifactUpdateEvent() {
    return result instanceof TaskArtifactUpdateEvent;
  }

  /**
   * Creates a new SendStreamingMessageSuccessResponse with a Task result.
   *
   * @param task the task result
   * @return a new SendStreamingMessageSuccessResponse
   */
  public static SendStreamingMessageSuccessResponse ofTask(Task task) {
    return new SendStreamingMessageSuccessResponse(task);
  }

  /**
   * Creates a new SendStreamingMessageSuccessResponse with a Message result.
   *
   * @param message the message result
   * @return a new SendStreamingMessageSuccessResponse
   */
  public static SendStreamingMessageSuccessResponse ofMessage(Message message) {
    return new SendStreamingMessageSuccessResponse(message);
  }

  /**
   * Creates a new SendStreamingMessageSuccessResponse with a TaskStatusUpdateEvent result.
   *
   * @param event the task status update event result
   * @return a new SendStreamingMessageSuccessResponse
   */
  public static SendStreamingMessageSuccessResponse ofTaskStatusUpdateEvent(
      TaskStatusUpdateEvent event) {
    return new SendStreamingMessageSuccessResponse(event);
  }

  /**
   * Creates a new SendStreamingMessageSuccessResponse with a TaskArtifactUpdateEvent result.
   *
   * @param event the task artifact update event result
   * @return a new SendStreamingMessageSuccessResponse
   */
  public static SendStreamingMessageSuccessResponse ofTaskArtifactUpdateEvent(
      TaskArtifactUpdateEvent event) {
    return new SendStreamingMessageSuccessResponse(event);
  }

  /**
   * Creates a copy of this response with a new result.
   *
   * @param newResult the new result object
   * @return a new response with the specified result
   */
  public SendStreamingMessageSuccessResponse withResult(Object newResult) {
    return new SendStreamingMessageSuccessResponse(newResult, this.getId());
  }

  @Override
  public JSONRPCMessage withId(String newId) {
    return new SendStreamingMessageSuccessResponse(this.result, newId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    SendStreamingMessageSuccessResponse that = (SendStreamingMessageSuccessResponse) obj;
    return Objects.equals(getId(), that.getId()) && Objects.equals(result, that.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), result);
  }

  @Override
  public String toString() {
    return "SendStreamingMessageSuccessResponse{"
        + "result="
        + result
        + ", id='"
        + getId()
        + '\''
        + '}';
  }
}
