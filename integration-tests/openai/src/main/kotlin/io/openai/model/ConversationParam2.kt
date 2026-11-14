package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The conversation that this response belongs to.
 */
@Serializable
public data class ConversationParam2(
  public val id: String,
)
