package io.microagents.a2a.spring.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.microagents.a2a.core.client.A2AClient;
import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.exceptions.A2AClientHTTPException;
import io.microagents.a2a.core.types.exceptions.A2AClientJSONException;
import io.microagents.a2a.core.types.requests.*;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * A2A Client for interacting with an A2A agent.
 *
 * <p>This client provides methods to communicate with A2A agents using the Agent2Agent protocol
 * over HTTP/JSON-RPC 2.0. It supports both synchronous and streaming operations, and handles all
 * the protocol details internally.
 *
 * <p>The client can be initialized with either an {@link AgentCard} or a direct URL to the agent's
 * RPC endpoint. When using an AgentCard, the URL is extracted from the card's configuration.
 *
 * <h3>Usage Examples:</h3>
 *
 * <pre>{@code
 * // Create client from agent card
 * AgentCard agentCard = ... // obtained from A2ACardResolver
 * A2AClient client = new A2AClient(webClient, agentCard);
 *
 * // Create client with direct URL
 * A2AClient client = new A2AClient(webClient, "https://agent.example.com/a2a");
 *
 * // Send a message
 * SendMessageRequest request = SendMessageRequest.builder()
 *     .withMethod("message/send")
 *     .withParams(MessageSendParams.builder()
 *         .withMessage(Message.userText("Hello"))
 *         .build())
 *     .build();
 *
 * SendMessageSuccessResponse response = client.sendMessage(request).block();
 * }</pre>
 *
 * @since 0.1.0
 */
public class SpringA2AClient implements A2AClient {

  private final WebClient webClient;
  private final String url;
  private final ObjectMapper objectMapper;
  private final Map<String, String> defaultHeaders;
  private final Duration defaultTimeout;
  private final Duration streamingTimeout;

  /**
   * Constructs a new A2AClient with the specified WebClient and AgentCard.
   *
   * @param webClient the reactive web client to use for HTTP requests
   * @param agentCard the agent card containing the agent's endpoint configuration
   * @throws IllegalArgumentException if agentCard is null or doesn't contain a valid URL
   */
  public SpringA2AClient(@NotNull WebClient webClient, @NotNull AgentCard agentCard) {
    this(webClient, agentCard, null);
  }

  /**
   * Constructs a new A2AClient with the specified WebClient, AgentCard, and authentication headers.
   *
   * @param webClient the reactive web client to use for HTTP requests
   * @param agentCard the agent card containing the agent's endpoint configuration
   * @param defaultHeaders optional map of default headers to include with every request (e.g.,
   *     authentication)
   * @throws IllegalArgumentException if agentCard is null or doesn't contain a valid URL
   */
  public SpringA2AClient(
      @NotNull WebClient webClient,
      @NotNull AgentCard agentCard,
      Map<String, String> defaultHeaders) {
    this(webClient, agentCard, defaultHeaders, Duration.ofSeconds(30), null);
  }

  /**
   * Constructs a new A2AClient with full configuration options.
   *
   * @param webClient the reactive web client to use for HTTP requests
   * @param agentCard the agent card containing the agent's endpoint configuration
   * @param defaultHeaders optional map of default headers to include with every request (e.g.,
   *     authentication)
   * @param defaultTimeout timeout for non-streaming requests (null for no timeout)
   * @param streamingTimeout timeout for streaming requests (null for no timeout)
   * @throws IllegalArgumentException if agentCard is null or doesn't contain a valid URL
   */
  public SpringA2AClient(
      @NotNull WebClient webClient,
      @NotNull AgentCard agentCard,
      Map<String, String> defaultHeaders,
      Duration defaultTimeout,
      Duration streamingTimeout) {
    this.webClient = webClient;
    this.url = extractUrlFromAgentCard(agentCard);
    this.objectMapper = createObjectMapper();
    this.defaultHeaders = defaultHeaders != null ? Map.copyOf(defaultHeaders) : Map.of();
    this.defaultTimeout = defaultTimeout;
    this.streamingTimeout = streamingTimeout;
  }

  /**
   * Constructs a new A2AClient with the specified WebClient and direct URL.
   *
   * @param webClient the reactive web client to use for HTTP requests
   * @param url the direct URL to the agent's A2A RPC endpoint
   * @throws IllegalArgumentException if url is null or empty
   */
  public SpringA2AClient(@NotNull WebClient webClient, @NotNull String url) {
    this(webClient, url, null);
  }

  /**
   * Constructs a new A2AClient with the specified WebClient, direct URL, and authentication
   * headers.
   *
   * @param webClient the reactive web client to use for HTTP requests
   * @param url the direct URL to the agent's A2A RPC endpoint
   * @param defaultHeaders optional map of default headers to include with every request (e.g.,
   *     authentication)
   * @throws IllegalArgumentException if url is null or empty
   */
  public SpringA2AClient(
      @NotNull WebClient webClient, @NotNull String url, Map<String, String> defaultHeaders) {
    this(webClient, url, defaultHeaders, Duration.ofSeconds(30), null);
  }

  /**
   * Constructs a new A2AClient with full configuration options.
   *
   * @param webClient the reactive web client to use for HTTP requests
   * @param url the direct URL to the agent's A2A RPC endpoint
   * @param defaultHeaders optional map of default headers to include with every request (e.g.,
   *     authentication)
   * @param defaultTimeout timeout for non-streaming requests (null for no timeout)
   * @param streamingTimeout timeout for streaming requests (null for no timeout)
   * @throws IllegalArgumentException if url is null or empty
   */
  public SpringA2AClient(
      @NotNull WebClient webClient,
      @NotNull String url,
      Map<String, String> defaultHeaders,
      Duration defaultTimeout,
      Duration streamingTimeout) {
    if (url == null || url.trim().isEmpty()) {
      throw new IllegalArgumentException("URL cannot be null or empty");
    }
    this.webClient = webClient;
    this.url = url;
    this.objectMapper = createObjectMapper();
    this.defaultHeaders = defaultHeaders != null ? Map.copyOf(defaultHeaders) : Map.of();
    this.defaultTimeout = defaultTimeout;
    this.streamingTimeout = streamingTimeout;
  }

  /**
   * Creates a new A2AClient by fetching the public AgentCard from the specified URL.
   *
   * <p>This is a convenience factory method that combines agent card resolution and client creation
   * in a single step. For more advanced agent card fetching (e.g., authenticated cards), use {@link
   * A2ACardResolver} directly.
   *
   * @param webClient the reactive web client to use for HTTP requests
   * @param baseUrl the base URL of the agent's host
   * @param agentCardPath the path to the agent card endpoint (default: "/.well-known/agent.json")
   * @return a Mono that emits an initialized A2AClient instance
   * @throws A2AClientHTTPException if an HTTP error occurs fetching the agent card
   * @throws A2AClientJSONException if the agent card response is invalid
   */
  public static Mono<A2AClient> fromAgentCardUrl(
      @NotNull WebClient webClient, @NotNull String baseUrl, String agentCardPath) {
    String cardPath = agentCardPath != null ? agentCardPath : "/.well-known/agent.json";
    SpringA2ACardResolver resolver = new SpringA2ACardResolver(webClient, baseUrl, cardPath);

    return resolver.getAgentCard().map(agentCard -> new SpringA2AClient(webClient, agentCard));
  }

  /**
   * Creates an authentication header map for Bearer token authentication.
   *
   * @param token the bearer token
   * @return a map containing the Authorization header
   */
  public static Map<String, String> bearerAuth(String token) {
    return Map.of("Authorization", "Bearer " + token);
  }

  /**
   * Creates an authentication header map for API key authentication.
   *
   * @param headerName the name of the API key header (e.g., "X-API-Key")
   * @param apiKey the API key value
   * @return a map containing the API key header
   */
  public static Map<String, String> apiKeyAuth(String headerName, String apiKey) {
    return Map.of(headerName, apiKey);
  }

  /**
   * Creates an authentication header map for Basic authentication.
   *
   * @param credentials the Base64-encoded credentials
   * @return a map containing the Authorization header
   */
  public static Map<String, String> basicAuth(String credentials) {
    return Map.of("Authorization", "Basic " + credentials);
  }

  /**
   * Creates a new A2AClient by fetching the public AgentCard from the default path.
   *
   * @param webClient the reactive web client to use for HTTP requests
   * @param baseUrl the base URL of the agent's host
   * @return a Mono that emits an initialized A2AClient instance
   */
  public static Mono<A2AClient> fromAgentCardUrl(
      @NotNull WebClient webClient, @NotNull String baseUrl) {
    return fromAgentCardUrl(webClient, baseUrl, null);
  }

  /**
   * Sends a non-streaming message request to the agent.
   *
   * <p>This method sends a single message to the agent and waits for a complete response. The
   * response can contain either a Task (for long-running operations) or a direct Message result.
   *
   * @param request the SendMessageRequest containing the message and configuration
   * @return a Mono that emits a SendMessageSuccessResponse containing the agent's response
   * @throws A2AClientHTTPException if an HTTP error occurs during the request
   * @throws A2AClientJSONException if the response cannot be decoded or validated
   */
  public Mono<SendMessageSuccessResponse> sendMessage(@NotNull SendMessageRequest request) {
    return sendRequest(ensureRequestId(request), SendMessageSuccessResponse.class);
  }

  /**
   * Sends a streaming message request to the agent and returns a stream of responses.
   *
   * <p>This method uses Server-Sent Events (SSE) to receive a stream of updates from the agent as
   * it processes the request. The stream can include various types of responses such as Task
   * updates, Messages, status updates, and artifacts.
   *
   * @param request the SendStreamingMessageRequest containing the message and configuration
   * @return a Flux that emits SendStreamingMessageResponse objects as they arrive
   * @throws A2AClientHTTPException if an HTTP or SSE protocol error occurs
   * @throws A2AClientJSONException if an SSE event cannot be decoded or validated
   */
  public Flux<SendStreamingMessageResponse> sendMessageStreaming(
      @NotNull SendStreamingMessageRequest request) {
    SendStreamingMessageRequest requestWithId = ensureRequestId(request);

    WebClient.RequestHeadersSpec<?> requestSpec =
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.TEXT_EVENT_STREAM)
            .bodyValue(requestWithId);

    // Add default headers (e.g., authentication)
    for (Map.Entry<String, String> header : defaultHeaders.entrySet()) {
      requestSpec = requestSpec.header(header.getKey(), header.getValue());
    }

    Flux<SendStreamingMessageResponse> response =
        requestSpec
            .retrieve()
            .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {})
            .map(this::parseStreamingResponse);

    // Apply timeout if configured
    if (streamingTimeout != null) {
      response = response.timeout(streamingTimeout);
    }

    return response
        .onErrorMap(WebClientResponseException.class, this::mapHttpException)
        .onErrorMap(Exception.class, this::mapGenericException);
  }

  /**
   * Retrieves the current state and history of a specific task.
   *
   * @param request the GetTaskRequest specifying the task ID and history length
   * @return a Mono that emits a GetTaskResponse containing the Task or an error
   * @throws A2AClientHTTPException if an HTTP error occurs during the request
   * @throws A2AClientJSONException if the response cannot be decoded or validated
   */
  public Mono<GetTaskResponse> getTask(@NotNull GetTaskRequest request) {
    return sendRequest(ensureRequestId(request), GetTaskResponse.class);
  }

  /**
   * Requests the agent to cancel a specific task.
   *
   * @param request the CancelTaskRequest specifying the task ID
   * @return a Mono that emits a CancelTaskResponse containing the updated Task or an error
   * @throws A2AClientHTTPException if an HTTP error occurs during the request
   * @throws A2AClientJSONException if the response cannot be decoded or validated
   */
  public Mono<CancelTaskResponse> cancelTask(@NotNull CancelTaskRequest request) {
    return sendRequest(ensureRequestId(request), CancelTaskResponse.class);
  }

  /**
   * Sets or updates the push notification configuration for a specific task.
   *
   * @param request the SetTaskPushNotificationConfigRequest specifying the task ID and
   *     configuration
   * @return a Mono that emits a SetTaskPushNotificationConfigResponse containing confirmation or an
   *     error
   * @throws A2AClientHTTPException if an HTTP error occurs during the request
   * @throws A2AClientJSONException if the response cannot be decoded or validated
   */
  public Mono<SetTaskPushNotificationConfigResponse> setTaskPushNotificationConfig(
      @NotNull SetTaskPushNotificationConfigRequest request) {
    return sendRequest(ensureRequestId(request), SetTaskPushNotificationConfigResponse.class);
  }

  /**
   * Retrieves the push notification configuration for a specific task.
   *
   * @param request the GetTaskPushNotificationConfigRequest specifying the task ID
   * @return a Mono that emits a GetTaskPushNotificationConfigResponse containing the configuration
   *     or an error
   * @throws A2AClientHTTPException if an HTTP error occurs during the request
   * @throws A2AClientJSONException if the response cannot be decoded or validated
   */
  public Mono<GetTaskPushNotificationConfigResponse> getTaskPushNotificationConfig(
      @NotNull GetTaskPushNotificationConfigRequest request) {
    return sendRequest(ensureRequestId(request), GetTaskPushNotificationConfigResponse.class);
  }

  /**
   * Returns the URL of the agent's A2A endpoint.
   *
   * @return the agent's A2A endpoint URL
   */
  public String getUrl() {
    return url;
  }

  // Private helper methods

  private <T> Mono<T> sendRequest(Object request, Class<T> responseType) {
    WebClient.RequestHeadersSpec<?> requestSpec =
        webClient.post().uri(url).contentType(MediaType.APPLICATION_JSON).bodyValue(request);

    // Add default headers (e.g., authentication)
    for (Map.Entry<String, String> header : defaultHeaders.entrySet()) {
      requestSpec = requestSpec.header(header.getKey(), header.getValue());
    }

    Mono<T> response = requestSpec.retrieve().bodyToMono(responseType);

    // Apply timeout if configured
    if (defaultTimeout != null) {
      response = response.timeout(defaultTimeout);
    }

    return response
        .onErrorMap(WebClientResponseException.class, this::mapHttpException)
        .onErrorMap(Exception.class, this::mapGenericException);
  }

  private SendStreamingMessageResponse parseStreamingResponse(ServerSentEvent<String> sseEvent) {
    try {
      String data = sseEvent.data();
      if (data == null) {
        throw new A2AClientJSONException("SSE event data is null");
      }
      return objectMapper.readValue(data, SendStreamingMessageResponse.class);
    } catch (Exception e) {
      throw new A2AClientJSONException("Failed to parse SSE event data: " + e.getMessage(), e);
    }
  }

  private RuntimeException mapHttpException(WebClientResponseException ex) {
    return new A2AClientHTTPException(
        ex.getStatusCode().value(), String.format("HTTP request failed: %s", ex.getMessage()), ex);
  }

  private RuntimeException mapGenericException(Exception ex) {
    if (ex instanceof A2AClientHTTPException || ex instanceof A2AClientJSONException) {
      return (RuntimeException) ex;
    }
    return new A2AClientHTTPException(503, "Network communication error: " + ex.getMessage(), ex);
  }

  private <T> T ensureRequestId(T request) {
    if (request instanceof JSONRPCMessage jsonRpcMessage) {
      if (jsonRpcMessage.getId() == null) {
        @SuppressWarnings("unchecked")
        T updatedRequest = (T) jsonRpcMessage.withId(UUID.randomUUID().toString());
        return updatedRequest;
      }
    }

    return request;
  }

  private String extractUrlFromAgentCard(AgentCard agentCard) {
    if (agentCard == null) {
      throw new IllegalArgumentException("AgentCard cannot be null");
    }

    String cardUrl = agentCard.getUrl();
    if (cardUrl == null || cardUrl.trim().isEmpty()) {
      throw new IllegalArgumentException("AgentCard must contain a valid URL");
    }

    return cardUrl;
  }

  private ObjectMapper createObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
  }
}
