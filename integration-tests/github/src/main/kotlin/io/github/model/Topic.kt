package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * A topic aggregates entities that are related to a subject.
 */
@Serializable
public data class Topic(
  public val names: List<String>,
)
