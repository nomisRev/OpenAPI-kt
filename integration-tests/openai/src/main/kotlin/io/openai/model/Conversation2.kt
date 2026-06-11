package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The conversation that this response belonged to. Input items and output items from this response were automatically added to this conversation.
 */
@JvmInline
@Serializable
public value class Conversation2(
  public val id: String,
)
