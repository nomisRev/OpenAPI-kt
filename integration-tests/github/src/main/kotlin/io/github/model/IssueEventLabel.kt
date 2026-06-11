package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Issue Event Label
 */
@Serializable
public data class IssueEventLabel(
  public val name: String?,
  public val color: String?,
)
