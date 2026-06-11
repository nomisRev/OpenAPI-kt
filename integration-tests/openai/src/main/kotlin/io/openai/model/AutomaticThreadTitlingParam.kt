package io.openai.model

import kotlin.Boolean
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Controls whether ChatKit automatically generates thread titles.
 */
@JvmInline
@Serializable
public value class AutomaticThreadTitlingParam(
  public val enabled: Boolean? = null,
)
