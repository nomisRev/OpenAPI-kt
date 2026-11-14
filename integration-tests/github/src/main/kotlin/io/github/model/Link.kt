package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Hypermedia Link
 */
@Serializable
public data class Link(
  public val href: String,
)
