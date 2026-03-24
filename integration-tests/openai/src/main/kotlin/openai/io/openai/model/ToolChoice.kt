package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Tool selection that the assistant should honor when executing the item.
 */
@JvmInline
@Serializable
public value class ToolChoice(
  public val id: String,
)
