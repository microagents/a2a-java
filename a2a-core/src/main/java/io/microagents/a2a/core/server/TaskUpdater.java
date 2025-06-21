package io.microagents.a2a.core.server;

import io.microagents.a2a.core.types.core.Artifact;
import io.microagents.a2a.core.types.core.Message;
import io.microagents.a2a.core.types.core.Part;
import io.microagents.a2a.core.types.core.Role;
import io.microagents.a2a.core.types.core.TaskArtifactUpdateEvent;
import io.microagents.a2a.core.types.core.TaskState;
import io.microagents.a2a.core.types.core.TaskStatus;
import io.microagents.a2a.core.types.core.TaskStatusUpdateEvent;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import reactor.core.publisher.Mono;

/**
 * Helper class for agents to publish updates to a task's event queue.
 *
 * <p>Simplifies the process of creating and enqueueing standard task events. This class provides
 * convenience methods for common task state transitions and artifact management operations.
 */
public class TaskUpdater {

  private final EventQueue eventQueue;
  private final String taskId;
  private final String contextId;

  /**
   * Creates a new TaskUpdater.
   *
   * @param eventQueue the EventQueue associated with the task
   * @param taskId the ID of the task
   * @param contextId the context ID of the task
   */
  public TaskUpdater(
      @NotNull EventQueue eventQueue, @NotNull String taskId, @NotNull String contextId) {
    this.eventQueue = eventQueue;
    this.taskId = taskId;
    this.contextId = contextId;
  }

  /**
   * Updates the status of the task and publishes a TaskStatusUpdateEvent.
   *
   * @param state the new state of the task
   * @param message an optional message associated with the status update
   * @param finalUpdate if true, indicates this is the final status update for the task
   * @param timestamp optional ISO 8601 datetime string, defaults to current time if null
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> updateStatus(
      @NotNull TaskState state, Message message, boolean finalUpdate, String timestamp) {
    String currentTimestamp = timestamp != null ? timestamp : Instant.now().toString();

    TaskStatus status =
        TaskStatus.builder(state).withMessage(message).withTimestamp(currentTimestamp).build();

    TaskStatusUpdateEvent event =
        TaskStatusUpdateEvent.builder(taskId, status)
            .withContextId(contextId)
            .withFinal(finalUpdate)
            .build();

    return eventQueue.enqueueEvent(event);
  }

  /**
   * Updates the status of the task with default timestamp.
   *
   * @param state the new state of the task
   * @param message an optional message associated with the status update
   * @param finalUpdate if true, indicates this is the final status update for the task
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> updateStatus(@NotNull TaskState state, Message message, boolean finalUpdate) {
    return updateStatus(state, message, finalUpdate, null);
  }

  /**
   * Updates the status of the task (non-final update).
   *
   * @param state the new state of the task
   * @param message an optional message associated with the status update
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> updateStatus(@NotNull TaskState state, Message message) {
    return updateStatus(state, message, false);
  }

  /**
   * Updates the status of the task (non-final update, no message).
   *
   * @param state the new state of the task
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> updateStatus(@NotNull TaskState state) {
    return updateStatus(state, null, false);
  }

  /**
   * Adds an artifact chunk to the task and publishes a TaskArtifactUpdateEvent.
   *
   * @param parts a list of Part objects forming the artifact chunk
   * @param artifactId the ID of the artifact; a new UUID is generated if null
   * @param name optional name for the artifact
   * @param metadata optional metadata for the artifact
   * @param append optional boolean indicating if this chunk appends to a previous one
   * @param lastChunk optional boolean indicating if this is the last chunk
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> addArtifact(
      @NotNull List<Part> parts,
      String artifactId,
      String name,
      Map<String, Object> metadata,
      Boolean append,
      Boolean lastChunk) {
    String actualArtifactId = artifactId != null ? artifactId : UUID.randomUUID().toString();

    Artifact.Builder artifactBuilder =
        Artifact.builder(actualArtifactId).addParts(parts).withName(name);

    if (metadata != null) {
      artifactBuilder.addMetadata(metadata);
    }

    Artifact artifact = artifactBuilder.build();

    TaskArtifactUpdateEvent event =
        TaskArtifactUpdateEvent.builder(taskId, artifact)
            .withContextId(contextId)
            .withAppend(append)
            .withLastChunk(lastChunk)
            .build();

    return eventQueue.enqueueEvent(event);
  }

  /**
   * Adds an artifact with minimal parameters.
   *
   * @param parts a list of Part objects forming the artifact chunk
   * @param artifactId the ID of the artifact; a new UUID is generated if null
   * @param name optional name for the artifact
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> addArtifact(@NotNull List<Part> parts, String artifactId, String name) {
    return addArtifact(parts, artifactId, name, null, null, null);
  }

  /**
   * Adds an artifact with auto-generated ID.
   *
   * @param parts a list of Part objects forming the artifact chunk
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> addArtifact(@NotNull List<Part> parts) {
    return addArtifact(parts, null, null);
  }

  /**
   * Marks the task as completed and publishes a final status update.
   *
   * @param message optional completion message
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> complete(Message message) {
    return updateStatus(TaskState.COMPLETED, message, true);
  }

  /**
   * Marks the task as completed and publishes a final status update.
   *
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> complete() {
    return complete(null);
  }

  /**
   * Marks the task as failed and publishes a final status update.
   *
   * @param message optional failure message
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> failed(Message message) {
    return updateStatus(TaskState.FAILED, message, true);
  }

  /**
   * Marks the task as failed and publishes a final status update.
   *
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> failed() {
    return failed(null);
  }

  /**
   * Marks the task as rejected and publishes a final status update.
   *
   * @param message optional rejection message
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> reject(Message message) {
    return updateStatus(TaskState.REJECTED, message, true);
  }

  /**
   * Marks the task as rejected and publishes a final status update.
   *
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> reject() {
    return reject(null);
  }

  /**
   * Marks the task as submitted and publishes a status update.
   *
   * @param message optional submission message
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> submit(Message message) {
    return updateStatus(TaskState.SUBMITTED, message, false);
  }

  /**
   * Marks the task as submitted and publishes a status update.
   *
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> submit() {
    return submit(null);
  }

  /**
   * Marks the task as working and publishes a status update.
   *
   * @param message optional work start message
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> startWork(Message message) {
    return updateStatus(TaskState.WORKING, message, false);
  }

  /**
   * Marks the task as working and publishes a status update.
   *
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> startWork() {
    return startWork(null);
  }

  /**
   * Marks the task as cancelled and publishes a final status update.
   *
   * @param message optional cancellation message
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> cancel(Message message) {
    return updateStatus(TaskState.CANCELED, message, true);
  }

  /**
   * Marks the task as cancelled and publishes a final status update.
   *
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> cancel() {
    return cancel(null);
  }

  /**
   * Marks the task as input required and publishes a status update.
   *
   * @param message optional input requirement message
   * @param finalUpdate if true, indicates this is the final status update for the task
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> requiresInput(Message message, boolean finalUpdate) {
    return updateStatus(TaskState.INPUT_REQUIRED, message, finalUpdate);
  }

  /**
   * Marks the task as input required and publishes a status update (non-final).
   *
   * @param message optional input requirement message
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> requiresInput(Message message) {
    return requiresInput(message, false);
  }

  /**
   * Marks the task as input required and publishes a status update (non-final).
   *
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> requiresInput() {
    return requiresInput(null, false);
  }

  /**
   * Marks the task as auth required and publishes a status update.
   *
   * @param message optional auth requirement message
   * @param finalUpdate if true, indicates this is the final status update for the task
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> requiresAuth(Message message, boolean finalUpdate) {
    return updateStatus(TaskState.AUTH_REQUIRED, message, finalUpdate);
  }

  /**
   * Marks the task as auth required and publishes a status update (non-final).
   *
   * @param message optional auth requirement message
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> requiresAuth(Message message) {
    return requiresAuth(message, false);
  }

  /**
   * Marks the task as auth required and publishes a status update (non-final).
   *
   * @return a Mono that completes when the event has been enqueued
   */
  public Mono<Void> requiresAuth() {
    return requiresAuth(null, false);
  }

  /**
   * Creates a new message object sent by the agent for this task/context.
   *
   * <p>Note: This method only <em>creates</em> the message object. It does not automatically
   * enqueue it.
   *
   * @param parts a list of Part objects for the message content
   * @param metadata optional metadata for the message
   * @return a new Message object
   */
  public Message newAgentMessage(@NotNull List<Part> parts, Map<String, Object> metadata) {
    Message message = new Message(UUID.randomUUID().toString(), Role.AGENT, parts);
    return message.withTaskId(taskId).withContextId(contextId).withMetadata(metadata);
  }

  /**
   * Creates a new message object sent by the agent for this task/context.
   *
   * @param parts a list of Part objects for the message content
   * @return a new Message object
   */
  public Message newAgentMessage(@NotNull List<Part> parts) {
    return newAgentMessage(parts, null);
  }

  /**
   * Gets the task ID.
   *
   * @return the task ID
   */
  public String getTaskId() {
    return taskId;
  }

  /**
   * Gets the context ID.
   *
   * @return the context ID
   */
  public String getContextId() {
    return contextId;
  }
}
