package io.microagents.a2a.core.types.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import java.util.Objects;

/**
 * Allows configuration of the supported OAuth Flows.
 *
 * <p>This contains configuration information for the OAuth 2.0 flow types supported by a security
 * scheme.
 */
public class OAuthFlows {

  @Valid
  @JsonProperty("authorizationCode")
  private final AuthorizationCodeOAuthFlow authorizationCode;

  @Valid
  @JsonProperty("implicit")
  private final ImplicitOAuthFlow implicit;

  @Valid
  @JsonProperty("password")
  private final PasswordOAuthFlow password;

  @Valid
  @JsonProperty("clientCredentials")
  private final ClientCredentialsOAuthFlow clientCredentials;

  /** Creates empty OAuthFlows. */
  public OAuthFlows() {
    this(null, null, null, null);
  }

  /**
   * Creates OAuthFlows with all fields.
   *
   * @param authorizationCode configuration for the OAuth Authorization Code flow
   * @param implicit configuration for the OAuth Implicit flow
   * @param password configuration for the OAuth Resource Owner Password flow
   * @param clientCredentials configuration for the OAuth Client Credentials flow
   */
  public OAuthFlows(
      AuthorizationCodeOAuthFlow authorizationCode,
      ImplicitOAuthFlow implicit,
      PasswordOAuthFlow password,
      ClientCredentialsOAuthFlow clientCredentials) {
    this.authorizationCode = authorizationCode;
    this.implicit = implicit;
    this.password = password;
    this.clientCredentials = clientCredentials;
  }

  /**
   * Gets the configuration for the OAuth Authorization Code flow. Previously called accessCode in
   * OpenAPI 2.0.
   *
   * @return the authorization code flow configuration, or null if not supported
   */
  public AuthorizationCodeOAuthFlow getAuthorizationCode() {
    return authorizationCode;
  }

  /**
   * Gets the configuration for the OAuth Implicit flow.
   *
   * @return the implicit flow configuration, or null if not supported
   */
  public ImplicitOAuthFlow getImplicit() {
    return implicit;
  }

  /**
   * Gets the configuration for the OAuth Resource Owner Password flow.
   *
   * @return the password flow configuration, or null if not supported
   */
  public PasswordOAuthFlow getPassword() {
    return password;
  }

  /**
   * Gets the configuration for the OAuth Client Credentials flow. Previously called application in
   * OpenAPI 2.0.
   *
   * @return the client credentials flow configuration, or null if not supported
   */
  public ClientCredentialsOAuthFlow getClientCredentials() {
    return clientCredentials;
  }

  /**
   * Creates empty OAuthFlows.
   *
   * @return a new OAuthFlows instance with no flows configured
   */
  public static OAuthFlows empty() {
    return new OAuthFlows();
  }

  /**
   * Creates OAuthFlows with authorization code flow only.
   *
   * @param authorizationCode the authorization code flow configuration
   * @return a new OAuthFlows instance
   */
  public static OAuthFlows authorizationCode(AuthorizationCodeOAuthFlow authorizationCode) {
    return new OAuthFlows(authorizationCode, null, null, null);
  }

  /**
   * Creates OAuthFlows with implicit flow only.
   *
   * @param implicit the implicit flow configuration
   * @return a new OAuthFlows instance
   */
  public static OAuthFlows implicit(ImplicitOAuthFlow implicit) {
    return new OAuthFlows(null, implicit, null, null);
  }

  /**
   * Creates OAuthFlows with password flow only.
   *
   * @param password the password flow configuration
   * @return a new OAuthFlows instance
   */
  public static OAuthFlows password(PasswordOAuthFlow password) {
    return new OAuthFlows(null, null, password, null);
  }

  /**
   * Creates OAuthFlows with client credentials flow only.
   *
   * @param clientCredentials the client credentials flow configuration
   * @return a new OAuthFlows instance
   */
  public static OAuthFlows clientCredentials(ClientCredentialsOAuthFlow clientCredentials) {
    return new OAuthFlows(null, null, null, clientCredentials);
  }

  /**
   * Creates a builder for constructing OAuthFlows instances.
   *
   * @return a new builder instance
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for constructing OAuthFlows instances. */
  public static class Builder {
    private AuthorizationCodeOAuthFlow authorizationCode;
    private ImplicitOAuthFlow implicit;
    private PasswordOAuthFlow password;
    private ClientCredentialsOAuthFlow clientCredentials;

    /**
     * Sets the authorization code flow.
     *
     * @param authorizationCode the authorization code flow configuration
     * @return this builder
     */
    public Builder withAuthorizationCode(AuthorizationCodeOAuthFlow authorizationCode) {
      this.authorizationCode = authorizationCode;
      return this;
    }

    /**
     * Sets the implicit flow.
     *
     * @param implicit the implicit flow configuration
     * @return this builder
     */
    public Builder withImplicit(ImplicitOAuthFlow implicit) {
      this.implicit = implicit;
      return this;
    }

    /**
     * Sets the password flow.
     *
     * @param password the password flow configuration
     * @return this builder
     */
    public Builder withPassword(PasswordOAuthFlow password) {
      this.password = password;
      return this;
    }

    /**
     * Sets the client credentials flow.
     *
     * @param clientCredentials the client credentials flow configuration
     * @return this builder
     */
    public Builder withClientCredentials(ClientCredentialsOAuthFlow clientCredentials) {
      this.clientCredentials = clientCredentials;
      return this;
    }

    /**
     * Builds the OAuthFlows instance.
     *
     * @return a new OAuthFlows
     */
    public OAuthFlows build() {
      return new OAuthFlows(authorizationCode, implicit, password, clientCredentials);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OAuthFlows that = (OAuthFlows) o;
    return Objects.equals(authorizationCode, that.authorizationCode)
        && Objects.equals(implicit, that.implicit)
        && Objects.equals(password, that.password)
        && Objects.equals(clientCredentials, that.clientCredentials);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authorizationCode, implicit, password, clientCredentials);
  }

  @Override
  public String toString() {
    return "OAuthFlows{"
        + "authorizationCode="
        + authorizationCode
        + ", implicit="
        + implicit
        + ", password="
        + password
        + ", clientCredentials="
        + clientCredentials
        + '}';
  }
}
