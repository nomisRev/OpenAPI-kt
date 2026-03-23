package io.github.model

import kotlin.Boolean
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Whether actions must be pinned to a full-length commit SHA.
 */
@JvmInline
@Serializable
public value class ShaPinningRequired(
  public val `value`: Boolean,
)
