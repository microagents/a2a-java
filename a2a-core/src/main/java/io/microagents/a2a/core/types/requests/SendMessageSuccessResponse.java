package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * JSON-RPC success response model for the 'message/send' method.
 *
 * <p>This response is returned when a message send request is successfully processed. The result
 * can be either a Task (if the agent created a task) or a Message (if the agent responded
 * directly).
 */
@Schema(description = "JSON-RPC success response model for the 'message/send' method")
public class SendMessageSuccessResponse extends JSONRPCMessage {

  /** The result object on success - can be either Task or Message */
  @JsonProperty("result")
  @Valid
  @NotNull
  @Schema(description = "The result object on success", required = true)
  private Object result;

  /** Default constructor. */
  public SendMessageSuccessResponse() {
    super();
  }

  /**
   * Constructor with result.
   *
   * @param result the result object (Task or Message)
   */
  public SendMessageSuccessResponse(Object result) {
    super();
    this.result = result;
  }

  /**
   * Constructor with result and ID.
   *
   * @param result the result object (Task or Message)
   * @param id the response ID
   */
  public SendMessageSuccessResponse(Object result, String id) {
    super(id);
    this.result = result;
  }

  /**
   * Gets the result object.
   *
   * @return the result object (Task or Message)
   */
  public Object getResult() {
    return result;
  }

  /**
   * Sets the result object.
   *
   * @param result the result object (Task or Message)
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
   * Creates a new SendMessageSuccessResponse with a Task result.
   *
   * @param task the task result
   * @return a new SendMessageSuccessResponse
   */
  public static SendMessageSuccessResponse ofTask(Task task) {
    return new SendMessageSuccessResponse(task);
  }

  /**
   * Creates a new SendMessageSuccessResponse with a Message result.
   *
   * @param message the message result
   * @return a new SendMessageSuccessResponse
   */
  public static SendMessageSuccessResponse ofMessage(Message message) {
    return new SendMessageSuccessResponse(message);
  }

  /**
   * Creates a new SendMessageSuccessResponse with a Task result and ID.
   *
   * @param task the task result
   * @param id the response ID
   * @return a new SendMessageSuccessResponse
   */
  public static SendMessageSuccessResponse ofTask(Task task, String id) {
    return new SendMessageSuccessResponse(task, id);
  }

  /**
   * Creates a new SendMessageSuccessResponse with a Message result and ID.
   *
   * @param message the message result
   * @param id the response ID
   * @return a new SendMessageSuccessResponse
   */
  public static SendMessageSuccessResponse ofMessage(Message message, String id) {
    return new SendMessageSuccessResponse(message, id);
  }

  /**
   * Creates a copy of this response with a new result.
   *
   * @param newResult the new result object
   * @return a new response with the specified result
   */
  public SendMessageSuccessResponse withResult(Object newResult) {
    return new SendMessageSuccessResponse(newResult, this.getId());
  }

  @Override
  public JSONRPCMessage withId(String newId) {
    return new SendMessageSuccessResponse(this.result, newId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    SendMessageSuccessResponse that = (SendMessageSuccessResponse) obj;
    return Objects.equals(getId(), that.getId()) && Objects.equals(result, that.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), result);
  }

  @Override
  public String toString() {
    return "SendMessageSuccessResponse{" + "result=" + result + ", id='" + getId() + '\'' + '}';
  }
}
