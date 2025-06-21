package io.microagents.a2a.core.server;

/**
 * Abstract interface representing user authentication information.
 *
 * <p>Implementations provide details about whether a request is authenticated and the identity of
 * the authenticated user.
 *
 * @since 0.1.0
 */
public interface User {

  /**
   * Checks if the user is authenticated.
   *
   * @return true if the user is authenticated, false otherwise
   */
  boolean isAuthenticated();

  /**
   * Gets the user's name or identifier.
   *
   * @return The user name, or "anonymous" for unauthenticated users
   */
  String getUserName();
}
