package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.notifications.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Represents the service provider of an agent.
 *
 * <p>This contains information about the organization or entity that provides and operates the
 * agent service.
 */
public class AgentProvider {

  @NotBlank
  @JsonProperty("organization")
  private final String organization;

  @NotBlank
  @JsonProperty("url")
  private final String url;

  /**
   * Creates a new AgentProvider.
   *
   * @param organization agent provider's organization name
   * @param url agent provider's URL
   */
  public AgentProvider(@NotBlank String organization, @NotBlank String url) {
    this.organization = Objects.requireNonNull(organization, "organization cannot be null").trim();
    if (this.organization.isEmpty()) {
      throw new IllegalArgumentException("organization cannot be blank");
    }
    this.url = Objects.requireNonNull(url, "url cannot be null").trim();
    if (this.url.isEmpty()) {
      throw new IllegalArgumentException("url cannot be blank");
    }
  }

  /**
   * Gets the agent provider's organization name.
   *
   * @return the organization name
   */
  @NotBlank
  public String getOrganization() {
    return organization;
  }

  /**
   * Gets the agent provider's URL.
   *
   * @return the provider URL
   */
  @NotBlank
  public String getUrl() {
    return url;
  }

  /**
   * Creates a new AgentProvider.
   *
   * @param organization agent provider's organization name
   * @param url agent provider's URL
   * @return a new AgentProvider instance
   */
  public static AgentProvider of(@NotBlank String organization, @NotBlank String url) {
    return new AgentProvider(organization, url);
  }

  /**
   * Creates a builder for constructing AgentProvider instances.
   *
   * @param organization the required organization name
   * @return a new builder instance
   */
  public static Builder builder(@NotBlank String organization) {
    return new Builder(organization);
  }

  /** Builder for constructing AgentProvider instances. */
  public static class Builder {
    private final String organization;
    private String url;

    private Builder(@NotBlank String organization) {
      this.organization =
          Objects.requireNonNull(organization, "organization cannot be null").trim();
      if (this.organization.isEmpty()) {
        throw new IllegalArgumentException("organization cannot be blank");
      }
    }

    /**
     * Sets the provider URL.
     *
     * @param url the provider URL
     * @return this builder
     */
    public Builder withUrl(@NotBlank String url) {
      this.url = Objects.requireNonNull(url, "url cannot be null").trim();
      if (this.url.isEmpty()) {
        throw new IllegalArgumentException("url cannot be blank");
      }
      return this;
    }

    /**
     * Builds the AgentProvider instance.
     *
     * @return a new AgentProvider
     * @throws IllegalStateException if url is not set
     */
    public AgentProvider build() {
      if (url == null) {
        throw new IllegalStateException("url must be set");
      }
      return new AgentProvider(organization, url);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AgentProvider that = (AgentProvider) o;
    return Objects.equals(organization, that.organization) && Objects.equals(url, that.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(organization, url);
  }

  @Override
  public String toString() {
    return "AgentProvider{" + "organization='" + organization + '\'' + ", url='" + url + '\'' + '}';
  }
}
