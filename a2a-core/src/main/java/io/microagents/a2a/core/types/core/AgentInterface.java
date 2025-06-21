package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.notifications.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * AgentInterface provides a declaration of a combination of the target url and the supported
 * transport to interact with the agent.
 *
 * <p>This represents an endpoint where the agent can be reached along with the transport protocol
 * it supports for communication.
 */
public class AgentInterface {

  @NotBlank
  @JsonProperty("url")
  private final String url;

  @NotBlank
  @JsonProperty("transport")
  private final String transport;

  /**
   * Creates a new AgentInterface.
   *
   * @param url the target URL for the agent interface
   * @param transport the supported transport protocol
   */
  public AgentInterface(@NotBlank String url, @NotBlank String transport) {
    this.url = Objects.requireNonNull(url, "url cannot be null").trim();
    if (this.url.isEmpty()) {
      throw new IllegalArgumentException("url cannot be blank");
    }
    this.transport = Objects.requireNonNull(transport, "transport cannot be null").trim();
    if (this.transport.isEmpty()) {
      throw new IllegalArgumentException("transport cannot be blank");
    }
  }

  /**
   * Gets the target URL for the agent interface.
   *
   * @return the URL
   */
  @NotBlank
  public String getUrl() {
    return url;
  }

  /**
   * Gets the supported transport protocol.
   *
   * <p>This is an open form string, to be easily extended for many transport protocols. The core
   * ones officially supported are JSONRPC, GRPC and HTTP+JSON.
   *
   * @return the transport protocol
   */
  @NotBlank
  public String getTransport() {
    return transport;
  }

  /**
   * Creates a new AgentInterface.
   *
   * @param url the target URL
   * @param transport the transport protocol
   * @return a new AgentInterface instance
   */
  public static AgentInterface of(@NotBlank String url, @NotBlank String transport) {
    return new AgentInterface(url, transport);
  }

  /**
   * Creates an AgentInterface with JSONRPC transport.
   *
   * @param url the target URL
   * @return a new AgentInterface instance with JSONRPC transport
   */
  public static AgentInterface jsonRpc(@NotBlank String url) {
    return new AgentInterface(url, "JSONRPC");
  }

  /**
   * Creates an AgentInterface with gRPC transport.
   *
   * @param url the target URL
   * @return a new AgentInterface instance with gRPC transport
   */
  public static AgentInterface grpc(@NotBlank String url) {
    return new AgentInterface(url, "GRPC");
  }

  /**
   * Creates an AgentInterface with HTTP+JSON transport.
   *
   * @param url the target URL
   * @return a new AgentInterface instance with HTTP+JSON transport
   */
  public static AgentInterface httpJson(@NotBlank String url) {
    return new AgentInterface(url, "HTTP+JSON");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AgentInterface that = (AgentInterface) o;
    return Objects.equals(url, that.url) && Objects.equals(transport, that.transport);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url, transport);
  }

  @Override
  public String toString() {
    return "AgentInterface{" + "url='" + url + '\'' + ", transport='" + transport + '\'' + '}';
  }
}
