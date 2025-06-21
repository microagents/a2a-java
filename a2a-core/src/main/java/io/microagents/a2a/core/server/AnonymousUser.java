package io.microagents.a2a.core.server;

/**
 * Implementation of User representing an unauthenticated/anonymous user.
 *
 * @since 0.1.0
 */
public final class AnonymousUser implements User {

  /** Singleton instance of anonymous user. */
  public static final AnonymousUser INSTANCE = new AnonymousUser();

  private AnonymousUser() {
    // Private constructor for singleton
  }

  @Override
  public boolean isAuthenticated() {
    return false;
  }

  @Override
  public String getUserName() {
    return "anonymous";
  }

  @Override
  public String toString() {
    return "AnonymousUser";
  }
}
