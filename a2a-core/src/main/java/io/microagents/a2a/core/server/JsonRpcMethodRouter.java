package io.microagents.a2a.core.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.microagents.a2a.core.types.core.Event;
import io.microagents.a2a.core.types.core.MessageSendParams;
import io.microagents.a2a.core.types.core.TaskIdParams;
import io.microagents.a2a.core.types.core.TaskPushNotificationConfig;
import io.microagents.a2a.core.types.core.TaskQueryParams;
import io.microagents.a2a.core.types.exceptions.A2AServerException;
import io.microagents.a2a.core.types.requests.GetTaskPushNotificationConfigParams;
import java.util.Map;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Routes JSON-RPC method calls to appropriate RequestHandler methods.
 *
 * <p>This router maps A2A JSON-RPC method names to corresponding handler methods, handling
 * parameter parsing and type conversion. It supports both single-response and streaming methods.
 *
 * @since 0.1.0
 */
public class JsonRpcMethodRouter {

  private final RequestHandler requestHandler;
  private final ObjectMapper objectMapper;

  /**
   * Constructs a new router with the given handler and object mapper.
   *
   * @param requestHandler the handler to route requests to
   * @param objectMapper the JSON object mapper for parameter parsing
   */
  public JsonRpcMethodRouter(RequestHandler requestHandler, ObjectMapper objectMapper) {
    this.requestHandler = requestHandler;
    this.objectMapper = objectMapper;
  }

  /**
   * Routes a non-streaming JSON-RPC method call to the appropriate handler.
   *
   * @param method the JSON-RPC method name
   * @param params the method parameters as a Map
   * @param context the server call context
   * @return a Mono containing the result
   */
  public Mono<Object> route(String method, Map<String, Object> params, ServerCallContext context) {
    JsonNode paramsNode = convertToJsonNode(params);
    return switch (method) {
      case "message/send" -> {
        MessageSendParams msgParams = parseParams(paramsNode, MessageSendParams.class);
        yield requestHandler.onMessageSend(msgParams, context);
      }
      case "tasks/get" -> {
        TaskQueryParams taskParams = parseParams(paramsNode, TaskQueryParams.class);
        yield requestHandler.onGetTask(taskParams, context).cast(Object.class);
      }
      case "tasks/cancel" -> {
        TaskIdParams taskIdParams = parseParams(paramsNode, TaskIdParams.class);
        yield requestHandler.onCancelTask(taskIdParams, context).cast(Object.class);
      }
      case "tasks/pushNotificationConfig/set" -> {
        TaskPushNotificationConfig config =
            parseParams(paramsNode, TaskPushNotificationConfig.class);
        yield requestHandler.onSetTaskPushNotificationConfig(config, context).cast(Object.class);
      }
      case "tasks/pushNotificationConfig/get" -> {
        Object configParams;
        if (paramsNode.has("taskId")) {
          configParams = parseParams(paramsNode, TaskIdParams.class);
        } else {
          configParams = parseParams(paramsNode, GetTaskPushNotificationConfigParams.class);
        }
        yield requestHandler
            .onGetTaskPushNotificationConfig(configParams, context)
            .cast(Object.class);
      }
      default ->
          Mono.error(
              new A2AServerException(
                  "Method not found: " + method,
                  -32601, // JSON-RPC method not found error
                  null));
    };
  }

  /**
   * Routes a streaming JSON-RPC method call to the appropriate handler.
   *
   * @param method the JSON-RPC method name
   * @param params the method parameters as a Map
   * @param context the server call context
   * @return a Flux containing the event stream
   */
  public Flux<Event> routeStreaming(
      String method, Map<String, Object> params, ServerCallContext context) {
    JsonNode paramsNode = convertToJsonNode(params);
    return switch (method) {
      case "message/stream" -> {
        MessageSendParams msgParams = parseParams(paramsNode, MessageSendParams.class);
        yield requestHandler.onMessageSendStream(msgParams, context);
      }
      case "tasks/resubscribe" -> {
        TaskIdParams taskIdParams = parseParams(paramsNode, TaskIdParams.class);
        yield requestHandler.onResubscribeToTask(taskIdParams, context);
      }
      default ->
          Flux.error(
              new A2AServerException(
                  "Streaming method not found: " + method,
                  -32601, // JSON-RPC method not found error
                  null));
    };
  }

  /**
   * Checks if the given method supports streaming.
   *
   * @param method the JSON-RPC method name
   * @return true if the method supports streaming
   */
  public boolean isStreamingMethod(String method) {
    return switch (method) {
      case "message/stream", "tasks/resubscribe" -> true;
      default -> false;
    };
  }

  /**
   * Parses JSON parameters to the specified type.
   *
   * @param params the JSON parameters
   * @param paramType the target parameter type
   * @param <T> the parameter type
   * @return the parsed parameters
   * @throws A2AServerException if parsing fails
   */
  private <T> T parseParams(JsonNode params, Class<T> paramType) {
    try {
      return objectMapper.treeToValue(params, paramType);
    } catch (Exception e) {
      throw new A2AServerException(
          "Invalid parameters for method: " + e.getMessage(),
          -32602, // JSON-RPC invalid params error
          null);
    }
  }

  /**
   * Converts a Map to JsonNode for processing.
   *
   * @param params the parameter map
   * @return the JsonNode representation
   */
  private JsonNode convertToJsonNode(Map<String, Object> params) {
    if (params == null) {
      return objectMapper.nullNode();
    }
    return objectMapper.valueToTree(params);
  }
}
