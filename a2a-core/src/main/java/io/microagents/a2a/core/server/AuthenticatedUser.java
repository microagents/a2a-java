package io.microagents.a2a.core.server;

import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Implementation of User representing an authenticated user.
 *
 * @since 0.1.0
 */
public final class AuthenticatedUser implements User {

  @NotBlank private final String userName;

  /**
   * Constructs an authenticated user with the given user name.
   *
   * @param userName The authenticated user's name
   * @throws IllegalArgumentException if userName is null or blank
   */
  public AuthenticatedUser(@NotBlank String userName) {
    if (userName == null || userName.isBlank()) {
      throw new IllegalArgumentException("User name cannot be null or blank");
    }
    this.userName = userName;
  }

  @Override
  public boolean isAuthenticated() {
    return true;
  }

  @Override
  public String getUserName() {
    return userName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AuthenticatedUser that = (AuthenticatedUser) o;
    return Objects.equals(userName, that.userName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userName);
  }

  @Override
  public String toString() {
    return "AuthenticatedUser{userName='" + userName + "'}";
  }
}
