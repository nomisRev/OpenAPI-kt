package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Hypermedia Link with Type
 */
@Serializable
public data class LinkWithType(
  public val href: String,
  public val type: String,
)
