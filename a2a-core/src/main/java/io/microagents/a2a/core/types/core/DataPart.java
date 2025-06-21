package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.notifications.*;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a structured data part within an A2A message or artifact.
 *
 * <p>DataPart is used for conveying structured JSON data such as forms, parameters, or any
 * machine-readable information that needs to be processed programmatically.
 *
 * @see Part
 * @see Message
 * @see Artifact
 * @since 0.1.0
 */
public class DataPart extends Part {

  public static final String KIND = "data";

  /**
   * The structured data payload as a map of key-value pairs. This represents JSON object data that
   * can be nested.
   */
  @NotNull(message = "Data content cannot be null")
  private Map<String, Object> data;

  /** Default constructor for Jackson deserialization. */
  public DataPart() {
    super();
  }

  /**
   * Creates a new DataPart with the specified data payload.
   *
   * @param data the structured data content (must not be null)
   */
  public DataPart(Map<String, Object> data) {
    super();
    this.data = data;
  }

  /**
   * Creates a new DataPart with data content and metadata.
   *
   * @param data the structured data content (must not be null)
   * @param metadata optional metadata for this part
   */
  public DataPart(Map<String, Object> data, Map<String, Object> metadata) {
    super(metadata);
    this.data = data;
  }

  @Override
  @JsonProperty("kind")
  @NotNull
  public String getKind() {
    return KIND;
  }

  /**
   * Gets the structured data content of this part.
   *
   * @return the data payload as a map
   */
  public Map<String, Object> getData() {
    return data;
  }

  /**
   * Sets the structured data content of this part.
   *
   * @param data the data payload (must not be null)
   */
  public void setData(Map<String, Object> data) {
    this.data = data;
  }

  /**
   * Builder-style method to set the data content.
   *
   * @param data the data payload
   * @return this DataPart instance for method chaining
   */
  public DataPart withData(Map<String, Object> data) {
    this.data = data;
    return this;
  }

  /**
   * Creates a new DataPart with the specified data.
   *
   * @param data the structured data content
   * @return a new DataPart instance
   */
  public static DataPart of(Map<String, Object> data) {
    return new DataPart(data);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DataPart dataPart = (DataPart) o;
    return Objects.equals(data, dataPart.data)
        && Objects.equals(getMetadata(), dataPart.getMetadata());
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, getMetadata());
  }

  @Override
  public String toString() {
    return "DataPart{" + "data=" + data + ", metadata=" + getMetadata() + '}';
  }
}
