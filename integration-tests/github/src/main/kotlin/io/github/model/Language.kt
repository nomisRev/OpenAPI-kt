package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Language
 */
@JvmInline
@Serializable
public value class Language(
  public val values: Map<String, Long>,
)
