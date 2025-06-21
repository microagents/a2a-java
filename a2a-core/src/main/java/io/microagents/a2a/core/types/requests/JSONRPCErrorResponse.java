package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Represents a JSON-RPC 2.0 Error Response object.
 *
 * <p>A JSON-RPC 2.0 error response contains:
 *
 * <ul>
 *   <li>jsonrpc - Must be exactly "2.0"
 *   <li>error - The error object describing what went wrong
 *   <li>id - The identifier from the original request (may be null if request was invalid)
 * </ul>
 *
 * @see <a href="https://www.jsonrpc.org/specification">JSON-RPC 2.0 Specification</a>
 */
@Schema(description = "Represents a JSON-RPC 2.0 Error Response object")
public class JSONRPCErrorResponse extends JSONRPCMessage {

  /** The error object describing what went wrong. */
  @JsonProperty("error")
  @Valid
  @NotNull
  @Schema(description = "The error object describing what went wrong", required = true)
  private JSONRPCError error;

  /** Default constructor. */
  public JSONRPCErrorResponse() {
    super();
  }

  /**
   * Constructor with error.
   *
   * @param error the error object
   */
  public JSONRPCErrorResponse(JSONRPCError error) {
    super();
    this.error = error;
  }

  /**
   * Constructor with error and ID.
   *
   * @param error the error object
   * @param id the response ID
   */
  public JSONRPCErrorResponse(JSONRPCError error, String id) {
    super(id);
    this.error = error;
  }

  /**
   * Gets the error object.
   *
   * @return the error object
   */
  public JSONRPCError getError() {
    return error;
  }

  /**
   * Sets the error object.
   *
   * @param error the error object
   */
  public void setError(JSONRPCError error) {
    this.error = error;
  }

  /**
   * Creates a new error response with the specified error.
   *
   * @param error the error object
   * @return a new JSONRPCErrorResponse
   */
  public static JSONRPCErrorResponse of(JSONRPCError error) {
    return new JSONRPCErrorResponse(error);
  }

  /**
   * Creates a new error response with the specified error and ID.
   *
   * @param error the error object
   * @param id the response ID
   * @return a new JSONRPCErrorResponse
   */
  public static JSONRPCErrorResponse of(JSONRPCError error, String id) {
    return new JSONRPCErrorResponse(error, id);
  }

  /**
   * Creates a new error response with a parse error.
   *
   * @return a new JSONRPCErrorResponse with parse error
   */
  public static JSONRPCErrorResponse parseError() {
    return new JSONRPCErrorResponse(JSONRPCError.parseError());
  }

  /**
   * Creates a new error response with an invalid request error.
   *
   * @return a new JSONRPCErrorResponse with invalid request error
   */
  public static JSONRPCErrorResponse invalidRequest() {
    return new JSONRPCErrorResponse(JSONRPCError.invalidRequest());
  }

  /**
   * Creates a new error response with a method not found error.
   *
   * @return a new JSONRPCErrorResponse with method not found error
   */
  public static JSONRPCErrorResponse methodNotFound() {
    return new JSONRPCErrorResponse(JSONRPCError.methodNotFound());
  }

  /**
   * Creates a new error response with an invalid parameters error.
   *
   * @return a new JSONRPCErrorResponse with invalid parameters error
   */
  public static JSONRPCErrorResponse invalidParams() {
    return new JSONRPCErrorResponse(JSONRPCError.invalidParams());
  }

  /**
   * Creates a new error response with an internal error.
   *
   * @return a new JSONRPCErrorResponse with internal error
   */
  public static JSONRPCErrorResponse internalError() {
    return new JSONRPCErrorResponse(JSONRPCError.internalError());
  }

  /**
   * Creates a copy of this response with a new error.
   *
   * @param newError the new error object
   * @return a new response with the specified error
   */
  public JSONRPCErrorResponse withError(JSONRPCError newError) {
    return new JSONRPCErrorResponse(newError, this.getId());
  }

  @Override
  public JSONRPCMessage withId(String newId) {
    return new JSONRPCErrorResponse(this.error, newId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    JSONRPCErrorResponse that = (JSONRPCErrorResponse) obj;
    return Objects.equals(getId(), that.getId()) && Objects.equals(error, that.error);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), error);
  }

  @Override
  public String toString() {
    return "JSONRPCErrorResponse{" + "error=" + error + ", id='" + getId() + '\'' + '}';
  }
}
