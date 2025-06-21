package io.microagents.a2a.core.types.notifications;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * Defines authentication details for push notifications.
 *
 * <p>This class specifies how authentication should be handled when the agent sends push
 * notifications to the client's callback URL.
 */
@Schema(description = "Defines authentication details for push notifications")
public class PushNotificationAuthenticationInfo {

  /** Supported authentication schemes - e.g. Basic, Bearer */
  @JsonProperty("schemes")
  @NotNull
  @Schema(
      description = "Supported authentication schemes - e.g. Basic, Bearer",
      example = "[\"Bearer\", \"Basic\"]",
      required = true)
  private List<String> schemes;

  /** Optional credentials */
  @JsonProperty("credentials")
  @Schema(description = "Optional credentials")
  private String credentials;

  /** Default constructor. */
  public PushNotificationAuthenticationInfo() {}

  /**
   * Constructor with schemes.
   *
   * @param schemes the supported authentication schemes
   */
  public PushNotificationAuthenticationInfo(List<String> schemes) {
    this.schemes = schemes;
  }

  /**
   * Full constructor.
   *
   * @param schemes the supported authentication schemes
   * @param credentials optional credentials
   */
  public PushNotificationAuthenticationInfo(List<String> schemes, String credentials) {
    this.schemes = schemes;
    this.credentials = credentials;
  }

  /**
   * Gets the supported schemes.
   *
   * @return the list of supported authentication schemes
   */
  public List<String> getSchemes() {
    return schemes;
  }

  /**
   * Sets the supported schemes.
   *
   * @param schemes the supported authentication schemes
   */
  public void setSchemes(List<String> schemes) {
    this.schemes = schemes;
  }

  /**
   * Gets the credentials.
   *
   * @return the credentials, may be null
   */
  public String getCredentials() {
    return credentials;
  }

  /**
   * Sets the credentials.
   *
   * @param credentials the credentials
   */
  public void setCredentials(String credentials) {
    this.credentials = credentials;
  }

  /**
   * Creates new authentication info with the specified schemes.
   *
   * @param schemes the supported authentication schemes
   * @return a new PushNotificationAuthenticationInfo
   */
  public static PushNotificationAuthenticationInfo of(List<String> schemes) {
    return new PushNotificationAuthenticationInfo(schemes);
  }

  /**
   * Creates a copy of this authentication info with new schemes.
   *
   * @param newSchemes the new schemes
   * @return a new authentication info with the specified schemes
   */
  public PushNotificationAuthenticationInfo withSchemes(List<String> newSchemes) {
    return new PushNotificationAuthenticationInfo(newSchemes, this.credentials);
  }

  /**
   * Creates a copy of this authentication info with new credentials.
   *
   * @param newCredentials the new credentials
   * @return a new authentication info with the specified credentials
   */
  public PushNotificationAuthenticationInfo withCredentials(String newCredentials) {
    return new PushNotificationAuthenticationInfo(this.schemes, newCredentials);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    PushNotificationAuthenticationInfo that = (PushNotificationAuthenticationInfo) obj;
    return Objects.equals(schemes, that.schemes) && Objects.equals(credentials, that.credentials);
  }

  @Override
  public int hashCode() {
    return Objects.hash(schemes, credentials);
  }

  @Override
  public String toString() {
    return "PushNotificationAuthenticationInfo{"
        + "schemes="
        + schemes
        + ", credentials='"
        + credentials
        + '\''
        + '}';
  }
}
