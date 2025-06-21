package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Base interface for any JSON-RPC 2.0 request or response.
 *
 * <p>This follows the JSON-RPC 2.0 specification for message structure:
 *
 * <ul>
 *   <li>jsonrpc - Must be exactly "2.0"
 *   <li>id - Optional identifier (String or Number)
 * </ul>
 *
 * @see <a href="https://www.jsonrpc.org/specification">JSON-RPC 2.0 Specification</a>
 */
@Schema(description = "Base interface for any JSON-RPC 2.0 request or response")
public abstract class JSONRPCMessage {

  /** Specifies the version of the JSON-RPC protocol. MUST be exactly "2.0". */
  @JsonProperty("jsonrpc")
  @NotNull
  @Schema(
      description = "Specifies the version of the JSON-RPC protocol. MUST be exactly \"2.0\".",
      example = "2.0",
      required = true)
  private final String jsonrpc = "2.0";

  /**
   * An identifier established by the Client that MUST contain a String or Number. Numbers SHOULD
   * NOT contain fractional parts. May be null for notifications.
   */
  @JsonProperty("id")
  @Schema(
      description =
          "An identifier established by the Client that MUST contain a String or Number. "
              + "Numbers SHOULD NOT contain fractional parts.",
      example = "1")
  private String id;

  /** Default constructor. */
  protected JSONRPCMessage() {}

  /**
   * Constructor with ID.
   *
   * @param id the request/response identifier
   */
  protected JSONRPCMessage(String id) {
    this.id = id;
  }

  /**
   * Gets the JSON-RPC version.
   *
   * @return always "2.0"
   */
  public String getJsonrpc() {
    return jsonrpc;
  }

  /**
   * Gets the message ID.
   *
   * @return the message ID, may be null
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the message ID.
   *
   * @param id the message ID
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Creates a copy of this message with a new ID.
   *
   * @param newId the new ID
   * @return a new message instance with the specified ID
   */
  public abstract JSONRPCMessage withId(String newId);

  @Override
  public String toString() {
    return getClass().getSimpleName()
        + "{"
        + "jsonrpc='"
        + jsonrpc
        + '\''
        + ", id='"
        + id
        + '\''
        + '}';
  }
}
