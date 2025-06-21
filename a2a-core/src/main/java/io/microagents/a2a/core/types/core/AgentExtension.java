package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.notifications.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A declaration of an extension supported by an Agent.
 *
 * <p>Extensions allow agents to declare additional capabilities or protocols they support beyond
 * the core A2A specification.
 */
public class AgentExtension {

  @NotBlank
  @JsonProperty("uri")
  private final String uri;

  @JsonProperty("description")
  private final String description;

  @JsonProperty("required")
  private final Boolean required;

  @JsonProperty("params")
  private final Map<String, Object> params;

  /**
   * Creates a new AgentExtension with only the required URI.
   *
   * @param uri the URI of the extension
   */
  public AgentExtension(@NotBlank String uri) {
    this(uri, null, null, null);
  }

  /**
   * Creates a new AgentExtension with all fields.
   *
   * @param uri the URI of the extension
   * @param description description of how this agent uses this extension
   * @param required whether the client must follow specific requirements of the extension
   * @param params optional configuration for the extension
   */
  public AgentExtension(
      @NotBlank String uri, String description, Boolean required, Map<String, Object> params) {
    this.uri = Objects.requireNonNull(uri, "uri cannot be null").trim();
    if (this.uri.isEmpty()) {
      throw new IllegalArgumentException("uri cannot be blank");
    }
    this.description = description;
    this.required = required;
    this.params = params == null ? null : Collections.unmodifiableMap(new HashMap<>(params));
  }

  /**
   * Gets the URI of the extension.
   *
   * @return the extension URI
   */
  @NotBlank
  public String getUri() {
    return uri;
  }

  /**
   * Gets the description of how this agent uses this extension.
   *
   * @return the description, or null if not specified
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets whether the client must follow specific requirements of the extension.
   *
   * @return true if required, false if optional, null if not specified
   */
  public Boolean getRequired() {
    return required;
  }

  /**
   * Checks if this extension is required by the agent.
   *
   * @return true if required, false otherwise (defaults to false if not specified)
   */
  public boolean isRequired() {
    return Boolean.TRUE.equals(required);
  }

  /**
   * Gets the optional configuration for the extension.
   *
   * @return unmodifiable map of parameters, or null if none
   */
  public Map<String, Object> getParams() {
    return params;
  }

  /**
   * Creates a new AgentExtension with the specified URI.
   *
   * @param uri the extension URI
   * @return a new AgentExtension instance
   */
  public static AgentExtension of(@NotBlank String uri) {
    return new AgentExtension(uri);
  }

  /**
   * Creates a new AgentExtension with URI and description.
   *
   * @param uri the extension URI
   * @param description the description
   * @return a new AgentExtension instance
   */
  public static AgentExtension of(@NotBlank String uri, String description) {
    return new AgentExtension(uri, description, null, null);
  }

  /**
   * Creates a builder for constructing AgentExtension instances.
   *
   * @param uri the required extension URI
   * @return a new builder instance
   */
  public static Builder builder(@NotBlank String uri) {
    return new Builder(uri);
  }

  /** Builder for constructing AgentExtension instances. */
  public static class Builder {
    private final String uri;
    private String description;
    private Boolean required;
    private final Map<String, Object> params = new HashMap<>();

    private Builder(@NotBlank String uri) {
      this.uri = Objects.requireNonNull(uri, "uri cannot be null").trim();
      if (this.uri.isEmpty()) {
        throw new IllegalArgumentException("uri cannot be blank");
      }
    }

    /**
     * Sets the extension description.
     *
     * @param description the description
     * @return this builder
     */
    public Builder withDescription(String description) {
      this.description = description;
      return this;
    }

    /**
     * Sets whether the extension is required.
     *
     * @param required true if required, false if optional
     * @return this builder
     */
    public Builder withRequired(boolean required) {
      this.required = required;
      return this;
    }

    /**
     * Marks the extension as required.
     *
     * @return this builder
     */
    public Builder required() {
      this.required = true;
      return this;
    }

    /**
     * Marks the extension as optional.
     *
     * @return this builder
     */
    public Builder optional() {
      this.required = false;
      return this;
    }

    /**
     * Adds a parameter.
     *
     * @param key the parameter key
     * @param value the parameter value
     * @return this builder
     */
    public Builder addParam(@NotBlank String key, Object value) {
      Objects.requireNonNull(key, "key cannot be null");
      if (key.trim().isEmpty()) {
        throw new IllegalArgumentException("key cannot be blank");
      }
      this.params.put(key.trim(), value);
      return this;
    }

    /**
     * Adds multiple parameters.
     *
     * @param params the parameters to add
     * @return this builder
     */
    public Builder addParams(Map<String, Object> params) {
      Objects.requireNonNull(params, "params cannot be null");
      params.forEach((key, value) -> addParam(key, value));
      return this;
    }

    /**
     * Builds the AgentExtension instance.
     *
     * @return a new AgentExtension
     */
    public AgentExtension build() {
      return new AgentExtension(uri, description, required, params.isEmpty() ? null : params);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AgentExtension that = (AgentExtension) o;
    return Objects.equals(uri, that.uri)
        && Objects.equals(description, that.description)
        && Objects.equals(required, that.required)
        && Objects.equals(params, that.params);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uri, description, required, params);
  }

  @Override
  public String toString() {
    return "AgentExtension{"
        + "uri='"
        + uri
        + '\''
        + ", description='"
        + description
        + '\''
        + ", required="
        + required
        + ", params="
        + params
        + '}';
  }
}
