package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.microagents.a2a.core.types.notifications.*;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

/**
 * Abstract base class for all content parts in A2A messages and artifacts.
 *
 * <p>Parts represent the smallest unit of content and can be one of three types: text, file, or
 * structured data. This uses Jackson polymorphic deserialization to handle the union type from the
 * A2A specification.
 *
 * @see TextPart
 * @see FilePart
 * @see DataPart
 * @see Message
 * @see Artifact
 * @since 0.1.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "kind")
@JsonSubTypes({
  @JsonSubTypes.Type(value = TextPart.class, name = "text"),
  @JsonSubTypes.Type(value = FilePart.class, name = "file"),
  @JsonSubTypes.Type(value = DataPart.class, name = "data"),
})
public abstract class Part {

  /**
   * Optional metadata associated with this part. Can contain part-specific metadata or processing
   * hints.
   */
  private Map<String, Object> metadata;

  /** Default constructor for Jackson deserialization. */
  protected Part() {}

  /**
   * Creates a new Part with optional metadata.
   *
   * @param metadata optional metadata for this part
   */
  protected Part(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  /**
   * Gets the type discriminator for this part. This is used by Jackson for polymorphic
   * serialization.
   *
   * @return the kind identifier for this part type
   */
  @NotNull
  public abstract String getKind();

  /**
   * Gets the optional metadata associated with this part.
   *
   * @return the metadata map, or null if no metadata is present
   */
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  /**
   * Sets the optional metadata for this part.
   *
   * @param metadata the metadata map
   */
  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  /**
   * Builder-style method to set metadata.
   *
   * @param metadata the metadata map
   * @return this part instance for method chaining
   */
  public Part withMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }
}
