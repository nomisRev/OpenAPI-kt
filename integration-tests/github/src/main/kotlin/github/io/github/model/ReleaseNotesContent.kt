package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Generated name and body describing a release
 */
@Serializable
public data class ReleaseNotesContent(
  public val name: String,
  public val body: String,
)
