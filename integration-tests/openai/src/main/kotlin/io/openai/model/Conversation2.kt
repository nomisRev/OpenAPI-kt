package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The conversation that this response belonged to. Input items and output items from this response were automatically added to this conversation.
 */
@Serializable
public data class Conversation2(
  public val id: String,
)
