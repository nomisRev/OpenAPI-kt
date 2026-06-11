package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Issue Event Rename
 */
@Serializable
public data class IssueEventRename(
  public val from: String,
  public val to: String,
)
