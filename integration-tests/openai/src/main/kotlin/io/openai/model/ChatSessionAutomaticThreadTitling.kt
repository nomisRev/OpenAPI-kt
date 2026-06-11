package io.openai.model

import kotlin.Boolean
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Automatic thread title preferences for the session.
 */
@JvmInline
@Serializable
public value class ChatSessionAutomaticThreadTitling(
  public val enabled: Boolean,
)
