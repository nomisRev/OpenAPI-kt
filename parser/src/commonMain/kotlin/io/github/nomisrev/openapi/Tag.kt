package io.github.nomisrev.openapi

import kotlinx.serialization.Serializable

/**
 * Allows adding metadata to a single tag that is used by @Operation@. It is not mandatory to have
 * a @Tag@ per tag used there.
 */
@Serializable
public data class Tag(
  /** The name of the tag. */
  public val name: String,
  /**
   * A short description for the tag. [CommonMark syntax](https://spec.commonmark.org/) MAY be used
   * for rich text representation.
   */
  public val description: String? = null,
  /** Additional external documentation for this tag. */
  public val externalDocs: ExternalDocs? = null,
)
