package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Hypermedia Link
 */
@JvmInline
@Serializable
public value class Link(
  public val href: String,
)
