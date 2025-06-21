package io.microagents.a2a.core.types.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Configuration details for a supported OAuth Authorization Code Flow.
 *
 * <p>Previously called accessCode in OpenAPI 2.0. This flow requires authorization URL, token URL,
 * and scopes configuration.
 */
public class AuthorizationCodeOAuthFlow {

  @NotBlank
  @JsonProperty("authorizationUrl")
  private final String authorizationUrl;

  @NotBlank
  @JsonProperty("tokenUrl")
  private final String tokenUrl;

  @JsonProperty("refreshUrl")
  private final String refreshUrl;

  @NotNull
  @JsonProperty("scopes")
  private final Map<String, String> scopes;

  /**
   * Creates a new AuthorizationCodeOAuthFlow with required fields.
   *
   * @param authorizationUrl the authorization URL for this flow
   * @param tokenUrl the token URL for this flow
   * @param scopes the available scopes for the OAuth2 security scheme
   */
  public AuthorizationCodeOAuthFlow(
      @NotBlank String authorizationUrl,
      @NotBlank String tokenUrl,
      @NotNull Map<String, String> scopes) {
    this(authorizationUrl, tokenUrl, null, scopes);
  }

  /**
   * Creates a new AuthorizationCodeOAuthFlow with all fields.
   *
   * @param authorizationUrl the authorization URL for this flow
   * @param tokenUrl the token URL for this flow
   * @param refreshUrl the URL to be used for obtaining refresh tokens (may be null)
   * @param scopes the available scopes for the OAuth2 security scheme
   */
  public AuthorizationCodeOAuthFlow(
      @NotBlank String authorizationUrl,
      @NotBlank String tokenUrl,
      String refreshUrl,
      @NotNull Map<String, String> scopes) {
    this.authorizationUrl =
        Objects.requireNonNull(authorizationUrl, "authorizationUrl cannot be null").trim();
    if (this.authorizationUrl.isEmpty()) {
      throw new IllegalArgumentException("authorizationUrl cannot be blank");
    }
    this.tokenUrl = Objects.requireNonNull(tokenUrl, "tokenUrl cannot be null").trim();
    if (this.tokenUrl.isEmpty()) {
      throw new IllegalArgumentException("tokenUrl cannot be blank");
    }
    this.refreshUrl = refreshUrl;
    this.scopes =
        Collections.unmodifiableMap(
            new HashMap<>(Objects.requireNonNull(scopes, "scopes cannot be null")));
  }

  /**
   * Gets the authorization URL to be used for this flow. This MUST be in the form of a URL. The
   * OAuth2 standard requires the use of TLS.
   *
   * @return the authorization URL
   */
  @NotBlank
  public String getAuthorizationUrl() {
    return authorizationUrl;
  }

  /**
   * Gets the token URL to be used for this flow. This MUST be in the form of a URL. The OAuth2
   * standard requires the use of TLS.
   *
   * @return the token URL
   */
  @NotBlank
  public String getTokenUrl() {
    return tokenUrl;
  }

  /**
   * Gets the URL to be used for obtaining refresh tokens. This MUST be in the form of a URL. The
   * OAuth2 standard requires the use of TLS.
   *
   * @return the refresh URL, or null if not specified
   */
  public String getRefreshUrl() {
    return refreshUrl;
  }

  /**
   * Gets the available scopes for the OAuth2 security scheme. A map between the scope name and a
   * short description for it. The map MAY be empty.
   *
   * @return unmodifiable map of scopes
   */
  @NotNull
  public Map<String, String> getScopes() {
    return scopes;
  }

  /**
   * Creates a new AuthorizationCodeOAuthFlow with required fields.
   *
   * @param authorizationUrl the authorization URL
   * @param tokenUrl the token URL
   * @param scopes the scopes map
   * @return a new AuthorizationCodeOAuthFlow instance
   */
  public static AuthorizationCodeOAuthFlow of(
      @NotBlank String authorizationUrl,
      @NotBlank String tokenUrl,
      @NotNull Map<String, String> scopes) {
    return new AuthorizationCodeOAuthFlow(authorizationUrl, tokenUrl, scopes);
  }

  /**
   * Creates a builder for constructing AuthorizationCodeOAuthFlow instances.
   *
   * @param authorizationUrl the required authorization URL
   * @param tokenUrl the required token URL
   * @return a new builder instance
   */
  public static Builder builder(@NotBlank String authorizationUrl, @NotBlank String tokenUrl) {
    return new Builder(authorizationUrl, tokenUrl);
  }

  /** Builder for constructing AuthorizationCodeOAuthFlow instances. */
  public static class Builder {
    private final String authorizationUrl;
    private final String tokenUrl;
    private String refreshUrl;
    private final Map<String, String> scopes = new HashMap<>();

    private Builder(@NotBlank String authorizationUrl, @NotBlank String tokenUrl) {
      this.authorizationUrl =
          Objects.requireNonNull(authorizationUrl, "authorizationUrl cannot be null").trim();
      if (this.authorizationUrl.isEmpty()) {
        throw new IllegalArgumentException("authorizationUrl cannot be blank");
      }
      this.tokenUrl = Objects.requireNonNull(tokenUrl, "tokenUrl cannot be null").trim();
      if (this.tokenUrl.isEmpty()) {
        throw new IllegalArgumentException("tokenUrl cannot be blank");
      }
    }

    /**
     * Sets the refresh URL.
     *
     * @param refreshUrl the refresh URL
     * @return this builder
     */
    public Builder withRefreshUrl(String refreshUrl) {
      this.refreshUrl = refreshUrl;
      return this;
    }

    /**
     * Adds a scope.
     *
     * @param scope the scope name
     * @param description the scope description
     * @return this builder
     */
    public Builder addScope(@NotBlank String scope, @NotBlank String description) {
      Objects.requireNonNull(scope, "scope cannot be null");
      Objects.requireNonNull(description, "description cannot be null");
      String trimmedScope = scope.trim();
      String trimmedDescription = description.trim();
      if (trimmedScope.isEmpty()) {
        throw new IllegalArgumentException("scope cannot be blank");
      }
      if (trimmedDescription.isEmpty()) {
        throw new IllegalArgumentException("description cannot be blank");
      }
      this.scopes.put(trimmedScope, trimmedDescription);
      return this;
    }

    /**
     * Adds multiple scopes.
     *
     * @param scopes the scopes map
     * @return this builder
     */
    public Builder addScopes(@NotNull Map<String, String> scopes) {
      Objects.requireNonNull(scopes, "scopes cannot be null");
      scopes.forEach((scope, description) -> addScope(scope, description));
      return this;
    }

    /**
     * Builds the AuthorizationCodeOAuthFlow instance.
     *
     * @return a new AuthorizationCodeOAuthFlow
     */
    public AuthorizationCodeOAuthFlow build() {
      return new AuthorizationCodeOAuthFlow(authorizationUrl, tokenUrl, refreshUrl, scopes);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AuthorizationCodeOAuthFlow that = (AuthorizationCodeOAuthFlow) o;
    return Objects.equals(authorizationUrl, that.authorizationUrl)
        && Objects.equals(tokenUrl, that.tokenUrl)
        && Objects.equals(refreshUrl, that.refreshUrl)
        && Objects.equals(scopes, that.scopes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authorizationUrl, tokenUrl, refreshUrl, scopes);
  }

  @Override
  public String toString() {
    return "AuthorizationCodeOAuthFlow{"
        + "authorizationUrl='"
        + authorizationUrl
        + '\''
        + ", tokenUrl='"
        + tokenUrl
        + '\''
        + ", refreshUrl='"
        + refreshUrl
        + '\''
        + ", scopes="
        + scopes
        + '}';
  }
}
