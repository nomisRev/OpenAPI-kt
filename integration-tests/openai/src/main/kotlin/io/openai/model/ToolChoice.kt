package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Tool selection that the assistant should honor when executing the item.
 */
@Serializable
public data class ToolChoice(
  public val id: String,
)
