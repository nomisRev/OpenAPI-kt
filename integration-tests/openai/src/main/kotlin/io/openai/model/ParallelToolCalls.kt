package io.openai.model

import kotlin.Boolean
import kotlin.jvm.JvmInline
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

/**
 * Whether to enable [parallel function calling](/docs/guides/function-calling#configuring-parallel-function-calling) during tool use.
 */
@JvmInline
@Serializable
public value class ParallelToolCalls(
  @Required
  public val `value`: Boolean = true,
)
