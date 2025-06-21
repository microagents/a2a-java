package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Represents a JSON-RPC 2.0 Success Response object.
 *
 * <p>A JSON-RPC 2.0 success response contains:
 *
 * <ul>
 *   <li>jsonrpc - Must be exactly "2.0"
 *   <li>result - The result of the successful method call
 *   <li>id - The identifier from the original request
 * </ul>
 *
 * @param <T> the type of the result object
 * @see <a href="https://www.jsonrpc.org/specification">JSON-RPC 2.0 Specification</a>
 */
@Schema(description = "Represents a JSON-RPC 2.0 Success Response object")
public class JSONRPCSuccessResponse<T> extends JSONRPCMessage {

  /** The result object on success. */
  @JsonProperty("result")
  @NotNull
  @Schema(description = "The result object on success", required = true)
  private T result;

  /** Default constructor. */
  public JSONRPCSuccessResponse() {
    super();
  }

  /**
   * Constructor with result.
   *
   * @param result the result object
   */
  public JSONRPCSuccessResponse(T result) {
    super();
    this.result = result;
  }

  /**
   * Constructor with result and ID.
   *
   * @param result the result object
   * @param id the response ID
   */
  public JSONRPCSuccessResponse(T result, String id) {
    super(id);
    this.result = result;
  }

  /**
   * Gets the result object.
   *
   * @return the result object
   */
  public T getResult() {
    return result;
  }

  /**
   * Sets the result object.
   *
   * @param result the result object
   */
  public void setResult(T result) {
    this.result = result;
  }

  /**
   * Creates a new success response with the specified result.
   *
   * @param <R> the result type
   * @param result the result object
   * @return a new JSONRPCSuccessResponse
   */
  public static <R> JSONRPCSuccessResponse<R> of(R result) {
    return new JSONRPCSuccessResponse<>(result);
  }

  /**
   * Creates a new success response with the specified result and ID.
   *
   * @param <R> the result type
   * @param result the result object
   * @param id the response ID
   * @return a new JSONRPCSuccessResponse
   */
  public static <R> JSONRPCSuccessResponse<R> of(R result, String id) {
    return new JSONRPCSuccessResponse<>(result, id);
  }

  /**
   * Creates a copy of this response with a new result.
   *
   * @param <R> the new result type
   * @param newResult the new result object
   * @return a new response with the specified result
   */
  public <R> JSONRPCSuccessResponse<R> withResult(R newResult) {
    return new JSONRPCSuccessResponse<>(newResult, this.getId());
  }

  @Override
  public JSONRPCMessage withId(String newId) {
    return new JSONRPCSuccessResponse<>(this.result, newId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    JSONRPCSuccessResponse<?> that = (JSONRPCSuccessResponse<?>) obj;
    return Objects.equals(getId(), that.getId()) && Objects.equals(result, that.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), result);
  }

  @Override
  public String toString() {
    return "JSONRPCSuccessResponse{" + "result=" + result + ", id='" + getId() + '\'' + '}';
  }
}
