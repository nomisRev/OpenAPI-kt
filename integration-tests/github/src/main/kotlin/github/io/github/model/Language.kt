package io.github.model

import kotlin.Long
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Language
 */
@JvmInline
@Serializable
public value class Language(
  public val values: List<Long>? = null,
)
