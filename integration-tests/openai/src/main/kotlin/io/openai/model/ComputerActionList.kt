package io.openai.model

import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Flattened batched actions for `computer_use`. Each action includes an
 * `type` discriminator and action-specific fields.
 *
 */
@JvmInline
@Serializable
public value class ComputerActionList(
  public val items: List<ComputerAction>,
)
