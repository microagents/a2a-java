package io.microagents.a2a.core.types.security;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The location of the API key. Valid values are "query", "header", or "cookie".
 *
 * <p>This enum specifies where the API key should be placed in the HTTP request.
 */
public enum ApiKeyLocation {

  /** API key is passed as a query parameter. */
  QUERY("query"),

  /** API key is passed in an HTTP header. */
  HEADER("header"),

  /** API key is passed as a cookie. */
  COOKIE("cookie");

  private final String value;

  ApiKeyLocation(String value) {
    this.value = value;
  }

  /**
   * Gets the JSON representation of this location.
   *
   * @return the location value as it appears in JSON
   */
  @JsonValue
  public String getValue() {
    return value;
  }

  /**
   * Creates an ApiKeyLocation from its string value.
   *
   * @param value the string value ("query", "header", or "cookie")
   * @return the corresponding ApiKeyLocation
   * @throws IllegalArgumentException if the value is not recognized
   */
  public static ApiKeyLocation fromValue(String value) {
    for (ApiKeyLocation location : values()) {
      if (location.value.equals(value)) {
        return location;
      }
    }
    throw new IllegalArgumentException("Unknown API key location: " + value);
  }

  @Override
  public String toString() {
    return value;
  }
}
