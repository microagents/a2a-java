package io.microagents.a2a.spring.client;

import io.microagents.a2a.core.types.core.AgentCard;
import io.microagents.a2a.core.types.exceptions.A2AClientHTTPException;
import io.microagents.a2a.core.types.exceptions.A2AClientJSONException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * Agent Card resolver for discovering and fetching A2A agent capabilities.
 *
 * <p>This class provides functionality to fetch AgentCard objects from A2A agents. Agent cards
 * contain metadata about an agent's capabilities, supported methods, authentication requirements,
 * and endpoint configurations.
 *
 * <p>The resolver supports fetching both public agent cards (typically available at {@code
 * /.well-known/agent.json}) and authenticated or extended agent cards from custom paths.
 *
 * <h3>Usage Examples:</h3>
 *
 * <pre>{@code
 * // Create resolver for public agent card
 * A2ACardResolver resolver = new A2ACardResolver(webClient, "https://agent.example.com");
 *
 * // Fetch the public agent card
 * AgentCard publicCard = resolver.getAgentCard().block();
 *
 * // Fetch a custom agent card from a specific path
 * AgentCard customCard = resolver.getAgentCard("/api/agent-card").block();
 * }</pre>
 *
 * @since 0.1.0
 */
public class SpringA2ACardResolver {
  private static final Logger logger = LoggerFactory.getLogger(SpringA2ACardResolver.class);

  private final WebClient webClient;
  private final String baseUrl;
  private final String defaultAgentCardPath;

  /**
   * Constructs a new A2ACardResolver with the specified configuration.
   *
   * @param webClient the reactive web client to use for HTTP requests
   * @param baseUrl the base URL of the agent's host (without trailing slash)
   * @param agentCardPath the default path to the agent card endpoint, relative to the base URL
   * @throws IllegalArgumentException if any parameter is null or empty
   */
  public SpringA2ACardResolver(
      @NotNull WebClient webClient, @NotBlank String baseUrl, @NotBlank String agentCardPath) {
    this.webClient = webClient;
    this.baseUrl = normalizeBaseUrl(baseUrl);
    this.defaultAgentCardPath = normalizeAgentCardPath(agentCardPath);
  }

  /**
   * Constructs a new A2ACardResolver with the default agent card path.
   *
   * <p>Uses the standard A2A agent card path: {@code /.well-known/agent.json}
   *
   * @param webClient the reactive web client to use for HTTP requests
   * @param baseUrl the base URL of the agent's host (without trailing slash)
   * @throws IllegalArgumentException if any parameter is null or empty
   */
  public SpringA2ACardResolver(@NotNull WebClient webClient, @NotBlank String baseUrl) {
    this(webClient, baseUrl, "/.well-known/agent.json");
  }

  /**
   * Fetches the agent card from the default path.
   *
   * <p>This method fetches the agent card using the default agent card path configured during
   * resolver initialization (typically the public agent card).
   *
   * @return a Mono that emits an AgentCard object representing the agent's capabilities
   * @throws A2AClientHTTPException if an HTTP error occurs during the request
   * @throws A2AClientJSONException if the response cannot be decoded as JSON or validated
   */
  public Mono<AgentCard> getAgentCard() {
    return getAgentCard(null);
  }

  /**
   * Fetches an agent card from a specified path relative to the base URL.
   *
   * <p>If the relative card path is null, this method uses the default public agent card path. This
   * allows fetching both public and authenticated/extended agent cards from the same agent.
   *
   * @param relativeCardPath optional path to the agent card endpoint, relative to the base URL. If
   *     null, uses the default public agent card path
   * @return a Mono that emits an AgentCard object representing the agent's capabilities
   * @throws A2AClientHTTPException if an HTTP error occurs during the request
   * @throws A2AClientJSONException if the response cannot be decoded as JSON or validated
   */
  public Mono<AgentCard> getAgentCard(String relativeCardPath) {
    String pathSegment =
        relativeCardPath != null ? normalizeAgentCardPath(relativeCardPath) : defaultAgentCardPath;

    String targetUrl = baseUrl + pathSegment;

    return webClient
        .get()
        .uri(targetUrl)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(AgentCard.class)
        .doOnSuccess(
            agentCard -> {
              logger.debug("Successfully fetched agent card from {}", targetUrl);
            })
        .onErrorMap(
            WebClientResponseException.class,
            ex ->
                new A2AClientHTTPException(
                    ex.getStatusCode().value(),
                    String.format(
                        "Failed to fetch agent card from %s: %s", targetUrl, ex.getMessage()),
                    ex))
        .onErrorMap(
            Exception.class,
            ex -> {
              if (ex instanceof A2AClientHTTPException) {
                return ex;
              }
              return new A2AClientJSONException(
                  String.format(
                      "Failed to parse or validate agent card from %s: %s",
                      targetUrl, ex.getMessage()),
                  ex);
            });
  }

  /**
   * Returns the base URL configured for this resolver.
   *
   * @return the base URL of the agent's host
   */
  public String getBaseUrl() {
    return baseUrl;
  }

  /**
   * Returns the default agent card path configured for this resolver.
   *
   * @return the default agent card path
   */
  public String getDefaultAgentCardPath() {
    return defaultAgentCardPath;
  }

  // Private helper methods

  private String normalizeBaseUrl(String baseUrl) {
    if (baseUrl == null || baseUrl.trim().isEmpty()) {
      throw new IllegalArgumentException("Base URL cannot be null or empty");
    }
    return baseUrl.replaceAll("/+$", ""); // Remove trailing slashes
  }

  private String normalizeAgentCardPath(String agentCardPath) {
    if (agentCardPath == null || agentCardPath.trim().isEmpty()) {
      throw new IllegalArgumentException("Agent card path cannot be null or empty");
    }
    String normalized = agentCardPath.trim();
    if (!normalized.startsWith("/")) {
      normalized = "/" + normalized;
    }
    return normalized;
  }
}
