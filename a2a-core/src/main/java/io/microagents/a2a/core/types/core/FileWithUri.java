package io.microagents.a2a.core.types.core;

import io.microagents.a2a.core.types.notifications.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Represents a file referenced by its URI location.
 *
 * <p>This is used for large files where it's more efficient to provide a reference URI rather than
 * embedding the entire content. The client or agent can retrieve the file content from the provided
 * URI.
 *
 * @see FileReference
 * @see FilePart
 * @since 0.1.0
 */
public class FileWithUri extends FileReference {

  /**
   * URI to the file content. Absolute URLs are strongly recommended. Accessibility is
   * context-dependent.
   */
  @NotBlank(message = "File URI cannot be blank")
  private String uri;

  /** Default constructor for Jackson deserialization. */
  public FileWithUri() {
    super();
  }

  /**
   * Creates a new FileWithUri with the specified URI.
   *
   * @param uri URI to file content (must not be blank)
   */
  public FileWithUri(String uri) {
    super();
    this.uri = uri;
  }

  /**
   * Creates a new FileWithUri with URI, name, and MIME type.
   *
   * @param uri URI to file content (must not be blank)
   * @param name optional filename
   * @param mimeType optional MIME type
   */
  public FileWithUri(String uri, String name, String mimeType) {
    super(name, mimeType);
    this.uri = uri;
  }

  /**
   * Gets the URI to the file content.
   *
   * @return the file URI
   */
  public String getUri() {
    return uri;
  }

  /**
   * Sets the URI to the file content.
   *
   * @param uri the file URI (must not be blank)
   */
  public void setUri(String uri) {
    this.uri = uri;
  }

  /**
   * Builder-style method to set the URI.
   *
   * @param uri the file URI
   * @return this FileWithUri instance for method chaining
   */
  public FileWithUri withUri(String uri) {
    this.uri = uri;
    return this;
  }

  /**
   * Creates a new FileWithUri with the specified URI.
   *
   * @param uri URI to file content
   * @return a new FileWithUri instance
   */
  public static FileWithUri of(String uri) {
    return new FileWithUri(uri);
  }

  /**
   * Creates a new FileWithUri with URI and filename.
   *
   * @param uri URI to file content
   * @param name filename
   * @return a new FileWithUri instance
   */
  public static FileWithUri of(String uri, String name) {
    return new FileWithUri(uri, name, null);
  }

  /**
   * Creates a new FileWithUri with URI, filename, and MIME type.
   *
   * @param uri URI to file content
   * @param name filename
   * @param mimeType MIME type
   * @return a new FileWithUri instance
   */
  public static FileWithUri of(String uri, String name, String mimeType) {
    return new FileWithUri(uri, name, mimeType);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FileWithUri that = (FileWithUri) o;
    return Objects.equals(uri, that.uri)
        && Objects.equals(getName(), that.getName())
        && Objects.equals(getMimeType(), that.getMimeType());
  }

  @Override
  public int hashCode() {
    return Objects.hash(uri, getName(), getMimeType());
  }

  @Override
  public String toString() {
    return "FileWithUri{"
        + "uri='"
        + uri
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
