package io.openai.model

import kotlin.Boolean
import kotlinx.serialization.Serializable

/**
 * Automatic thread title preferences for the session.
 */
@Serializable
public data class ChatSessionAutomaticThreadTitling(
  public val enabled: Boolean,
)
