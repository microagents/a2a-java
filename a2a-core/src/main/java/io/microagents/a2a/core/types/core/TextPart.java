package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.notifications.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a textual content part within an A2A message or artifact.
 *
 * <p>TextPart is used for conveying plain textual content such as user messages, agent responses,
 * or any string-based information.
 *
 * @see Part
 * @see Message
 * @see Artifact
 * @since 0.1.0
 */
public class TextPart extends Part {

  public static final String KIND = "text";

  /** The textual content of this part. */
  @NotBlank(message = "Text content cannot be blank")
  private String text;

  /** Default constructor for Jackson deserialization. */
  public TextPart() {
    super();
  }

  /**
   * Creates a new TextPart with the specified text content.
   *
   * @param text the textual content (must not be blank)
   */
  public TextPart(String text) {
    super();
    this.text = text;
  }

  /**
   * Creates a new TextPart with text content and metadata.
   *
   * @param text the textual content (must not be blank)
   * @param metadata optional metadata for this part
   */
  public TextPart(String text, Map<String, Object> metadata) {
    super(metadata);
    this.text = text;
  }

  @Override
  @JsonProperty("kind")
  @NotNull
  public String getKind() {
    return KIND;
  }

  /**
   * Gets the textual content of this part.
   *
   * @return the text content
   */
  public String getText() {
    return text;
  }

  /**
   * Sets the textual content of this part.
   *
   * @param text the text content (must not be blank)
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Builder-style method to set the text content.
   *
   * @param text the text content
   * @return this TextPart instance for method chaining
   */
  public TextPart withText(String text) {
    this.text = text;
    return this;
  }

  /**
   * Creates a new TextPart with the specified text.
   *
   * @param text the textual content
   * @return a new TextPart instance
   */
  public static TextPart of(String text) {
    return new TextPart(text);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TextPart textPart = (TextPart) o;
    return Objects.equals(text, textPart.text)
        && Objects.equals(getMetadata(), textPart.getMetadata());
  }

  @Override
  public int hashCode() {
    return Objects.hash(text, getMetadata());
  }

  @Override
  public String toString() {
    return "TextPart{" + "text='" + text + '\'' + ", metadata=" + getMetadata() + '}';
  }
}
