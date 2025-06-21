package io.microagents.a2a.core.client;

import io.microagents.a2a.core.types.core.*;
import io.microagents.a2a.core.types.requests.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * A2A Client interface for interacting with A2A agents.
 *
 * <p>This interface defines the core operations for communicating with A2A agents using the
 * Agent2Agent protocol over HTTP/JSON-RPC 2.0. It supports both synchronous and streaming
 * operations.
 *
 * @since 0.1.0
 */
public interface A2AClient {

  /**
   * Sends a non-streaming message request to the agent.
   *
   * @param request the SendMessageRequest containing the message and configuration
   * @return a Mono that emits a SendMessageSuccessResponse containing the agent's response
   */
  Mono<SendMessageSuccessResponse> sendMessage(SendMessageRequest request);

  /**
   * Sends a streaming message request to the agent and returns a stream of responses.
   *
   * @param request the SendStreamingMessageRequest containing the message and configuration
   * @return a Flux that emits SendStreamingMessageResponse objects as they arrive
   */
  Flux<SendStreamingMessageResponse> sendMessageStreaming(SendStreamingMessageRequest request);

  /**
   * Retrieves the current state and history of a specific task.
   *
   * @param request the GetTaskRequest specifying the task ID and history length
   * @return a Mono that emits a GetTaskResponse containing the Task or an error
   */
  Mono<GetTaskResponse> getTask(GetTaskRequest request);

  /**
   * Cancels a running task.
   *
   * @param request the CancelTaskRequest specifying the task ID to cancel
   * @return a Mono that emits a CancelTaskResponse indicating success or failure
   */
  Mono<CancelTaskResponse> cancelTask(CancelTaskRequest request);

  /**
   * Configures push notifications for a specific task.
   *
   * @param request the SetTaskPushNotificationConfigRequest with notification configuration
   * @return a Mono that emits a SetTaskPushNotificationConfigResponse confirming the configuration
   */
  Mono<SetTaskPushNotificationConfigResponse> setTaskPushNotificationConfig(
      SetTaskPushNotificationConfigRequest request);

  /**
   * Retrieves the push notification configuration for a specific task.
   *
   * @param request the GetTaskPushNotificationConfigRequest specifying the task ID
   * @return a Mono that emits a GetTaskPushNotificationConfigResponse with the current
   *     configuration
   */
  Mono<GetTaskPushNotificationConfigResponse> getTaskPushNotificationConfig(
      GetTaskPushNotificationConfigRequest request);
}
