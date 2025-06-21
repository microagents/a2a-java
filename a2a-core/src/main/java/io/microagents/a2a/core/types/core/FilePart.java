package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.microagents.a2a.core.types.notifications.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a file content part within an A2A message or artifact.
 *
 * <p>FilePart contains a file reference which can be either direct byte content (for small files)
 * or a URI reference (for large files). The file reference also includes optional metadata like
 * filename and MIME type.
 *
 * @see Part
 * @see FileReference
 * @see FileWithBytes
 * @see FileWithUri
 * @see Message
 * @see Artifact
 * @since 0.1.0
 */
public class FilePart extends Part {

  public static final String KIND = "file";

  /** File content either as bytes or URI reference. */
  @NotNull(message = "File reference cannot be null")
  @Valid
  private FileReference file;

  /** Default constructor for Jackson deserialization. */
  public FilePart() {
    super();
  }

  /**
   * Creates a new FilePart with the specified file reference.
   *
   * @param file the file reference (must not be null)
   */
  public FilePart(FileReference file) {
    super();
    this.file = file;
  }

  /**
   * Creates a new FilePart with file reference and metadata.
   *
   * @param file the file reference (must not be null)
   * @param metadata optional metadata for this part
   */
  public FilePart(FileReference file, Map<String, Object> metadata) {
    super(metadata);
    this.file = file;
  }

  @Override
  @JsonProperty("kind")
  @NotNull
  public String getKind() {
    return KIND;
  }

  /**
   * Gets the file reference.
   *
   * @return the file reference containing content or URI
   */
  public FileReference getFile() {
    return file;
  }

  /**
   * Sets the file reference.
   *
   * @param file the file reference (must not be null)
   */
  public void setFile(FileReference file) {
    this.file = file;
  }

  /**
   * Builder-style method to set the file reference.
   *
   * @param file the file reference
   * @return this FilePart instance for method chaining
   */
  public FilePart withFile(FileReference file) {
    this.file = file;
    return this;
  }

  /**
   * Creates a new FilePart with a file containing bytes.
   *
   * @param bytes base64 encoded file content
   * @return a new FilePart instance
   */
  public static FilePart ofBytes(String bytes) {
    return new FilePart(new FileWithBytes(bytes));
  }

  /**
   * Creates a new FilePart with a file containing bytes and metadata.
   *
   * @param bytes base64 encoded file content
   * @param name filename
   * @param mimeType MIME type
   * @return a new FilePart instance
   */
  public static FilePart ofBytes(String bytes, String name, String mimeType) {
    return new FilePart(new FileWithBytes(bytes, name, mimeType));
  }

  /**
   * Creates a new FilePart with a file URI reference.
   *
   * @param uri URI to file content
   * @return a new FilePart instance
   */
  public static FilePart ofUri(String uri) {
    return new FilePart(new FileWithUri(uri));
  }

  /**
   * Creates a new FilePart with a file URI reference and metadata.
   *
   * @param uri URI to file content
   * @param name filename
   * @param mimeType MIME type
   * @return a new FilePart instance
   */
  public static FilePart ofUri(String uri, String name, String mimeType) {
    return new FilePart(new FileWithUri(uri, name, mimeType));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FilePart filePart = (FilePart) o;
    return Objects.equals(file, filePart.file)
        && Objects.equals(getMetadata(), filePart.getMetadata());
  }

  @Override
  public int hashCode() {
    return Objects.hash(file, getMetadata());
  }

  @Override
  public String toString() {
    return "FilePart{" + "file=" + file + ", metadata=" + getMetadata() + '}';
  }
}
