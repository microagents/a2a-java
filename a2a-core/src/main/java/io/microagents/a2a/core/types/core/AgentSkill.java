package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.notifications.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.*;

/**
 * Represents a unit of capability that an agent can perform.
 *
 * <p>Skills define specific capabilities of an agent with descriptions, examples, supported
 * input/output modes, and categorization tags.
 */
public class AgentSkill {

  @NotBlank
  @JsonProperty("id")
  private final String id;

  @NotBlank
  @JsonProperty("name")
  private final String name;

  @NotBlank
  @JsonProperty("description")
  private final String description;

  @NotEmpty
  @JsonProperty("tags")
  private final List<String> tags;

  @JsonProperty("examples")
  private final List<String> examples;

  @JsonProperty("inputModes")
  private final List<String> inputModes;

  @JsonProperty("outputModes")
  private final List<String> outputModes;

  /**
   * Creates a new AgentSkill with required fields.
   *
   * @param id unique identifier for the agent's skill
   * @param name human readable name of the skill
   * @param description description of the skill
   * @param tags set of tagwords describing classes of capabilities
   */
  public AgentSkill(
      @NotBlank String id,
      @NotBlank String name,
      @NotBlank String description,
      @NotEmpty List<String> tags) {
    this(id, name, description, tags, null, null, null);
  }

  /**
   * Creates a new AgentSkill with all fields.
   *
   * @param id unique identifier for the agent's skill
   * @param name human readable name of the skill
   * @param description description of the skill
   * @param tags set of tagwords describing classes of capabilities
   * @param examples example scenarios that the skill can perform
   * @param inputModes supported interaction modes for input
   * @param outputModes supported media types for output
   */
  public AgentSkill(
      @NotBlank String id,
      @NotBlank String name,
      @NotBlank String description,
      @NotEmpty List<String> tags,
      List<String> examples,
      List<String> inputModes,
      List<String> outputModes) {
    this.id = Objects.requireNonNull(id, "id cannot be null").trim();
    if (this.id.isEmpty()) {
      throw new IllegalArgumentException("id cannot be blank");
    }
    this.name = Objects.requireNonNull(name, "name cannot be null").trim();
    if (this.name.isEmpty()) {
      throw new IllegalArgumentException("name cannot be blank");
    }
    this.description = Objects.requireNonNull(description, "description cannot be null").trim();
    if (this.description.isEmpty()) {
      throw new IllegalArgumentException("description cannot be blank");
    }
    this.tags =
        Collections.unmodifiableList(
            new ArrayList<>(Objects.requireNonNull(tags, "tags cannot be null")));
    if (this.tags.isEmpty()) {
      throw new IllegalArgumentException("tags cannot be empty");
    }
    this.examples =
        examples == null ? null : Collections.unmodifiableList(new ArrayList<>(examples));
    this.inputModes =
        inputModes == null ? null : Collections.unmodifiableList(new ArrayList<>(inputModes));
    this.outputModes =
        outputModes == null ? null : Collections.unmodifiableList(new ArrayList<>(outputModes));
  }

  /**
   * Gets the unique identifier for the agent's skill.
   *
   * @return the skill ID
   */
  @NotBlank
  public String getId() {
    return id;
  }

  /**
   * Gets the human readable name of the skill.
   *
   * @return the skill name
   */
  @NotBlank
  public String getName() {
    return name;
  }

  /**
   * Gets the description of the skill.
   *
   * <p>This description will be used by the client or a human as a hint to understand what the
   * skill does.
   *
   * @return the skill description
   */
  @NotBlank
  public String getDescription() {
    return description;
  }

  /**
   * Gets the set of tagwords describing classes of capabilities for this specific skill.
   *
   * @return unmodifiable list of tags
   */
  @NotEmpty
  public List<String> getTags() {
    return tags;
  }

  /**
   * Gets the set of example scenarios that the skill can perform.
   *
   * <p>Will be used by the client as a hint to understand how the skill can be used.
   *
   * @return unmodifiable list of examples, or null if none
   */
  public List<String> getExamples() {
    return examples;
  }

  /**
   * Gets the set of interaction modes that the skill supports.
   *
   * <p>These are supported media types for input (if different than the default).
   *
   * @return unmodifiable list of input modes, or null if using defaults
   */
  public List<String> getInputModes() {
    return inputModes;
  }

  /**
   * Gets the supported media types for output.
   *
   * @return unmodifiable list of output modes, or null if using defaults
   */
  public List<String> getOutputModes() {
    return outputModes;
  }

  /**
   * Creates a new AgentSkill with required fields.
   *
   * @param id unique identifier for the agent's skill
   * @param name human readable name of the skill
   * @param description description of the skill
   * @param tags set of tagwords describing classes of capabilities
   * @return a new AgentSkill instance
   */
  public static AgentSkill of(
      @NotBlank String id,
      @NotBlank String name,
      @NotBlank String description,
      @NotEmpty List<String> tags) {
    return new AgentSkill(id, name, description, tags);
  }

  /**
   * Creates a new AgentSkill with a single tag.
   *
   * @param id unique identifier for the agent's skill
   * @param name human readable name of the skill
   * @param description description of the skill
   * @param tag single tag describing the capability
   * @return a new AgentSkill instance
   */
  public static AgentSkill of(
      @NotBlank String id,
      @NotBlank String name,
      @NotBlank String description,
      @NotBlank String tag) {
    return new AgentSkill(id, name, description, List.of(tag));
  }

  /**
   * Creates a builder for constructing AgentSkill instances.
   *
   * @param id the required skill ID
   * @param name the required skill name
   * @return a new builder instance
   */
  public static Builder builder(@NotBlank String id, @NotBlank String name) {
    return new Builder(id, name);
  }

  /** Builder for constructing AgentSkill instances. */
  public static class Builder {
    private final String id;
    private final String name;
    private String description;
    private final List<String> tags = new ArrayList<>();
    private final List<String> examples = new ArrayList<>();
    private final List<String> inputModes = new ArrayList<>();
    private final List<String> outputModes = new ArrayList<>();

    private Builder(@NotBlank String id, @NotBlank String name) {
      this.id = Objects.requireNonNull(id, "id cannot be null").trim();
      if (this.id.isEmpty()) {
        throw new IllegalArgumentException("id cannot be blank");
      }
      this.name = Objects.requireNonNull(name, "name cannot be null").trim();
      if (this.name.isEmpty()) {
        throw new IllegalArgumentException("name cannot be blank");
      }
    }

    /**
     * Sets the skill description.
     *
     * @param description the description
     * @return this builder
     */
    public Builder withDescription(@NotBlank String description) {
      this.description = Objects.requireNonNull(description, "description cannot be null").trim();
      if (this.description.isEmpty()) {
        throw new IllegalArgumentException("description cannot be blank");
      }
      return this;
    }

    /**
     * Adds a tag to the skill.
     *
     * @param tag the tag to add
     * @return this builder
     */
    public Builder addTag(@NotBlank String tag) {
      Objects.requireNonNull(tag, "tag cannot be null");
      String trimmedTag = tag.trim();
      if (trimmedTag.isEmpty()) {
        throw new IllegalArgumentException("tag cannot be blank");
      }
      this.tags.add(trimmedTag);
      return this;
    }

    /**
     * Adds multiple tags to the skill.
     *
     * @param tags the tags to add
     * @return this builder
     */
    public Builder addTags(@NotEmpty Collection<String> tags) {
      Objects.requireNonNull(tags, "tags cannot be null");
      tags.forEach(tag -> addTag(tag));
      return this;
    }

    /**
     * Adds an example to the skill.
     *
     * @param example the example to add
     * @return this builder
     */
    public Builder addExample(@NotBlank String example) {
      Objects.requireNonNull(example, "example cannot be null");
      String trimmedExample = example.trim();
      if (trimmedExample.isEmpty()) {
        throw new IllegalArgumentException("example cannot be blank");
      }
      this.examples.add(trimmedExample);
      return this;
    }

    /**
     * Adds multiple examples to the skill.
     *
     * @param examples the examples to add
     * @return this builder
     */
    public Builder addExamples(@NotNull Collection<String> examples) {
      Objects.requireNonNull(examples, "examples cannot be null");
      examples.forEach(example -> addExample(example));
      return this;
    }

    /**
     * Adds an input mode to the skill.
     *
     * @param inputMode the input mode to add
     * @return this builder
     */
    public Builder addInputMode(@NotBlank String inputMode) {
      Objects.requireNonNull(inputMode, "inputMode cannot be null");
      String trimmedMode = inputMode.trim();
      if (trimmedMode.isEmpty()) {
        throw new IllegalArgumentException("inputMode cannot be blank");
      }
      this.inputModes.add(trimmedMode);
      return this;
    }

    /**
     * Adds multiple input modes to the skill.
     *
     * @param inputModes the input modes to add
     * @return this builder
     */
    public Builder addInputModes(@NotNull Collection<String> inputModes) {
      Objects.requireNonNull(inputModes, "inputModes cannot be null");
      inputModes.forEach(mode -> addInputMode(mode));
      return this;
    }

    /**
     * Adds an output mode to the skill.
     *
     * @param outputMode the output mode to add
     * @return this builder
     */
    public Builder addOutputMode(@NotBlank String outputMode) {
      Objects.requireNonNull(outputMode, "outputMode cannot be null");
      String trimmedMode = outputMode.trim();
      if (trimmedMode.isEmpty()) {
        throw new IllegalArgumentException("outputMode cannot be blank");
      }
      this.outputModes.add(trimmedMode);
      return this;
    }

    /**
     * Adds multiple output modes to the skill.
     *
     * @param outputModes the output modes to add
     * @return this builder
     */
    public Builder addOutputModes(@NotNull Collection<String> outputModes) {
      Objects.requireNonNull(outputModes, "outputModes cannot be null");
      outputModes.forEach(mode -> addOutputMode(mode));
      return this;
    }

    /**
     * Builds the AgentSkill instance.
     *
     * @return a new AgentSkill
     * @throws IllegalStateException if description is not set or no tags were added
     */
    public AgentSkill build() {
      if (description == null) {
        throw new IllegalStateException("description must be set");
      }
      if (tags.isEmpty()) {
        throw new IllegalStateException("At least one tag must be added");
      }
      return new AgentSkill(
          id,
          name,
          description,
          tags,
          examples.isEmpty() ? null : examples,
          inputModes.isEmpty() ? null : inputModes,
          outputModes.isEmpty() ? null : outputModes);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AgentSkill that = (AgentSkill) o;
    return Objects.equals(id, that.id)
        && Objects.equals(name, that.name)
        && Objects.equals(description, that.description)
        && Objects.equals(tags, that.tags)
        && Objects.equals(examples, that.examples)
        && Objects.equals(inputModes, that.inputModes)
        && Objects.equals(outputModes, that.outputModes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, tags, examples, inputModes, outputModes);
  }

  @Override
  public String toString() {
    return "AgentSkill{"
        + "id='"
        + id
        + '\''
        + ", name='"
        + name
        + '\''
        + ", description='"
        + description
        + '\''
        + ", tags="
        + tags
        + ", examples="
        + examples
        + ", inputModes="
        + inputModes
        + ", outputModes="
        + outputModes
        + '}';
  }
}
