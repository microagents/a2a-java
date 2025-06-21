package io.microagents.a2a.core.types.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.notifications.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a JSON-RPC 2.0 Request object.
 *
 * <p>A JSON-RPC 2.0 request contains:
 *
 * <ul>
 *   <li>jsonrpc - Must be exactly "2.0"
 *   <li>method - The method name to invoke
 *   <li>params - Optional parameters for the method
 *   <li>id - Optional identifier
 * </ul>
 *
 * @see <a href="https://www.jsonrpc.org/specification">JSON-RPC 2.0 Specification</a>
 */
@Schema(description = "Represents a JSON-RPC 2.0 Request object")
public class JSONRPCRequest extends JSONRPCMessage {

  /** A String containing the name of the method to be invoked. */
  @JsonProperty("method")
  @NotNull
  @NotBlank
  @Schema(
      description = "A String containing the name of the method to be invoked",
      example = "message/send",
      required = true)
  private String method;

  /**
   * A Structured value that holds the parameter values to be used during the invocation of the
   * method. This MAY be omitted.
   */
  @JsonProperty("params")
  @Schema(
      description =
          "A Structured value that holds the parameter values to be used during the invocation of the method")
  private Map<String, Object> params;

  /** Default constructor. */
  public JSONRPCRequest() {
    super();
  }

  /**
   * Constructor with method name.
   *
   * @param method the method name
   */
  public JSONRPCRequest(String method) {
    super();
    this.method = method;
  }

  /**
   * Constructor with method name and ID.
   *
   * @param method the method name
   * @param id the request ID
   */
  public JSONRPCRequest(String method, String id) {
    super(id);
    this.method = method;
  }

  /**
   * Full constructor.
   *
   * @param method the method name
   * @param params the method parameters
   * @param id the request ID
   */
  public JSONRPCRequest(String method, Map<String, Object> params, String id) {
    super(id);
    this.method = method;
    this.params = params;
  }

  /**
   * Gets the method name.
   *
   * @return the method name
   */
  public String getMethod() {
    return method;
  }

  /**
   * Sets the method name.
   *
   * @param method the method name
   */
  public void setMethod(String method) {
    this.method = method;
  }

  /**
   * Gets the method parameters.
   *
   * @return the method parameters, may be null
   */
  public Map<String, Object> getParams() {
    return params;
  }

  /**
   * Sets the method parameters.
   *
   * @param params the method parameters
   */
  public void setParams(Map<String, Object> params) {
    this.params = params;
  }

  /**
   * Creates a new request with the specified method.
   *
   * @param method the method name
   * @return a new JSONRPCRequest
   */
  public static JSONRPCRequest of(String method) {
    return new JSONRPCRequest(method);
  }

  /**
   * Creates a new request with the specified method and parameters.
   *
   * @param method the method name
   * @param params the method parameters
   * @return a new JSONRPCRequest
   */
  public static JSONRPCRequest of(String method, Map<String, Object> params) {
    return new JSONRPCRequest(method, params, null);
  }

  /**
   * Creates a copy of this request with a new method.
   *
   * @param newMethod the new method name
   * @return a new request with the specified method
   */
  public JSONRPCRequest withMethod(String newMethod) {
    return new JSONRPCRequest(newMethod, this.params, this.getId());
  }

  /**
   * Creates a copy of this request with new parameters.
   *
   * @param newParams the new parameters
   * @return a new request with the specified parameters
   */
  public JSONRPCRequest withParams(Map<String, Object> newParams) {
    return new JSONRPCRequest(this.method, newParams, this.getId());
  }

  @Override
  public JSONRPCMessage withId(String newId) {
    return new JSONRPCRequest(this.method, this.params, newId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    JSONRPCRequest that = (JSONRPCRequest) obj;
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
    return "JSONRPCRequest{"
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
