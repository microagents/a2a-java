package io.microagents.a2a.core.server;

import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides request context information for A2A server method handlers.
 *
 * <p>This class encapsulates authentication information and request-scoped state that can be
 * accessed and modified throughout the request lifecycle.
 *
 * @since 0.1.0
 */
public class ServerCallContext {

  /** Mutable state map for request-scoped data. */
  @NotNull private final Map<String, Object> state;

  /** User authentication information. */
  @NotNull private final User user;

  /**
   * Constructs a new ServerCallContext with the given user. Initializes an empty state map.
   *
   * @param user The authenticated user information
   */
  public ServerCallContext(@NotNull User user) {
    this.user = user;
    this.state = new ConcurrentHashMap<>();
  }

  /**
   * Constructs a new ServerCallContext with the given user and initial state.
   *
   * @param user The authenticated user information
   * @param state Initial state values
   */
  public ServerCallContext(@NotNull User user, @NotNull Map<String, Object> state) {
    this.user = user;
    this.state = new ConcurrentHashMap<>(state);
  }

  /**
   * Gets the mutable state map for storing request-scoped data.
   *
   * @return The state map
   */
  public Map<String, Object> getState() {
    return state;
  }

  /**
   * Gets the authenticated user information.
   *
   * @return The user object
   */
  public User getUser() {
    return user;
  }

  /**
   * Convenience method to get a typed value from the state.
   *
   * @param key The state key
   * @param type The expected value type
   * @param <T> The value type
   * @return Optional containing the value if present and of the correct type
   */
  @SuppressWarnings("unchecked")
  public <T> Optional<T> getStateValue(String key, Class<T> type) {
    Object value = state.get(key);
    if (value != null && type.isInstance(value)) {
      return Optional.of((T) value);
    }
    return Optional.empty();
  }

  /**
   * Convenience method to set a value in the state.
   *
   * @param key The state key
   * @param value The value to set
   */
  public void setStateValue(String key, Object value) {
    if (value == null) {
      state.remove(key);
    } else {
      state.put(key, value);
    }
  }

  /**
   * Creates a ServerCallContext for an unauthenticated request.
   *
   * @return A context with an anonymous user
   */
  public static ServerCallContext anonymous() {
    return new ServerCallContext(AnonymousUser.INSTANCE);
  }

  /**
   * Creates a ServerCallContext for an authenticated request.
   *
   * @param userName The authenticated user's name
   * @return A context with an authenticated user
   */
  public static ServerCallContext authenticated(String userName) {
    return new ServerCallContext(new AuthenticatedUser(userName));
  }
}
