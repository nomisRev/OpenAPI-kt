package io.github.nomisrev.openapi

import kotlinx.serialization.Serializable

/** Allows referencing an external resource for extended documentation. */
@Serializable
public data class ExternalDocs(
  /**
   * A short description of the target documentation. CommonMark syntax MAY be used for rich text
   * representation.
   */
  public val description: String? = null,
  /** The URL for the target documentation. Value MUST be in the format of a URL. */
  public val url: String,
)
