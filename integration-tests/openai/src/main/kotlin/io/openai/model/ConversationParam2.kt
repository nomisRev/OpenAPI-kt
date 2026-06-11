package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The conversation that this response belongs to.
 */
@JvmInline
@Serializable
public value class ConversationParam2(
  public val id: String,
)
