package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * JSON-RPC request model for the 'message/stream' method.
 *
 * <p>This represents a streaming message send request to an A2A agent. The agent will process the
 * message and return a stream of responses via Server-Sent Events (SSE).
 */
@Schema(description = "JSON-RPC request model for the 'message/stream' method")
public class SendStreamingMessageRequest extends JSONRPCMessage {

  /** A String containing the name of the method to be invoked. */
  @JsonProperty("method")
  @NotNull
  @Schema(
      description = "A String containing the name of the method to be invoked",
      example = "message/stream",
      required = true)
  private final String method = "message/stream";

  /**
   * A Structured value that holds the parameter values to be used during the invocation of the
   * method.
   */
  @JsonProperty("params")
  @Valid
  @NotNull
  @Schema(
      description =
          "A Structured value that holds the parameter values to be used during the invocation of the method",
      required = true)
  private MessageSendParams params;

  /** Default constructor. */
  public SendStreamingMessageRequest() {
    super();
  }

  /**
   * Constructor with params.
   *
   * @param params the message send parameters
   */
  public SendStreamingMessageRequest(MessageSendParams params) {
    super();
    this.params = params;
  }

  /**
   * Constructor with params and ID.
   *
   * @param params the message send parameters
   * @param id the request ID
   */
  public SendStreamingMessageRequest(MessageSendParams params, String id) {
    super(id);
    this.params = params;
  }

  /**
   * Gets the method name.
   *
   * @return always "message/stream"
   */
  public String getMethod() {
    return method;
  }

  /**
   * Gets the message send parameters.
   *
   * @return the message send parameters
   */
  public MessageSendParams getParams() {
    return params;
  }

  /**
   * Sets the message send parameters.
   *
   * @param params the message send parameters
   */
  public void setParams(MessageSendParams params) {
    this.params = params;
  }

  /**
   * Creates a new SendStreamingMessageRequest with the specified parameters.
   *
   * @param params the message send parameters
   * @return a new SendStreamingMessageRequest
   */
  public static SendStreamingMessageRequest of(MessageSendParams params) {
    return new SendStreamingMessageRequest(params);
  }

  /**
   * Creates a new SendStreamingMessageRequest with the specified message.
   *
   * @param message the message to send
   * @return a new SendStreamingMessageRequest
   */
  public static SendStreamingMessageRequest of(Message message) {
    return new SendStreamingMessageRequest(MessageSendParams.of(message));
  }

  /**
   * Creates a copy of this request with new parameters.
   *
   * @param newParams the new parameters
   * @return a new request with the specified parameters
   */
  public SendStreamingMessageRequest withParams(MessageSendParams newParams) {
    return new SendStreamingMessageRequest(newParams, this.getId());
  }

  /**
   * Creates a copy of this request with a new message.
   *
   * @param newMessage the new message
   * @return a new request with the specified message
   */
  public SendStreamingMessageRequest withMessage(Message newMessage) {
    MessageSendParams newParams = this.params.withMessage(newMessage);
    return new SendStreamingMessageRequest(newParams, this.getId());
  }

  @Override
  public JSONRPCMessage withId(String newId) {
    return new SendStreamingMessageRequest(this.params, newId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    SendStreamingMessageRequest that = (SendStreamingMessageRequest) obj;
    return Objects.equals(getId(), that.getId())
        && Objects.equals(method, that.method)
        && Objects.equals(params, that.params);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), method, params);
  }

  @Override
  public String toString() {
    return "SendStreamingMessageRequest{"
        + "method='"
        + method
        + '\''
        + ", params="
        + params
        + ", id='"
        + getId()
        + '\''
        + '}';
  }
}
