package io.microagents.a2a.core.types.core;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.microagents.a2a.core.types.notifications.*;

/**
 * Abstract base class for file references in A2A FileParts.
 *
 * <p>Files can be represented either as direct byte content (FileWithBytes) or as a URI reference
 * (FileWithUri). This follows the A2A specification's union type for file handling.
 *
 * @see FileWithBytes
 * @see FileWithUri
 * @see FilePart
 * @since 0.1.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION, defaultImpl = FileWithBytes.class)
@JsonSubTypes({
  @JsonSubTypes.Type(value = FileWithBytes.class),
  @JsonSubTypes.Type(value = FileWithUri.class),
})
public abstract class FileReference {

  /** Optional filename for the file (e.g., "report.pdf"). */
  private String name;

  /**
   * Optional MIME type for the file (e.g., "image/png", "application/pdf"). Strongly recommended
   * for proper content handling.
   */
  private String mimeType;

  /** Default constructor for Jackson deserialization. */
  protected FileReference() {}

  /**
   * Creates a new FileReference with name and MIME type.
   *
   * @param name optional filename
   * @param mimeType optional MIME type
   */
  protected FileReference(String name, String mimeType) {
    this.name = name;
    this.mimeType = mimeType;
  }

  /**
   * Gets the optional filename.
   *
   * @return the filename, or null if not specified
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the optional filename.
   *
   * @param name the filename
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the optional MIME type.
   *
   * @return the MIME type, or null if not specified
   */
  public String getMimeType() {
    return mimeType;
  }

  /**
   * Sets the optional MIME type.
   *
   * @param mimeType the MIME type
   */
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   * Builder-style method to set the filename.
   *
   * @param name the filename
   * @return this FileReference instance for method chaining
   */
  public FileReference withName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Builder-style method to set the MIME type.
   *
   * @param mimeType the MIME type
   * @return this FileReference instance for method chaining
   */
  public FileReference withMimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }
}
