package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonValue;
import io.microagents.a2a.core.types.notifications.*;

/**
 * Represents the role of a message sender in A2A communication.
 *
 * <p>The role indicates whether a message originates from the client (user) or from the agent
 * (server) in an A2A conversation.
 *
 * @see Message
 * @since 0.1.0
 */
public enum Role {

  /**
   * Message from the A2A Client (user) to the A2A Server (agent). This represents input, requests,
   * or responses from the client side.
   */
  USER("user"),

  /**
   * Message from the A2A Server (agent) to the A2A Client. This represents responses, status
   * updates, or notifications from the agent side.
   */
  AGENT("agent");

  private final String value;

  Role(String value) {
    this.value = value;
  }

  /**
   * Gets the string representation used in JSON serialization.
   *
   * @return the JSON value for this role
   */
  @JsonValue
  public String getValue() {
    return value;
  }

  /**
   * Creates a Role from its string value.
   *
   * @param value the string value ("user" or "agent")
   * @return the corresponding Role
   * @throws IllegalArgumentException if the value is not recognized
   */
  public static Role fromValue(String value) {
    for (Role role : values()) {
      if (role.value.equals(value)) {
        return role;
      }
    }
    throw new IllegalArgumentException("Unknown Role value: " + value);
  }

  @Override
  public String toString() {
    return value;
  }
}
