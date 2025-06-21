package io.microagents.a2a.spring.client;

import io.microagents.a2a.core.client.HttpClient;
import io.microagents.a2a.core.types.exceptions.A2AClientHTTPException;
import io.microagents.a2a.core.types.exceptions.A2AClientJSONException;
import java.time.Duration;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring WebClient implementation of HttpClient for A2A protocol operations.
 *
 * @since 0.1.0
 */
public class WebClientHttpClient implements HttpClient {

  private final WebClient webClient;

  public WebClientHttpClient(WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
  public <T> Mono<T> post(
      String url,
      Object body,
      Class<T> responseType,
      Map<String, String> headers,
      Duration timeout) {
    WebClient.RequestHeadersSpec<?> requestSpec =
        webClient.post().uri(url).contentType(MediaType.APPLICATION_JSON).bodyValue(body);

    // Add headers
    if (headers != null) {
      for (Map.Entry<String, String> header : headers.entrySet()) {
        requestSpec = requestSpec.header(header.getKey(), header.getValue());
      }
    }

    Mono<T> response = requestSpec.retrieve().bodyToMono(responseType);

    // Apply timeout if specified
    if (timeout != null) {
      response = response.timeout(timeout);
    }

    return response
        .onErrorMap(WebClientResponseException.class, this::mapHttpException)
        .onErrorMap(Exception.class, this::mapGenericException);
  }

  @Override
  public <T> Flux<T> postForServerSentEvents(
      String url,
      Object body,
      Class<T> responseType,
      Map<String, String> headers,
      Duration timeout) {
    WebClient.RequestHeadersSpec<?> requestSpec =
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.TEXT_EVENT_STREAM)
            .bodyValue(body);

    // Add headers
    if (headers != null) {
      for (Map.Entry<String, String> header : headers.entrySet()) {
        requestSpec = requestSpec.header(header.getKey(), header.getValue());
      }
    }

    Flux<T> response =
        requestSpec
            .retrieve()
            .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {})
            .map(sseEvent -> parseSSEEvent(sseEvent, responseType));

    // Apply timeout if specified
    if (timeout != null) {
      response = response.timeout(timeout);
    }

    return response
        .onErrorMap(WebClientResponseException.class, this::mapHttpException)
        .onErrorMap(Exception.class, this::mapGenericException);
  }

  @Override
  public <T> Mono<T> get(
      String url, Class<T> responseType, Map<String, String> headers, Duration timeout) {
    WebClient.RequestHeadersSpec<?> requestSpec =
        webClient.get().uri(url).accept(MediaType.APPLICATION_JSON);

    // Add headers
    if (headers != null) {
      for (Map.Entry<String, String> header : headers.entrySet()) {
        requestSpec = requestSpec.header(header.getKey(), header.getValue());
      }
    }

    Mono<T> response = requestSpec.retrieve().bodyToMono(responseType);

    // Apply timeout if specified
    if (timeout != null) {
      response = response.timeout(timeout);
    }

    return response
        .onErrorMap(WebClientResponseException.class, this::mapHttpException)
        .onErrorMap(Exception.class, this::mapGenericException);
  }

  private <T> T parseSSEEvent(ServerSentEvent<String> sseEvent, Class<T> responseType) {
    try {
      String data = sseEvent.data();
      if (data == null) {
        throw new A2AClientJSONException("SSE event data is null");
      }
      // TODO: Use proper JSON parsing - for now assume JSON string parsing is handled elsewhere
      return responseType.cast(data);
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
}
