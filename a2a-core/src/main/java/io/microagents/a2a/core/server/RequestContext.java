package io.microagents.a2a.core.server;

import io.microagents.a2a.core.types.core.Message;
import io.microagents.a2a.core.types.core.MessageSendConfiguration;
import io.microagents.a2a.core.types.core.MessageSendParams;
import io.microagents.a2a.core.types.core.Task;
import io.microagents.a2a.core.types.core.TextPart;
import io.microagents.a2a.core.types.exceptions.A2AServerException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Request Context for agent execution.
 *
 * <p>Holds information about the current request being processed by the server, including the
 * incoming message, task and context identifiers, and related tasks. This context is passed to
 * agent executors to provide all necessary information for processing requests.
 *
 * <p>The RequestContext handles ID generation and validation to ensure consistency between requests
 * and tasks. It provides convenient access to request data and maintains relationships between
 * related tasks.
 *
 * @since 0.1.0
 */
public class RequestContext {

  private final MessageSendParams params;
  private final String taskId;
  private final String contextId;
  private final List<Task> relatedTasks;
  private final ServerCallContext callContext;
  private Task currentTask;

  /**
   * Creates a new RequestContext with full parameters.
   *
   * @param params the incoming MessageSendParams request payload
   * @param taskId the ID of the task explicitly provided in the request or path
   * @param contextId the ID of the context explicitly provided in the request or path
   * @param task the existing Task object retrieved from the store, if any
   * @param relatedTasks a list of other tasks related to the current request
   * @param callContext the server call context associated with this request
   */
  public RequestContext(
      MessageSendParams params,
      String taskId,
      String contextId,
      Task task,
      List<Task> relatedTasks,
      ServerCallContext callContext) {
    this.params = params;
    this.taskId = taskId;
    this.contextId = contextId;
    this.currentTask = task;
    this.relatedTasks = relatedTasks != null ? new ArrayList<>(relatedTasks) : new ArrayList<>();
    this.callContext = callContext;

    validateAndSetupIds();
  }

  /**
   * Creates a RequestContext for a new request without existing task.
   *
   * @param params the incoming MessageSendParams request payload
   * @param callContext the server call context
   * @return a new RequestContext instance
   */
  public static RequestContext forNewRequest(
      MessageSendParams params, ServerCallContext callContext) {
    return new RequestContext(params, null, null, null, null, callContext);
  }

  /**
   * Creates a RequestContext for an existing task.
   *
   * @param params the incoming MessageSendParams request payload
   * @param taskId the ID of the existing task
   * @param task the existing Task object
   * @param callContext the server call context
   * @return a new RequestContext instance
   */
  public static RequestContext forExistingTask(
      MessageSendParams params, String taskId, Task task, ServerCallContext callContext) {
    return new RequestContext(
        params, taskId, task != null ? task.getContextId() : null, task, null, callContext);
  }

  /**
   * Creates a RequestContext with specified IDs.
   *
   * @param params the incoming MessageSendParams request payload
   * @param taskId the task ID
   * @param contextId the context ID
   * @param callContext the server call context
   * @return a new RequestContext instance
   */
  public static RequestContext withIds(
      MessageSendParams params, String taskId, String contextId, ServerCallContext callContext) {
    return new RequestContext(params, taskId, contextId, null, null, callContext);
  }

  /**
   * Extracts text content from the user's message parts.
   *
   * @param delimiter the string to use when joining multiple text parts
   * @return a single string containing all text content from the user message, joined by the
   *     specified delimiter. Returns an empty string if no user message is present or if it
   *     contains no text parts.
   */
  public String getUserInput(String delimiter) {
    if (params == null || params.getMessage() == null) {
      return "";
    }

    return params.getMessage().getParts().stream()
        .filter(part -> part instanceof TextPart)
        .map(part -> ((TextPart) part).getText())
        .collect(Collectors.joining(delimiter));
  }

  /**
   * Extracts text content from the user's message parts using newline as delimiter.
   *
   * @return a single string containing all text content from the user message
   */
  public String getUserInput() {
    return getUserInput("\n");
  }

  /**
   * Attaches a related task to the context.
   *
   * <p>This is useful for scenarios like tool execution where a new task might be spawned.
   *
   * @param task the Task object to attach
   */
  public void attachRelatedTask(Task task) {
    if (task != null) {
      relatedTasks.add(task);
    }
  }

  /**
   * Gets the incoming Message object from the request, if available.
   *
   * @return the message, or null if not available
   */
  public Message getMessage() {
    return params != null ? params.getMessage() : null;
  }

  /**
   * Gets the list of tasks related to the current request.
   *
   * @return an immutable list of related tasks
   */
  public List<Task> getRelatedTasks() {
    return new ArrayList<>(relatedTasks);
  }

  /**
   * Gets the current Task object being processed.
   *
   * @return the current task, or null if not set
   */
  public Task getCurrentTask() {
    return currentTask;
  }

  /**
   * Sets the current task object.
   *
   * @param task the task to set as current
   */
  public void setCurrentTask(Task task) {
    this.currentTask = task;
  }

  /**
   * Gets the ID of the task associated with this context.
   *
   * @return the task ID, or null if not set
   */
  public String getTaskId() {
    return taskId;
  }

  /**
   * Gets the ID of the conversation context associated with this task.
   *
   * @return the context ID, or null if not set
   */
  public String getContextId() {
    return contextId;
  }

  /**
   * Gets the MessageSendConfiguration from the request, if available.
   *
   * @return the configuration, or null if not available
   */
  public MessageSendConfiguration getConfiguration() {
    return params != null ? params.getConfiguration() : null;
  }

  /**
   * Gets the server call context associated with this request.
   *
   * @return the call context, or null if not set
   */
  public ServerCallContext getCallContext() {
    return callContext;
  }

  /**
   * Gets the full request parameters.
   *
   * @return the MessageSendParams, or null if not set
   */
  public MessageSendParams getParams() {
    return params;
  }

  /**
   * Checks if this context has an associated request.
   *
   * @return true if params is not null, false otherwise
   */
  public boolean hasRequest() {
    return params != null;
  }

  /**
   * Checks if this context has an associated message.
   *
   * @return true if a message is available, false otherwise
   */
  public boolean hasMessage() {
    return hasRequest() && params.getMessage() != null;
  }

  /**
   * Checks if this context has a current task.
   *
   * @return true if a current task is set, false otherwise
   */
  public boolean hasCurrentTask() {
    return currentTask != null;
  }

  // Private helper methods

  private void validateAndSetupIds() {
    if (params == null || params.getMessage() == null) {
      return;
    }

    Message message = params.getMessage();

    // Validate and setup task ID
    if (taskId != null) {
      message.setTaskId(taskId);
      if (currentTask != null && !taskId.equals(currentTask.getId())) {
        throw new A2AServerException(
            "Task ID mismatch between context and current task",
            -32602, // Invalid params
            null);
      }
    } else {
      checkOrGenerateTaskId(message);
    }

    // Validate and setup context ID
    if (contextId != null) {
      message.setContextId(contextId);
      if (currentTask != null && !contextId.equals(currentTask.getContextId())) {
        throw new A2AServerException(
            "Context ID mismatch between context and current task",
            -32602, // Invalid params
            null);
      }
    } else {
      checkOrGenerateContextId(message);
    }
  }

  private void checkOrGenerateTaskId(Message message) {
    if (message.getTaskId() == null || message.getTaskId().trim().isEmpty()) {
      message.setTaskId(UUID.randomUUID().toString());
    }
  }

  private void checkOrGenerateContextId(Message message) {
    if (message.getContextId() == null || message.getContextId().trim().isEmpty()) {
      message.setContextId(UUID.randomUUID().toString());
    }
  }
}
