package io.microagents.a2a.core.types.core;

import io.microagents.a2a.core.types.notifications.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Represents a file with its content encoded as base64 bytes.
 *
 * <p>This is used for small files where the entire content can be transmitted directly within the
 * A2A message payload.
 *
 * @see FileReference
 * @see FilePart
 * @since 0.1.0
 */
public class FileWithBytes extends FileReference {

  /** Base64 encoded file content. */
  @NotBlank(message = "File bytes cannot be blank")
  private String bytes;

  /** Default constructor for Jackson deserialization. */
  public FileWithBytes() {
    super();
  }

  /**
   * Creates a new FileWithBytes with the specified base64 content.
   *
   * @param bytes base64 encoded file content (must not be blank)
   */
  public FileWithBytes(String bytes) {
    super();
    this.bytes = bytes;
  }

  /**
   * Creates a new FileWithBytes with content, name, and MIME type.
   *
   * @param bytes base64 encoded file content (must not be blank)
   * @param name optional filename
   * @param mimeType optional MIME type
   */
  public FileWithBytes(String bytes, String name, String mimeType) {
    super(name, mimeType);
    this.bytes = bytes;
  }

  /**
   * Gets the base64 encoded file content.
   *
   * @return the base64 encoded bytes
   */
  public String getBytes() {
    return bytes;
  }

  /**
   * Sets the base64 encoded file content.
   *
   * @param bytes the base64 encoded bytes (must not be blank)
   */
  public void setBytes(String bytes) {
    this.bytes = bytes;
  }

  /**
   * Builder-style method to set the bytes content.
   *
   * @param bytes the base64 encoded bytes
   * @return this FileWithBytes instance for method chaining
   */
  public FileWithBytes withBytes(String bytes) {
    this.bytes = bytes;
    return this;
  }

  /**
   * Creates a new FileWithBytes with the specified base64 content.
   *
   * @param bytes base64 encoded file content
   * @return a new FileWithBytes instance
   */
  public static FileWithBytes of(String bytes) {
    return new FileWithBytes(bytes);
  }

  /**
   * Creates a new FileWithBytes with content and filename.
   *
   * @param bytes base64 encoded file content
   * @param name filename
   * @return a new FileWithBytes instance
   */
  public static FileWithBytes of(String bytes, String name) {
    return new FileWithBytes(bytes, name, null);
  }

  /**
   * Creates a new FileWithBytes with content, filename, and MIME type.
   *
   * @param bytes base64 encoded file content
   * @param name filename
   * @param mimeType MIME type
   * @return a new FileWithBytes instance
   */
  public static FileWithBytes of(String bytes, String name, String mimeType) {
    return new FileWithBytes(bytes, name, mimeType);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FileWithBytes that = (FileWithBytes) o;
    return Objects.equals(bytes, that.bytes)
        && Objects.equals(getName(), that.getName())
        && Objects.equals(getMimeType(), that.getMimeType());
  }

  @Override
  public int hashCode() {
    return Objects.hash(bytes, getName(), getMimeType());
  }

  @Override
  public String toString() {
    return "FileWithBytes{"
        + "bytes='"
        + (bytes != null ? "[" + bytes.length() + " chars]" : "null")
        + '\''
        + ", name='"
        + getName()
        + '\''
        + ", mimeType='"
        + getMimeType()
        + '\''
        + '}';
  }
}
