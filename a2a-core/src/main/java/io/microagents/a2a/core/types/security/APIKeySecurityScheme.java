package io.microagents.a2a.core.types.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * API Key security scheme.
 *
 * <p>This security scheme represents authentication via API keys that can be passed in headers,
 * query parameters, or cookies.
 */
public class APIKeySecurityScheme extends SecurityScheme {

  @JsonProperty("type")
  private final String type = "apiKey";

  @NotBlank
  @JsonProperty("name")
  private final String name;

  @NotNull
  @JsonProperty("in")
  private final ApiKeyLocation in;

  @JsonProperty("description")
  private final String description;

  /**
   * Creates a new APIKeySecurityScheme with required fields.
   *
   * @param name the name of the header, query or cookie parameter to be used
   * @param in the location of the API key
   */
  public APIKeySecurityScheme(@NotBlank String name, @NotNull ApiKeyLocation in) {
    this(name, in, null);
  }

  /**
   * Creates a new APIKeySecurityScheme with all fields.
   *
   * @param name the name of the header, query or cookie parameter to be used
   * @param in the location of the API key
   * @param description description of this security scheme (may be null)
   */
  public APIKeySecurityScheme(
      @NotBlank String name, @NotNull ApiKeyLocation in, String description) {
    this.name = Objects.requireNonNull(name, "name cannot be null").trim();
    if (this.name.isEmpty()) {
      throw new IllegalArgumentException("name cannot be blank");
    }
    this.in = Objects.requireNonNull(in, "in cannot be null");
    this.description = description;
  }

  /**
   * Gets the security scheme type (always "apiKey").
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Gets the name of the header, query or cookie parameter to be used.
   *
   * @return the parameter name
   */
  @NotBlank
  public String getName() {
    return name;
  }

  /**
   * Gets the location of the API key.
   *
   * @return the API key location
   */
  @NotNull
  public ApiKeyLocation getIn() {
    return in;
  }

  /**
   * Gets the description of this security scheme.
   *
   * @return the description, or null if not specified
   */
  public String getDescription() {
    return description;
  }

  /**
   * Creates a new APIKeySecurityScheme for a header parameter.
   *
   * @param headerName the name of the header
   * @return a new APIKeySecurityScheme instance
   */
  public static APIKeySecurityScheme header(@NotBlank String headerName) {
    return new APIKeySecurityScheme(headerName, ApiKeyLocation.HEADER);
  }

  /**
   * Creates a new APIKeySecurityScheme for a query parameter.
   *
   * @param queryName the name of the query parameter
   * @return a new APIKeySecurityScheme instance
   */
  public static APIKeySecurityScheme query(@NotBlank String queryName) {
    return new APIKeySecurityScheme(queryName, ApiKeyLocation.QUERY);
  }

  /**
   * Creates a new APIKeySecurityScheme for a cookie.
   *
   * @param cookieName the name of the cookie
   * @return a new APIKeySecurityScheme instance
   */
  public static APIKeySecurityScheme cookie(@NotBlank String cookieName) {
    return new APIKeySecurityScheme(cookieName, ApiKeyLocation.COOKIE);
  }

  /**
   * Creates a new APIKeySecurityScheme with the specified parameters.
   *
   * @param name the parameter name
   * @param in the location
   * @return a new APIKeySecurityScheme instance
   */
  public static APIKeySecurityScheme of(@NotBlank String name, @NotNull ApiKeyLocation in) {
    return new APIKeySecurityScheme(name, in);
  }

  /**
   * Creates a builder for constructing APIKeySecurityScheme instances.
   *
   * @param name the required parameter name
   * @param in the required location
   * @return a new builder instance
   */
  public static Builder builder(@NotBlank String name, @NotNull ApiKeyLocation in) {
    return new Builder(name, in);
  }

  /** Builder for constructing APIKeySecurityScheme instances. */
  public static class Builder {
    private final String name;
    private final ApiKeyLocation in;
    private String description;

    private Builder(@NotBlank String name, @NotNull ApiKeyLocation in) {
      this.name = Objects.requireNonNull(name, "name cannot be null").trim();
      if (this.name.isEmpty()) {
        throw new IllegalArgumentException("name cannot be blank");
      }
      this.in = Objects.requireNonNull(in, "in cannot be null");
    }

    /**
     * Sets the description.
     *
     * @param description the description
     * @return this builder
     */
    public Builder withDescription(String description) {
      this.description = description;
      return this;
    }

    /**
     * Builds the APIKeySecurityScheme instance.
     *
     * @return a new APIKeySecurityScheme
     */
    public APIKeySecurityScheme build() {
      return new APIKeySecurityScheme(name, in, description);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    APIKeySecurityScheme that = (APIKeySecurityScheme) o;
    return Objects.equals(type, that.type)
        && Objects.equals(name, that.name)
        && in == that.in
        && Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, name, in, description);
  }

  @Override
  public String toString() {
    return "APIKeySecurityScheme{"
        + "type='"
        + type
        + '\''
        + ", name='"
        + name
        + '\''
        + ", in="
        + in
        + ", description='"
        + description
        + '\''
        + '}';
  }
}
