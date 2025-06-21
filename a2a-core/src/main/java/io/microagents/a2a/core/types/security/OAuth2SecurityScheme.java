package io.microagents.a2a.core.types.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * OAuth2.0 security scheme configuration.
 *
 * <p>This security scheme represents OAuth 2.0 authentication with support for multiple flow types
 * (authorization code, implicit, password, client credentials).
 */
public class OAuth2SecurityScheme extends SecurityScheme {

  @JsonProperty("type")
  private final String type = "oauth2";

  @NotNull
  @Valid
  @JsonProperty("flows")
  private final OAuthFlows flows;

  @JsonProperty("description")
  private final String description;

  /**
   * Creates a new OAuth2SecurityScheme with required fields.
   *
   * @param flows configuration information for the flow types supported
   */
  public OAuth2SecurityScheme(@NotNull OAuthFlows flows) {
    this(flows, null);
  }

  /**
   * Creates a new OAuth2SecurityScheme with all fields.
   *
   * @param flows configuration information for the flow types supported
   * @param description description of this security scheme (may be null)
   */
  public OAuth2SecurityScheme(@NotNull OAuthFlows flows, String description) {
    this.flows = Objects.requireNonNull(flows, "flows cannot be null");
    this.description = description;
  }

  /**
   * Gets the security scheme type (always "oauth2").
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Gets an object containing configuration information for the flow types supported.
   *
   * @return the OAuth flows configuration
   */
  @NotNull
  public OAuthFlows getFlows() {
    return flows;
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
   * Creates a new OAuth2SecurityScheme with the specified flows.
   *
   * @param flows the OAuth flows configuration
   * @return a new OAuth2SecurityScheme instance
   */
  public static OAuth2SecurityScheme of(@NotNull OAuthFlows flows) {
    return new OAuth2SecurityScheme(flows);
  }

  /**
   * Creates a new OAuth2SecurityScheme with authorization code flow only.
   *
   * @param authorizationCode the authorization code flow configuration
   * @return a new OAuth2SecurityScheme instance
   */
  public static OAuth2SecurityScheme authorizationCode(
      @NotNull AuthorizationCodeOAuthFlow authorizationCode) {
    return new OAuth2SecurityScheme(OAuthFlows.authorizationCode(authorizationCode));
  }

  /**
   * Creates a new OAuth2SecurityScheme with implicit flow only.
   *
   * @param implicit the implicit flow configuration
   * @return a new OAuth2SecurityScheme instance
   */
  public static OAuth2SecurityScheme implicit(@NotNull ImplicitOAuthFlow implicit) {
    return new OAuth2SecurityScheme(OAuthFlows.implicit(implicit));
  }

  /**
   * Creates a new OAuth2SecurityScheme with password flow only.
   *
   * @param password the password flow configuration
   * @return a new OAuth2SecurityScheme instance
   */
  public static OAuth2SecurityScheme password(@NotNull PasswordOAuthFlow password) {
    return new OAuth2SecurityScheme(OAuthFlows.password(password));
  }

  /**
   * Creates a new OAuth2SecurityScheme with client credentials flow only.
   *
   * @param clientCredentials the client credentials flow configuration
   * @return a new OAuth2SecurityScheme instance
   */
  public static OAuth2SecurityScheme clientCredentials(
      @NotNull ClientCredentialsOAuthFlow clientCredentials) {
    return new OAuth2SecurityScheme(OAuthFlows.clientCredentials(clientCredentials));
  }

  /**
   * Creates a builder for constructing OAuth2SecurityScheme instances.
   *
   * @return a new builder instance
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for constructing OAuth2SecurityScheme instances. */
  public static class Builder {
    private final OAuthFlows.Builder flowsBuilder = OAuthFlows.builder();
    private String description;

    /**
     * Sets the authorization code flow.
     *
     * @param authorizationCode the authorization code flow configuration
     * @return this builder
     */
    public Builder withAuthorizationCode(AuthorizationCodeOAuthFlow authorizationCode) {
      this.flowsBuilder.withAuthorizationCode(authorizationCode);
      return this;
    }

    /**
     * Sets the implicit flow.
     *
     * @param implicit the implicit flow configuration
     * @return this builder
     */
    public Builder withImplicit(ImplicitOAuthFlow implicit) {
      this.flowsBuilder.withImplicit(implicit);
      return this;
    }

    /**
     * Sets the password flow.
     *
     * @param password the password flow configuration
     * @return this builder
     */
    public Builder withPassword(PasswordOAuthFlow password) {
      this.flowsBuilder.withPassword(password);
      return this;
    }

    /**
     * Sets the client credentials flow.
     *
     * @param clientCredentials the client credentials flow configuration
     * @return this builder
     */
    public Builder withClientCredentials(ClientCredentialsOAuthFlow clientCredentials) {
      this.flowsBuilder.withClientCredentials(clientCredentials);
      return this;
    }

    /**
     * Sets the flows configuration directly.
     *
     * @param flows the OAuth flows configuration
     * @return this builder
     */
    public Builder withFlows(@NotNull OAuthFlows flows) {
      // Reset the builder and copy flows
      OAuthFlows f = Objects.requireNonNull(flows, "flows cannot be null");
      if (f.getAuthorizationCode() != null) {
        this.flowsBuilder.withAuthorizationCode(f.getAuthorizationCode());
      }
      if (f.getImplicit() != null) {
        this.flowsBuilder.withImplicit(f.getImplicit());
      }
      if (f.getPassword() != null) {
        this.flowsBuilder.withPassword(f.getPassword());
      }
      if (f.getClientCredentials() != null) {
        this.flowsBuilder.withClientCredentials(f.getClientCredentials());
      }
      return this;
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
     * Builds the OAuth2SecurityScheme instance.
     *
     * @return a new OAuth2SecurityScheme
     */
    public OAuth2SecurityScheme build() {
      return new OAuth2SecurityScheme(flowsBuilder.build(), description);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OAuth2SecurityScheme that = (OAuth2SecurityScheme) o;
    return Objects.equals(type, that.type)
        && Objects.equals(flows, that.flows)
        && Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, flows, description);
  }

  @Override
  public String toString() {
    return "OAuth2SecurityScheme{"
        + "type='"
        + type
        + '\''
        + ", flows="
        + flows
        + ", description='"
        + description
        + '\''
        + '}';
  }
}
