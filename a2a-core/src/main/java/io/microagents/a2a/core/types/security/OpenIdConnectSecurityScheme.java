package io.microagents.a2a.core.types.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * OpenID Connect security scheme configuration.
 *
 * <p>This security scheme represents OpenID Connect authentication, which is built on top of OAuth
 * 2.0.
 */
public class OpenIdConnectSecurityScheme extends SecurityScheme {

  @JsonProperty("type")
  private final String type = "openIdConnect";

  @NotBlank
  @JsonProperty("openIdConnectUrl")
  private final String openIdConnectUrl;

  @JsonProperty("description")
  private final String description;

  /**
   * Creates a new OpenIdConnectSecurityScheme with required fields.
   *
   * @param openIdConnectUrl well-known URL to discover the OpenID Connect provider metadata
   */
  public OpenIdConnectSecurityScheme(@NotBlank String openIdConnectUrl) {
    this(openIdConnectUrl, null);
  }

  /**
   * Creates a new OpenIdConnectSecurityScheme with all fields.
   *
   * @param openIdConnectUrl well-known URL to discover the OpenID Connect provider metadata
   * @param description description of this security scheme (may be null)
   */
  public OpenIdConnectSecurityScheme(@NotBlank String openIdConnectUrl, String description) {
    this.openIdConnectUrl =
        Objects.requireNonNull(openIdConnectUrl, "openIdConnectUrl cannot be null").trim();
    if (this.openIdConnectUrl.isEmpty()) {
      throw new IllegalArgumentException("openIdConnectUrl cannot be blank");
    }
    this.description = description;
  }

  /**
   * Gets the security scheme type (always "openIdConnect").
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Gets the well-known URL to discover the OpenID Connect Discovery provider metadata.
   *
   * @return the OpenID Connect URL
   */
  @NotBlank
  public String getOpenIdConnectUrl() {
    return openIdConnectUrl;
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
   * Creates a new OpenIdConnectSecurityScheme with the specified URL.
   *
   * @param openIdConnectUrl the OpenID Connect discovery URL
   * @return a new OpenIdConnectSecurityScheme instance
   */
  public static OpenIdConnectSecurityScheme of(@NotBlank String openIdConnectUrl) {
    return new OpenIdConnectSecurityScheme(openIdConnectUrl);
  }

  /**
   * Creates a builder for constructing OpenIdConnectSecurityScheme instances.
   *
   * @param openIdConnectUrl the required OpenID Connect discovery URL
   * @return a new builder instance
   */
  public static Builder builder(@NotBlank String openIdConnectUrl) {
    return new Builder(openIdConnectUrl);
  }

  /** Builder for constructing OpenIdConnectSecurityScheme instances. */
  public static class Builder {
    private final String openIdConnectUrl;
    private String description;

    private Builder(@NotBlank String openIdConnectUrl) {
      this.openIdConnectUrl =
          Objects.requireNonNull(openIdConnectUrl, "openIdConnectUrl cannot be null").trim();
      if (this.openIdConnectUrl.isEmpty()) {
        throw new IllegalArgumentException("openIdConnectUrl cannot be blank");
      }
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
     * Builds the OpenIdConnectSecurityScheme instance.
     *
     * @return a new OpenIdConnectSecurityScheme
     */
    public OpenIdConnectSecurityScheme build() {
      return new OpenIdConnectSecurityScheme(openIdConnectUrl, description);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OpenIdConnectSecurityScheme that = (OpenIdConnectSecurityScheme) o;
    return Objects.equals(type, that.type)
        && Objects.equals(openIdConnectUrl, that.openIdConnectUrl)
        && Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, openIdConnectUrl, description);
  }

  @Override
  public String toString() {
    return "OpenIdConnectSecurityScheme{"
        + "type='"
        + type
        + '\''
        + ", openIdConnectUrl='"
        + openIdConnectUrl
        + '\''
        + ", description='"
        + description
        + '\''
        + '}';
  }
}
