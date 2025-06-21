package io.microagents.a2a.core.client;

import io.microagents.a2a.core.types.core.AgentCard;
import reactor.core.publisher.Mono;

/**
 * Agent Card resolver interface for discovering and fetching A2A agent capabilities.
 *
 * <p>This interface provides functionality to fetch AgentCard objects from A2A agents. Agent cards
 * contain metadata about an agent's capabilities, supported methods, authentication requirements,
 * and endpoint configurations.
 *
 * @since 0.1.0
 */
public interface A2ACardResolver {

  /**
   * Fetches the agent card from the configured endpoint.
   *
   * @return a Mono that emits the AgentCard when available
   */
  Mono<AgentCard> getAgentCard();

  /**
   * Fetches an agent card from a specific path.
   *
   * @param agentCardPath the path to fetch the agent card from
   * @return a Mono that emits the AgentCard when available
   */
  Mono<AgentCard> getAgentCard(String agentCardPath);
}
