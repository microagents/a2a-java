package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.notifications.*;
import io.microagents.a2a.core.types.security.SecurityScheme;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.*;

/**
 * An AgentCard conveys key information: - Overall details (version, name, description, uses) -
 * Skills: A set of capabilities the agent can perform - Default modalities/content types supported
 * by the agent. - Authentication requirements
 *
 * <p>The AgentCard is used for agent discovery and capability advertisement in the A2A protocol
 * ecosystem.
 */
public class AgentCard {

  @NotBlank
  @JsonProperty("name")
  private final String name;

  @NotBlank
  @JsonProperty("version")
  private final String version;

  @NotBlank
  @JsonProperty("description")
  private final String description;

  @NotBlank
  @JsonProperty("url")
  private final String url;

  @NotEmpty
  @JsonProperty("defaultInputModes")
  private final List<String> defaultInputModes;

  @NotEmpty
  @JsonProperty("defaultOutputModes")
  private final List<String> defaultOutputModes;

  @NotEmpty
  @Valid
  @JsonProperty("skills")
  private final List<AgentSkill> skills;

  @NotNull
  @Valid
  @JsonProperty("capabilities")
  private final AgentCapabilities capabilities;

  @Valid
  @JsonProperty("provider")
  private final AgentProvider provider;

  @JsonProperty("documentationUrl")
  private final String documentationUrl;

  @JsonProperty("iconUrl")
  private final String iconUrl;

  @JsonProperty("preferredTransport")
  private final String preferredTransport;

  @Valid
  @JsonProperty("additionalInterfaces")
  private final List<AgentInterface> additionalInterfaces;

  @JsonProperty("supportsAuthenticatedExtendedCard")
  private final Boolean supportsAuthenticatedExtendedCard;

  @JsonProperty("security")
  private final List<Map<String, List<String>>> security;

  @Valid
  @JsonProperty("securitySchemes")
  private final Map<String, SecurityScheme> securitySchemes;

  /**
   * Creates a new AgentCard with required fields.
   *
   * @param name human readable name of the agent
   * @param version the version of the agent
   * @param description description of the agent
   * @param url URL where the agent is hosted
   * @param defaultInputModes default supported input modes
   * @param defaultOutputModes default supported output modes
   * @param skills skills the agent can perform
   * @param capabilities optional capabilities supported by the agent
   */
  public AgentCard(
      @NotBlank String name,
      @NotBlank String version,
      @NotBlank String description,
      @NotBlank String url,
      @NotEmpty List<String> defaultInputModes,
      @NotEmpty List<String> defaultOutputModes,
      @NotEmpty List<AgentSkill> skills,
      @NotNull AgentCapabilities capabilities) {
    this(
        name,
        version,
        description,
        url,
        defaultInputModes,
        defaultOutputModes,
        skills,
        capabilities,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null);
  }

  /** Creates a new AgentCard with all fields. */
  public AgentCard(
      @NotBlank String name,
      @NotBlank String version,
      @NotBlank String description,
      @NotBlank String url,
      @NotEmpty List<String> defaultInputModes,
      @NotEmpty List<String> defaultOutputModes,
      @NotEmpty List<AgentSkill> skills,
      @NotNull AgentCapabilities capabilities,
      AgentProvider provider,
      String documentationUrl,
      String iconUrl,
      String preferredTransport,
      List<AgentInterface> additionalInterfaces,
      Boolean supportsAuthenticatedExtendedCard,
      List<Map<String, List<String>>> security,
      Map<String, SecurityScheme> securitySchemes) {
    this.name = Objects.requireNonNull(name, "name cannot be null").trim();
    if (this.name.isEmpty()) {
      throw new IllegalArgumentException("name cannot be blank");
    }
    this.version = Objects.requireNonNull(version, "version cannot be null").trim();
    if (this.version.isEmpty()) {
      throw new IllegalArgumentException("version cannot be blank");
    }
    this.description = Objects.requireNonNull(description, "description cannot be null").trim();
    if (this.description.isEmpty()) {
      throw new IllegalArgumentException("description cannot be blank");
    }
    this.url = Objects.requireNonNull(url, "url cannot be null").trim();
    if (this.url.isEmpty()) {
      throw new IllegalArgumentException("url cannot be blank");
    }
    this.defaultInputModes =
        Collections.unmodifiableList(
            new ArrayList<>(
                Objects.requireNonNull(defaultInputModes, "defaultInputModes cannot be null")));
    if (this.defaultInputModes.isEmpty()) {
      throw new IllegalArgumentException("defaultInputModes cannot be empty");
    }
    this.defaultOutputModes =
        Collections.unmodifiableList(
            new ArrayList<>(
                Objects.requireNonNull(defaultOutputModes, "defaultOutputModes cannot be null")));
    if (this.defaultOutputModes.isEmpty()) {
      throw new IllegalArgumentException("defaultOutputModes cannot be empty");
    }
    this.skills =
        Collections.unmodifiableList(
            new ArrayList<>(Objects.requireNonNull(skills, "skills cannot be null")));
    if (this.skills.isEmpty()) {
      throw new IllegalArgumentException("skills cannot be empty");
    }
    this.capabilities = Objects.requireNonNull(capabilities, "capabilities cannot be null");
    this.provider = provider;
    this.documentationUrl = documentationUrl;
    this.iconUrl = iconUrl;
    this.preferredTransport = preferredTransport;
    this.additionalInterfaces =
        additionalInterfaces == null
            ? null
            : Collections.unmodifiableList(new ArrayList<>(additionalInterfaces));
    this.supportsAuthenticatedExtendedCard = supportsAuthenticatedExtendedCard;
    this.security =
        security == null ? null : Collections.unmodifiableList(new ArrayList<>(security));
    this.securitySchemes =
        securitySchemes == null
            ? null
            : Collections.unmodifiableMap(new HashMap<>(securitySchemes));
  }

  /**
   * Gets the human readable name of the agent.
   *
   * @return the agent name
   */
  @NotBlank
  public String getName() {
    return name;
  }

  /**
   * Gets the version of the agent. The format is up to the provider.
   *
   * @return the agent version
   */
  @NotBlank
  public String getVersion() {
    return version;
  }

  /**
   * Gets the human-readable description of the agent. Used to assist users and other agents in
   * understanding what the agent can do.
   *
   * @return the agent description
   */
  @NotBlank
  public String getDescription() {
    return description;
  }

  /**
   * Gets the URL where the agent is hosted. This represents the preferred endpoint as declared by
   * the agent.
   *
   * @return the agent URL
   */
  @NotBlank
  public String getUrl() {
    return url;
  }

  /**
   * Gets the set of interaction modes that the agent supports across all skills. This can be
   * overridden per-skill. Supported media types for input.
   *
   * @return unmodifiable list of default input modes
   */
  @NotEmpty
  public List<String> getDefaultInputModes() {
    return defaultInputModes;
  }

  /**
   * Gets the supported media types for output.
   *
   * @return unmodifiable list of default output modes
   */
  @NotEmpty
  public List<String> getDefaultOutputModes() {
    return defaultOutputModes;
  }

  /**
   * Gets the skills that are a unit of capability that an agent can perform.
   *
   * @return unmodifiable list of agent skills
   */
  @NotEmpty
  public List<AgentSkill> getSkills() {
    return skills;
  }

  /**
   * Gets the optional capabilities supported by the agent.
   *
   * @return the agent capabilities
   */
  @NotNull
  public AgentCapabilities getCapabilities() {
    return capabilities;
  }

  /**
   * Gets the service provider of the agent.
   *
   * @return the agent provider, or null if not specified
   */
  public AgentProvider getProvider() {
    return provider;
  }

  /**
   * Gets the URL to documentation for the agent.
   *
   * @return the documentation URL, or null if not specified
   */
  public String getDocumentationUrl() {
    return documentationUrl;
  }

  /**
   * Gets the URL to an icon for the agent.
   *
   * @return the icon URL, or null if not specified
   */
  public String getIconUrl() {
    return iconUrl;
  }

  /**
   * Gets the transport of the preferred endpoint. If empty, defaults to JSONRPC.
   *
   * @return the preferred transport, or null if not specified
   */
  public String getPreferredTransport() {
    return preferredTransport;
  }

  /**
   * Gets announcement of additional supported transports. Client can use any of the supported
   * transports.
   *
   * @return unmodifiable list of additional interfaces, or null if none
   */
  public List<AgentInterface> getAdditionalInterfaces() {
    return additionalInterfaces;
  }

  /**
   * Gets whether the agent supports providing an extended agent card when the user is
   * authenticated. Defaults to false if not specified.
   *
   * @return true if supported, false if not, null if not specified
   */
  public Boolean getSupportsAuthenticatedExtendedCard() {
    return supportsAuthenticatedExtendedCard;
  }

  /**
   * Checks if the agent supports authenticated extended cards.
   *
   * @return true if supported, false otherwise
   */
  public boolean isAuthenticatedExtendedCardSupported() {
    return Boolean.TRUE.equals(supportsAuthenticatedExtendedCard);
  }

  /**
   * Gets the security requirements for contacting the agent.
   *
   * @return unmodifiable list of security requirements, or null if none
   */
  public List<Map<String, List<String>>> getSecurity() {
    return security;
  }

  /**
   * Gets the security scheme details used for authenticating with this agent.
   *
   * @return unmodifiable map of security schemes, or null if none
   */
  public Map<String, SecurityScheme> getSecuritySchemes() {
    return securitySchemes;
  }

  /**
   * Creates a builder for constructing AgentCard instances.
   *
   * @param name the required agent name
   * @return a new builder instance
   */
  public static Builder builder(@NotBlank String name) {
    return new Builder(name);
  }

  /** Builder for constructing AgentCard instances. */
  public static class Builder {
    private final String name;
    private String version;
    private String description;
    private String url;
    private final List<String> defaultInputModes = new ArrayList<>();
    private final List<String> defaultOutputModes = new ArrayList<>();
    private final List<AgentSkill> skills = new ArrayList<>();
    private AgentCapabilities capabilities;
    private AgentProvider provider;
    private String documentationUrl;
    private String iconUrl;
    private String preferredTransport;
    private final List<AgentInterface> additionalInterfaces = new ArrayList<>();
    private Boolean supportsAuthenticatedExtendedCard;
    private final List<Map<String, List<String>>> security = new ArrayList<>();
    private final Map<String, SecurityScheme> securitySchemes = new HashMap<>();

    private Builder(@NotBlank String name) {
      this.name = Objects.requireNonNull(name, "name cannot be null").trim();
      if (this.name.isEmpty()) {
        throw new IllegalArgumentException("name cannot be blank");
      }
    }

    public Builder withVersion(@NotBlank String version) {
      this.version = Objects.requireNonNull(version, "version cannot be null").trim();
      if (this.version.isEmpty()) {
        throw new IllegalArgumentException("version cannot be blank");
      }
      return this;
    }

    public Builder withDescription(@NotBlank String description) {
      this.description = Objects.requireNonNull(description, "description cannot be null").trim();
      if (this.description.isEmpty()) {
        throw new IllegalArgumentException("description cannot be blank");
      }
      return this;
    }

    public Builder withUrl(@NotBlank String url) {
      this.url = Objects.requireNonNull(url, "url cannot be null").trim();
      if (this.url.isEmpty()) {
        throw new IllegalArgumentException("url cannot be blank");
      }
      return this;
    }

    public Builder addDefaultInputMode(@NotBlank String inputMode) {
      Objects.requireNonNull(inputMode, "inputMode cannot be null");
      String trimmed = inputMode.trim();
      if (trimmed.isEmpty()) {
        throw new IllegalArgumentException("inputMode cannot be blank");
      }
      this.defaultInputModes.add(trimmed);
      return this;
    }

    public Builder addDefaultInputModes(@NotNull Collection<String> inputModes) {
      Objects.requireNonNull(inputModes, "inputModes cannot be null");
      inputModes.forEach(mode -> addDefaultInputMode(mode));
      return this;
    }

    public Builder addDefaultOutputMode(@NotBlank String outputMode) {
      Objects.requireNonNull(outputMode, "outputMode cannot be null");
      String trimmed = outputMode.trim();
      if (trimmed.isEmpty()) {
        throw new IllegalArgumentException("outputMode cannot be blank");
      }
      this.defaultOutputModes.add(trimmed);
      return this;
    }

    public Builder addDefaultOutputModes(@NotNull Collection<String> outputModes) {
      Objects.requireNonNull(outputModes, "outputModes cannot be null");
      outputModes.forEach(mode -> addDefaultOutputMode(mode));
      return this;
    }

    public Builder addSkill(@NotNull AgentSkill skill) {
      this.skills.add(Objects.requireNonNull(skill, "skill cannot be null"));
      return this;
    }

    public Builder addSkills(@NotNull Collection<AgentSkill> skills) {
      Objects.requireNonNull(skills, "skills cannot be null");
      skills.forEach(skill -> addSkill(skill));
      return this;
    }

    public Builder withCapabilities(@NotNull AgentCapabilities capabilities) {
      this.capabilities = Objects.requireNonNull(capabilities, "capabilities cannot be null");
      return this;
    }

    public Builder withProvider(@NotNull AgentProvider provider) {
      this.provider = Objects.requireNonNull(provider, "provider cannot be null");
      return this;
    }

    public Builder withDocumentationUrl(String documentationUrl) {
      this.documentationUrl = documentationUrl;
      return this;
    }

    public Builder withIconUrl(String iconUrl) {
      this.iconUrl = iconUrl;
      return this;
    }

    public Builder withPreferredTransport(String preferredTransport) {
      this.preferredTransport = preferredTransport;
      return this;
    }

    public Builder addAdditionalInterface(@NotNull AgentInterface agentInterface) {
      this.additionalInterfaces.add(
          Objects.requireNonNull(agentInterface, "agentInterface cannot be null"));
      return this;
    }

    public Builder addAdditionalInterfaces(@NotNull Collection<AgentInterface> interfaces) {
      Objects.requireNonNull(interfaces, "interfaces cannot be null");
      interfaces.forEach(iface -> addAdditionalInterface(iface));
      return this;
    }

    public Builder withSupportsAuthenticatedExtendedCard(boolean supports) {
      this.supportsAuthenticatedExtendedCard = supports;
      return this;
    }

    /**
     * Adds a security scheme.
     *
     * @param name the security scheme name
     * @param scheme the security scheme
     * @return this builder
     */
    public Builder addSecurityScheme(@NotBlank String name, @NotNull SecurityScheme scheme) {
      Objects.requireNonNull(name, "name cannot be null");
      Objects.requireNonNull(scheme, "scheme cannot be null");
      String trimmedName = name.trim();
      if (trimmedName.isEmpty()) {
        throw new IllegalArgumentException("name cannot be blank");
      }
      this.securitySchemes.put(trimmedName, scheme);
      return this;
    }

    /**
     * Adds multiple security schemes.
     *
     * @param schemes the security schemes map
     * @return this builder
     */
    public Builder addSecuritySchemes(@NotNull Map<String, SecurityScheme> schemes) {
      Objects.requireNonNull(schemes, "schemes cannot be null");
      schemes.forEach((name, scheme) -> addSecurityScheme(name, scheme));
      return this;
    }

    public AgentCard build() {
      if (version == null) {
        throw new IllegalStateException("version must be set");
      }
      if (description == null) {
        throw new IllegalStateException("description must be set");
      }
      if (url == null) {
        throw new IllegalStateException("url must be set");
      }
      if (defaultInputModes.isEmpty()) {
        throw new IllegalStateException("At least one default input mode must be added");
      }
      if (defaultOutputModes.isEmpty()) {
        throw new IllegalStateException("At least one default output mode must be added");
      }
      if (skills.isEmpty()) {
        throw new IllegalStateException("At least one skill must be added");
      }
      if (capabilities == null) {
        throw new IllegalStateException("capabilities must be set");
      }
      return new AgentCard(
          name,
          version,
          description,
          url,
          defaultInputModes,
          defaultOutputModes,
          skills,
          capabilities,
          provider,
          documentationUrl,
          iconUrl,
          preferredTransport,
          additionalInterfaces.isEmpty() ? null : additionalInterfaces,
          supportsAuthenticatedExtendedCard,
          security.isEmpty() ? null : security,
          securitySchemes.isEmpty() ? null : securitySchemes);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AgentCard agentCard = (AgentCard) o;
    return Objects.equals(name, agentCard.name)
        && Objects.equals(version, agentCard.version)
        && Objects.equals(description, agentCard.description)
        && Objects.equals(url, agentCard.url)
        && Objects.equals(defaultInputModes, agentCard.defaultInputModes)
        && Objects.equals(defaultOutputModes, agentCard.defaultOutputModes)
        && Objects.equals(skills, agentCard.skills)
        && Objects.equals(capabilities, agentCard.capabilities)
        && Objects.equals(provider, agentCard.provider)
        && Objects.equals(documentationUrl, agentCard.documentationUrl)
        && Objects.equals(iconUrl, agentCard.iconUrl)
        && Objects.equals(preferredTransport, agentCard.preferredTransport)
        && Objects.equals(additionalInterfaces, agentCard.additionalInterfaces)
        && Objects.equals(
            supportsAuthenticatedExtendedCard, agentCard.supportsAuthenticatedExtendedCard)
        && Objects.equals(security, agentCard.security)
        && Objects.equals(securitySchemes, agentCard.securitySchemes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        name,
        version,
        description,
        url,
        defaultInputModes,
        defaultOutputModes,
        skills,
        capabilities,
        provider,
        documentationUrl,
        iconUrl,
        preferredTransport,
        additionalInterfaces,
        supportsAuthenticatedExtendedCard,
        security,
        securitySchemes);
  }

  @Override
  public String toString() {
    return "AgentCard{"
        + "name='"
        + name
        + '\''
        + ", version='"
        + version
        + '\''
        + ", description='"
        + description
        + '\''
        + ", url='"
        + url
        + '\''
        + ", defaultInputModes="
        + defaultInputModes
        + ", defaultOutputModes="
        + defaultOutputModes
        + ", skills="
        + skills
        + ", capabilities="
        + capabilities
        + ", provider="
        + provider
        + ", documentationUrl='"
        + documentationUrl
        + '\''
        + ", iconUrl='"
        + iconUrl
        + '\''
        + ", preferredTransport='"
        + preferredTransport
        + '\''
        + ", additionalInterfaces="
        + additionalInterfaces
        + ", supportsAuthenticatedExtendedCard="
        + supportsAuthenticatedExtendedCard
        + ", security="
        + security
        + ", securitySchemes="
        + securitySchemes
        + '}';
  }
}
