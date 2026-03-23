package io.github.model

import kotlin.Boolean
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Whether GitHub Actions is enabled on the repository.
 */
@JvmInline
@Serializable
public value class ActionsEnabled(
  public val `value`: Boolean,
)
