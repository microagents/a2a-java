package io.microagents.a2a.core.server;

import io.microagents.a2a.core.types.core.Artifact;
import io.microagents.a2a.core.types.core.Event;
import io.microagents.a2a.core.types.core.Message;
import io.microagents.a2a.core.types.core.Part;
import io.microagents.a2a.core.types.core.Task;
import io.microagents.a2a.core.types.core.TaskArtifactUpdateEvent;
import io.microagents.a2a.core.types.core.TaskState;
import io.microagents.a2a.core.types.core.TaskStatus;
import io.microagents.a2a.core.types.core.TaskStatusUpdateEvent;
import io.microagents.a2a.core.types.exceptions.A2AServerException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Helps manage a task's lifecycle during execution of a request.
 *
 * <p>The TaskManager is responsible for retrieving, saving, and updating {@link Task} objects based
 * on events received from agents. It maintains task state consistency and handles the lifecycle
 * transitions of tasks during agent execution.
 *
 * <p>Key responsibilities:
 *
 * <ul>
 *   <li>Creating new tasks when needed
 *   <li>Loading existing tasks from storage
 *   <li>Processing task-related events (status updates, artifact updates)
 *   <li>Maintaining task history and state transitions
 *   <li>Persisting task changes to the TaskStore
 * </ul>
 *
 * @since 0.1.0
 */
public class TaskManager {
  private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);

  private final String taskId;
  private final String contextId;
  private final TaskStore taskStore;
  private final Message initialMessage;
  private Task currentTask;

  /**
   * Initializes the TaskManager.
   *
   * @param taskId the ID of the task, if known from the request (may be null for new tasks)
   * @param contextId the ID of the context, if known from the request (may be null)
   * @param taskStore the TaskStore instance for persistence
   * @param initialMessage the Message that initiated the task (may be null)
   */
  public TaskManager(String taskId, String contextId, TaskStore taskStore, Message initialMessage) {
    this.taskId = taskId;
    this.contextId = contextId;
    this.taskStore = taskStore;
    this.initialMessage = initialMessage;
    this.currentTask = null;

    logger.debug("TaskManager initialized with task_id: {}, context_id: {}", taskId, contextId);
  }

  /**
   * Creates a TaskManager for a new task.
   *
   * @param taskStore the TaskStore instance for persistence
   * @param initialMessage the Message that initiated the task
   * @return a new TaskManager instance
   */
  public static TaskManager forNewTask(TaskStore taskStore, Message initialMessage) {
    return new TaskManager(null, null, taskStore, initialMessage);
  }

  /**
   * Creates a TaskManager for an existing task.
   *
   * @param taskId the ID of the existing task
   * @param taskStore the TaskStore instance for persistence
   * @return a new TaskManager instance
   */
  public static TaskManager forExistingTask(String taskId, TaskStore taskStore) {
    return new TaskManager(taskId, null, taskStore, null);
  }

  /**
   * Creates a TaskManager with full context information.
   *
   * @param taskId the ID of the task
   * @param contextId the ID of the context
   * @param taskStore the TaskStore instance for persistence
   * @param initialMessage the Message that initiated the task
   * @return a new TaskManager instance
   */
  public static TaskManager withContext(
      String taskId, String contextId, TaskStore taskStore, Message initialMessage) {
    return new TaskManager(taskId, contextId, taskStore, initialMessage);
  }

  /**
   * Retrieves the current task object, either from memory or the store.
   *
   * <p>If taskId is set, it first checks the in-memory currentTask, then attempts to load it from
   * the taskStore.
   *
   * @return a Mono containing the Task object if found, or empty Mono if not found
   */
  public Mono<Task> getTask() {
    if (taskId == null) {
      logger.debug("task_id is not set, cannot get task");
      return Mono.empty();
    }

    if (currentTask != null) {
      return Mono.just(currentTask);
    }

    logger.debug("Attempting to get task from store with id: {}", taskId);
    return taskStore
        .get(taskId)
        .doOnNext(
            task -> {
              if (task != null) {
                logger.debug("Task {} retrieved successfully", taskId);
                currentTask = task;
              } else {
                logger.debug("Task {} not found", taskId);
              }
            });
  }

  /**
   * Processes a task-related event (Task, Status, Artifact) and saves the updated task state.
   *
   * <p>Ensures task and context IDs match or are set from the event.
   *
   * @param event the task-related event (Task, TaskStatusUpdateEvent, or TaskArtifactUpdateEvent)
   * @return a Mono containing the updated Task object after processing the event
   */
  public Mono<Task> saveTaskEvent(Event event) {
    if (!(event instanceof Task
        || event instanceof TaskStatusUpdateEvent
        || event instanceof TaskArtifactUpdateEvent)) {
      return Mono.error(
          new IllegalArgumentException(
              "Event must be Task, TaskStatusUpdateEvent, or TaskArtifactUpdateEvent"));
    }

    String taskIdFromEvent = extractTaskId(event);

    // Validate task ID consistency
    if (taskId != null && !taskId.equals(taskIdFromEvent)) {
      return Mono.error(
          new A2AServerException(
              String.format(
                  "Task in event doesn't match TaskManager %s : %s", taskId, taskIdFromEvent),
              -32602, // Invalid params
              null));
    }

    String contextIdFromEvent = extractContextId(event);

    logger.debug(
        "Processing save of task event of type {} for task_id: {}",
        event.getClass().getSimpleName(),
        taskIdFromEvent);

    if (event instanceof Task taskEvent) {
      return saveTask(taskEvent);
    }

    return ensureTask(event)
        .flatMap(
            task -> {
              if (event instanceof TaskStatusUpdateEvent statusEvent) {
                return processStatusUpdate(task, statusEvent);
              } else if (event instanceof TaskArtifactUpdateEvent artifactEvent) {
                return processArtifactUpdate(task, artifactEvent);
              } else {
                return Mono.error(
                    new IllegalStateException("Unexpected event type: " + event.getClass()));
              }
            });
  }

  /**
   * Ensures a Task object exists in memory, loading from store or creating new if needed.
   *
   * @param event the task-related event triggering the need for a Task object
   * @return a Mono containing an existing or newly created Task object
   */
  public Mono<Task> ensureTask(Event event) {
    if (currentTask != null) {
      return Mono.just(currentTask);
    }

    if (taskId != null) {
      logger.debug("Attempting to retrieve existing task with id: {}", taskId);
      return taskStore.get(taskId).switchIfEmpty(Mono.defer(() -> createNewTask(event)));
    }

    return createNewTask(event);
  }

  /**
   * Processes an event and updates the task state if applicable.
   *
   * <p>If the event is task-related (Task, TaskStatusUpdateEvent, TaskArtifactUpdateEvent), the
   * internal task state is updated and persisted.
   *
   * @param event the event object received from the agent
   * @return a Mono containing the same event object that was processed
   */
  public Mono<Event> processEvent(Event event) {
    if (event instanceof Task
        || event instanceof TaskStatusUpdateEvent
        || event instanceof TaskArtifactUpdateEvent) {
      return saveTaskEvent(event).thenReturn(event);
    }

    return Mono.just(event);
  }

  /**
   * Updates a task object by adding a new message to its history.
   *
   * <p>If the task has a message in its current status, that message is moved to the history first.
   *
   * @param message the new Message to add to the history
   * @param task the Task object to update
   * @return the updated Task object
   */
  public Task updateWithMessage(Message message, Task task) {
    List<Message> history =
        new ArrayList<>(task.getHistory() != null ? task.getHistory() : List.of());

    // Move current status message to history if exists
    if (task.getStatus().getMessage() != null) {
      history.add(task.getStatus().getMessage());
    }

    // Add new message to history
    history.add(message);

    // Create updated task with cleared status message
    TaskStatus updatedStatus =
        TaskStatus.builder(task.getStatus().getState())
            .withTimestamp(task.getStatus().getTimestamp())
            .build(); // No message - cleared as intended

    Task updatedTask =
        Task.builder(task.getId(), task.getContextId(), updatedStatus)
            .withHistory(history)
            .withArtifacts(task.getArtifacts())
            .withMetadata(task.getMetadata())
            .build();

    this.currentTask = updatedTask;
    return updatedTask;
  }

  /**
   * Gets the current task ID.
   *
   * @return the task ID, may be null if not set
   */
  public String getTaskId() {
    return taskId;
  }

  /**
   * Gets the current context ID.
   *
   * @return the context ID, may be null if not set
   */
  public String getContextId() {
    return contextId;
  }

  // Private helper methods

  private String extractTaskId(Event event) {
    if (event instanceof Task task) {
      return task.getId();
    } else if (event instanceof TaskStatusUpdateEvent statusEvent) {
      return statusEvent.getTaskId();
    } else if (event instanceof TaskArtifactUpdateEvent artifactEvent) {
      return artifactEvent.getTaskId();
    }
    return null;
  }

  private String extractContextId(Event event) {
    if (event instanceof Task task) {
      return task.getContextId();
    } else if (event instanceof TaskStatusUpdateEvent statusEvent) {
      return statusEvent.getContextId();
    } else if (event instanceof TaskArtifactUpdateEvent artifactEvent) {
      return artifactEvent.getContextId();
    }
    return null;
  }

  private Mono<Task> processStatusUpdate(Task task, TaskStatusUpdateEvent event) {
    logger.debug("Updating task {} status to: {}", task.getId(), event.getStatus().getState());

    List<Message> history =
        new ArrayList<>(task.getHistory() != null ? task.getHistory() : List.of());

    // Move current status message to history if exists
    if (task.getStatus().getMessage() != null) {
      history.add(task.getStatus().getMessage());
    }

    Task updatedTask =
        Task.builder(task.getId(), task.getContextId(), event.getStatus())
            .withHistory(history)
            .withArtifacts(task.getArtifacts())
            .withMetadata(task.getMetadata())
            .build();

    return saveTask(updatedTask);
  }

  private Mono<Task> processArtifactUpdate(Task task, TaskArtifactUpdateEvent event) {
    logger.debug("Appending artifact to task {}", task.getId());

    // Add artifact to task (implement artifact appending logic)
    List<Artifact> artifacts =
        new ArrayList<>(task.getArtifacts() != null ? task.getArtifacts() : List.of());

    // Check if we should append to existing artifact or add new one
    if (Boolean.TRUE.equals(event.getAppend()) && !artifacts.isEmpty()) {
      // Find existing artifact with same ID and append to it
      Artifact newArtifact = event.getArtifact();
      boolean found = false;

      for (int i = 0; i < artifacts.size(); i++) {
        Artifact existing = artifacts.get(i);
        if (existing.getArtifactId().equals(newArtifact.getArtifactId())) {
          // Merge artifacts - combine parts
          List<Part> combinedParts = new ArrayList<>(existing.getParts());
          combinedParts.addAll(newArtifact.getParts());

          Artifact mergedArtifact =
              Artifact.builder(existing.getArtifactId())
                  .addParts(combinedParts)
                  .withName(
                      newArtifact.getName() != null ? newArtifact.getName() : existing.getName())
                  .addMetadata(existing.getMetadata())
                  .addMetadata(newArtifact.getMetadata())
                  .build();

          artifacts.set(i, mergedArtifact);
          found = true;
          break;
        }
      }

      if (!found) {
        artifacts.add(newArtifact);
      }
    } else {
      // Add as new artifact
      artifacts.add(event.getArtifact());
    }

    Task updatedTask =
        Task.builder(task.getId(), task.getContextId(), task.getStatus())
            .withHistory(task.getHistory())
            .withArtifacts(artifacts)
            .withMetadata(task.getMetadata())
            .build();

    return saveTask(updatedTask);
  }

  private Mono<Task> createNewTask(Event event) {
    String newContextId = extractContextId(event);
    String newTaskId = extractTaskId(event);

    logger.info(
        "Task not found or task_id not set. Creating new task for event (task_id: {}, context_id: {})",
        newTaskId,
        newContextId);

    Task newTask = initTaskObject(newTaskId, newContextId);
    return saveTask(newTask);
  }

  private Task initTaskObject(String newTaskId, String newContextId) {
    logger.debug(
        "Initializing new Task object with task_id: {}, context_id: {}", newTaskId, newContextId);

    List<Message> history = initialMessage != null ? List.of(initialMessage) : new ArrayList<>();

    TaskStatus status = TaskStatus.builder(TaskState.SUBMITTED).withCurrentTimestamp().build();

    return Task.builder(newTaskId, newContextId, status)
        .withHistory(history)
        .withArtifacts(new ArrayList<>())
        .build();
  }

  private Mono<Task> saveTask(Task task) {
    logger.debug("Saving task with id: {}", task.getId());

    return taskStore
        .save(task)
        .then(
            Mono.fromCallable(
                () -> {
                  currentTask = task;
                  logger.info("Task {} saved successfully", task.getId());
                  return task;
                }));
  }
}
