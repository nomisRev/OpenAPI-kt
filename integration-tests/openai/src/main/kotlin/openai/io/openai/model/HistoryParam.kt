package io.openai.model

import kotlin.Boolean
import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Controls how much historical context is retained for the session.
 */
@Serializable
public data class HistoryParam(
  public val enabled: Boolean? = null,
  @SerialName("recent_threads")
  public val recentThreads: Long? = null,
)
