package io.microagents.a2a.core.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.microagents.a2a.core.types.core.AgentCard;
import io.microagents.a2a.core.types.core.Event;
import io.microagents.a2a.core.types.exceptions.A2AServerException;
import io.microagents.a2a.core.types.requests.JSONRPCError;
import io.microagents.a2a.core.types.requests.JSONRPCErrorResponse;
import io.microagents.a2a.core.types.requests.JSONRPCRequest;
import io.microagents.a2a.core.types.requests.JSONRPCSuccessResponse;
import java.util.function.Function;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Core A2A JSON-RPC server framework providing request handling and response formatting.
 *
 * <p>This class handles the fundamental JSON-RPC 2.0 protocol mechanics including:
 *
 * <ul>
 *   <li>Request parsing and validation
 *   <li>Method routing to request handlers
 *   <li>Response formatting (success and error)
 *   <li>Streaming support via Server-Sent Events
 *   <li>Agent card serving
 * </ul>
 *
 * @since 0.1.0
 */
public class A2AJsonRpcServer {

  private final JsonRpcMethodRouter methodRouter;
  private final ObjectMapper objectMapper;
  private final AgentCard agentCard;
  private final Function<String, ServerCallContext> contextBuilder;

  /**
   * Constructs a new A2A JSON-RPC server.
   *
   * @param requestHandler the request handler implementation
   * @param objectMapper the JSON object mapper
   * @param agentCard the agent card to serve at /.well-known/agent.json
   * @param contextBuilder function to build context from request headers/auth
   */
  public A2AJsonRpcServer(
      RequestHandler requestHandler,
      ObjectMapper objectMapper,
      AgentCard agentCard,
      Function<String, ServerCallContext> contextBuilder) {
    this.methodRouter = new JsonRpcMethodRouter(requestHandler, objectMapper);
    this.objectMapper = objectMapper;
    this.agentCard = agentCard;
    this.contextBuilder = contextBuilder;
  }

  /**
   * Handles a JSON-RPC request and returns the appropriate response.
   *
   * @param requestBody the raw JSON request body
   * @param authorizationHeader the authorization header (optional)
   * @return a Mono containing the JSON-RPC response
   */
  public Mono<String> handleRequest(String requestBody, String authorizationHeader) {
    return parseRequest(requestBody)
        .flatMap(
            request -> {
              ServerCallContext context = contextBuilder.apply(authorizationHeader);

              if (methodRouter.isStreamingMethod(request.getMethod())) {
                return Mono.error(
                    new A2AServerException(
                        "Streaming methods require SSE endpoint",
                        -32000, // Server error
                        null));
              }

              return methodRouter
                  .route(request.getMethod(), request.getParams(), context)
                  .map(result -> (Object) buildSuccessResponse(request.getId(), result))
                  .onErrorResume(
                      throwable -> {
                        if (throwable instanceof A2AServerException serverError) {
                          return Mono.just(
                              (Object) buildErrorResponse(request.getId(), serverError));
                        }
                        return Mono.just(
                            (Object)
                                buildErrorResponse(
                                    request.getId(),
                                    new A2AServerException(
                                        "Internal server error: " + throwable.getMessage(),
                                        -32603, // Internal error
                                        null)));
                      });
            })
        .map(this::toJson);
  }

  /**
   * Handles a streaming JSON-RPC request and returns a Flux of events.
   *
   * @param requestBody the raw JSON request body
   * @param authorizationHeader the authorization header (optional)
   * @return a Flux of events for Server-Sent Events
   */
  public Flux<Event> handleStreamingRequest(String requestBody, String authorizationHeader) {
    return parseRequest(requestBody)
        .flatMapMany(
            request -> {
              ServerCallContext context = contextBuilder.apply(authorizationHeader);

              if (!methodRouter.isStreamingMethod(request.getMethod())) {
                return Flux.error(
                    new A2AServerException(
                        "Non-streaming method called on streaming endpoint",
                        -32000, // Server error
                        null));
              }

              return methodRouter
                  .routeStreaming(request.getMethod(), request.getParams(), context)
                  .onErrorMap(
                      throwable -> {
                        if (throwable instanceof A2AServerException) {
                          return throwable;
                        }
                        return new A2AServerException(
                            "Internal server error: " + throwable.getMessage(),
                            -32603, // Internal error
                            null);
                      });
            });
  }

  /**
   * Gets the agent card for /.well-known/agent.json endpoint.
   *
   * @return the agent card
   */
  public AgentCard getAgentCard() {
    return agentCard;
  }

  /**
   * Parses a JSON-RPC request from the raw request body.
   *
   * @param requestBody the JSON request body
   * @return a Mono containing the parsed request
   */
  private Mono<JSONRPCRequest> parseRequest(String requestBody) {
    try {
      JSONRPCRequest request = objectMapper.readValue(requestBody, JSONRPCRequest.class);
      if (request.getJsonrpc() == null || !"2.0".equals(request.getJsonrpc())) {
        return Mono.error(
            new A2AServerException(
                "Invalid JSON-RPC version",
                -32600, // Invalid request
                null));
      }
      return Mono.just(request);
    } catch (Exception e) {
      return Mono.error(
          new A2AServerException(
              "Parse error: " + e.getMessage(),
              -32700, // Parse error
              null));
    }
  }

  /**
   * Builds a JSON-RPC success response.
   *
   * @param requestId the request ID
   * @param result the result object
   * @return the success response
   */
  private JSONRPCSuccessResponse buildSuccessResponse(Object requestId, Object result) {
    JSONRPCSuccessResponse response = new JSONRPCSuccessResponse();
    response.setId(requestId != null ? requestId.toString() : null);
    response.setResult(result);
    return response;
  }

  /**
   * Builds a JSON-RPC error response.
   *
   * @param requestId the request ID
   * @param serverError the server error
   * @return the error response
   */
  private JSONRPCErrorResponse buildErrorResponse(
      Object requestId, A2AServerException serverError) {
    JSONRPCError error = new JSONRPCError();
    error.setCode(serverError.getErrorCode());
    error.setMessage(serverError.getMessage());
    error.setData(serverError.getData());

    JSONRPCErrorResponse response = new JSONRPCErrorResponse();
    response.setId(requestId != null ? requestId.toString() : null);
    response.setError(error);
    return response;
  }

  /**
   * Converts an object to JSON string.
   *
   * @param object the object to convert
   * @return the JSON string
   */
  private String toJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (Exception e) {
      throw new RuntimeException("Failed to serialize response", e);
    }
  }
}
