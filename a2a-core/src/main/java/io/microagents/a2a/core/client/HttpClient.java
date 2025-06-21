package io.microagents.a2a.core.client;

import java.time.Duration;
import java.util.Map;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Abstract HTTP client interface for A2A protocol operations.
 *
 * <p>This interface abstracts HTTP operations needed by the A2A client, allowing different
 * implementations (Spring WebClient, OkHttp, JDK HttpClient, etc.) to be plugged in without
 * changing the core A2A logic.
 *
 * @since 0.1.0
 */
public interface HttpClient {

  /**
   * Sends a POST request with JSON body and returns the response as a specific type.
   *
   * @param <T> the response type
   * @param url the target URL
   * @param body the request body object (will be serialized to JSON)
   * @param responseType the expected response type
   * @param headers optional headers to include
   * @param timeout optional timeout for the request
   * @return a Mono emitting the response
   */
  <T> Mono<T> post(
      String url,
      Object body,
      Class<T> responseType,
      Map<String, String> headers,
      Duration timeout);

  /**
   * Sends a POST request for Server-Sent Events and returns a stream of responses.
   *
   * @param <T> the response type
   * @param url the target URL
   * @param body the request body object (will be serialized to JSON)
   * @param responseType the expected response type for each SSE event
   * @param headers optional headers to include
   * @param timeout optional timeout for the request
   * @return a Flux emitting SSE responses
   */
  <T> Flux<T> postForServerSentEvents(
      String url,
      Object body,
      Class<T> responseType,
      Map<String, String> headers,
      Duration timeout);

  /**
   * Sends a GET request and returns the response as a specific type.
   *
   * @param <T> the response type
   * @param url the target URL
   * @param responseType the expected response type
   * @param headers optional headers to include
   * @param timeout optional timeout for the request
   * @return a Mono emitting the response
   */
  <T> Mono<T> get(String url, Class<T> responseType, Map<String, String> headers, Duration timeout);
}
