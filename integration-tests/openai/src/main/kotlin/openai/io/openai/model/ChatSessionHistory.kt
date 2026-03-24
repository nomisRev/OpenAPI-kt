package io.openai.model

import kotlin.Boolean
import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * History retention preferences returned for the session.
 */
@Serializable
public data class ChatSessionHistory(
  public val enabled: Boolean,
  @SerialName("recent_threads")
  public val recentThreads: Long?,
)
