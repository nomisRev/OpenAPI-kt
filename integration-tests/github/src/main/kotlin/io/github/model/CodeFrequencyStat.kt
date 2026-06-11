package io.github.model

import kotlin.Long
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Code Frequency Stat
 */
@JvmInline
@Serializable
public value class CodeFrequencyStat(
  public val items: List<Long>,
)
