package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * A topic aggregates entities that are related to a subject.
 */
@JvmInline
@Serializable
public value class Topic(
  public val names: List<String>,
)
