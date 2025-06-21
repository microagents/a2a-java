package io.microagents.a2a.core.types.security;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;

/**
 * Mirrors the OpenAPI Security Scheme Object.
 *
 * <p>This is a union type that can represent any of the supported security schemes: API Key, HTTP
 * Authentication, OAuth 2.0, or OpenID Connect.
 *
 * @see <a href="https://swagger.io/specification/#security-scheme-object">OpenAPI Security Scheme
 *     Object</a>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = APIKeySecurityScheme.class, name = "apiKey"),
  @JsonSubTypes.Type(value = HTTPAuthSecurityScheme.class, name = "http"),
  @JsonSubTypes.Type(value = OAuth2SecurityScheme.class, name = "oauth2"),
  @JsonSubTypes.Type(value = OpenIdConnectSecurityScheme.class, name = "openIdConnect"),
})
public abstract class SecurityScheme {
  /**
   * Gets the type of this security scheme.
   *
   * @return the security scheme type
   */
  public abstract String getType();

  /**
   * Gets the description of this security scheme.
   *
   * @return the description, or null if not specified
   */
  public abstract String getDescription();

  /**
   * Creates an API Key security scheme.
   *
   * @param name the name of the parameter
   * @param in the location of the API key
   * @return a new SecurityScheme instance
   */
  public static SecurityScheme apiKey(@NotNull String name, @NotNull ApiKeyLocation in) {
    return new APIKeySecurityScheme(name, in);
  }

  /**
   * Creates an API Key security scheme for a header.
   *
   * @param headerName the name of the header
   * @return a new SecurityScheme instance
   */
  public static SecurityScheme apiKeyHeader(@NotNull String headerName) {
    return APIKeySecurityScheme.header(headerName);
  }

  /**
   * Creates an API Key security scheme for a query parameter.
   *
   * @param queryName the name of the query parameter
   * @return a new SecurityScheme instance
   */
  public static SecurityScheme apiKeyQuery(@NotNull String queryName) {
    return APIKeySecurityScheme.query(queryName);
  }

  /**
   * Creates an API Key security scheme for a cookie.
   *
   * @param cookieName the name of the cookie
   * @return a new SecurityScheme instance
   */
  public static SecurityScheme apiKeyCookie(@NotNull String cookieName) {
    return APIKeySecurityScheme.cookie(cookieName);
  }

  /**
   * Creates an HTTP authentication security scheme.
   *
   * @param scheme the HTTP authentication scheme
   * @return a new SecurityScheme instance
   */
  public static SecurityScheme http(@NotNull String scheme) {
    return new HTTPAuthSecurityScheme(scheme);
  }

  /**
   * Creates a Basic HTTP authentication security scheme.
   *
   * @return a new SecurityScheme instance
   */
  public static SecurityScheme httpBasic() {
    return HTTPAuthSecurityScheme.basic();
  }

  /**
   * Creates a Bearer token HTTP authentication security scheme.
   *
   * @return a new SecurityScheme instance
   */
  public static SecurityScheme httpBearer() {
    return HTTPAuthSecurityScheme.bearer();
  }

  /**
   * Creates a Bearer token HTTP authentication security scheme with format.
   *
   * @param bearerFormat the bearer token format
   * @return a new SecurityScheme instance
   */
  public static SecurityScheme httpBearer(@NotNull String bearerFormat) {
    return HTTPAuthSecurityScheme.bearer(bearerFormat);
  }

  /**
   * Creates a JWT Bearer token HTTP authentication security scheme.
   *
   * @return a new SecurityScheme instance
   */
  public static SecurityScheme httpJwt() {
    return HTTPAuthSecurityScheme.jwt();
  }

  /**
   * Creates an OAuth 2.0 security scheme.
   *
   * @param flows the OAuth flows configuration
   * @return a new SecurityScheme instance
   */
  public static SecurityScheme oauth2(@NotNull OAuthFlows flows) {
    return new OAuth2SecurityScheme(flows);
  }

  /**
   * Creates an OAuth 2.0 security scheme with authorization code flow.
   *
   * @param authorizationCode the authorization code flow configuration
   * @return a new SecurityScheme instance
   */
  public static SecurityScheme oauth2AuthorizationCode(
      @NotNull AuthorizationCodeOAuthFlow authorizationCode) {
    return OAuth2SecurityScheme.authorizationCode(authorizationCode);
  }

  /**
   * Creates an OAuth 2.0 security scheme with implicit flow.
   *
   * @param implicit the implicit flow configuration
   * @return a new SecurityScheme instance
   */
  public static SecurityScheme oauth2Implicit(@NotNull ImplicitOAuthFlow implicit) {
    return OAuth2SecurityScheme.implicit(implicit);
  }

  /**
   * Creates an OAuth 2.0 security scheme with password flow.
   *
   * @param password the password flow configuration
   * @return a new SecurityScheme instance
   */
  public static SecurityScheme oauth2Password(@NotNull PasswordOAuthFlow password) {
    return OAuth2SecurityScheme.password(password);
  }

  /**
   * Creates an OAuth 2.0 security scheme with client credentials flow.
   *
   * @param clientCredentials the client credentials flow configuration
   * @return a new SecurityScheme instance
   */
  public static SecurityScheme oauth2ClientCredentials(
      @NotNull ClientCredentialsOAuthFlow clientCredentials) {
    return OAuth2SecurityScheme.clientCredentials(clientCredentials);
  }

  /**
   * Creates an OpenID Connect security scheme.
   *
   * @param openIdConnectUrl the OpenID Connect discovery URL
   * @return a new SecurityScheme instance
   */
  public static SecurityScheme openIdConnect(@NotNull String openIdConnectUrl) {
    return new OpenIdConnectSecurityScheme(openIdConnectUrl);
  }

  /**
   * Checks if this is an API Key security scheme.
   *
   * @return true if this is an API Key security scheme
   */
  public boolean isApiKey() {
    return this instanceof APIKeySecurityScheme;
  }

  /**
   * Checks if this is an HTTP authentication security scheme.
   *
   * @return true if this is an HTTP authentication security scheme
   */
  public boolean isHttp() {
    return this instanceof HTTPAuthSecurityScheme;
  }

  /**
   * Checks if this is an OAuth 2.0 security scheme.
   *
   * @return true if this is an OAuth 2.0 security scheme
   */
  public boolean isOAuth2() {
    return this instanceof OAuth2SecurityScheme;
  }

  /**
   * Checks if this is an OpenID Connect security scheme.
   *
   * @return true if this is an OpenID Connect security scheme
   */
  public boolean isOpenIdConnect() {
    return this instanceof OpenIdConnectSecurityScheme;
  }

  /**
   * Casts this security scheme to an API Key security scheme.
   *
   * @return this as an APIKeySecurityScheme
   * @throws ClassCastException if this is not an API Key security scheme
   */
  public APIKeySecurityScheme asApiKey() {
    return (APIKeySecurityScheme) this;
  }

  /**
   * Casts this security scheme to an HTTP authentication security scheme.
   *
   * @return this as an HTTPAuthSecurityScheme
   * @throws ClassCastException if this is not an HTTP authentication security scheme
   */
  public HTTPAuthSecurityScheme asHttp() {
    return (HTTPAuthSecurityScheme) this;
  }

  /**
   * Casts this security scheme to an OAuth 2.0 security scheme.
   *
   * @return this as an OAuth2SecurityScheme
   * @throws ClassCastException if this is not an OAuth 2.0 security scheme
   */
  public OAuth2SecurityScheme asOAuth2() {
    return (OAuth2SecurityScheme) this;
  }

  /**
   * Casts this security scheme to an OpenID Connect security scheme.
   *
   * @return this as an OpenIdConnectSecurityScheme
   * @throws ClassCastException if this is not an OpenID Connect security scheme
   */
  public OpenIdConnectSecurityScheme asOpenIdConnect() {
    return (OpenIdConnectSecurityScheme) this;
  }
}
