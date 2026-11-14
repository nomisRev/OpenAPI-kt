package io.openai.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Controls how much historical context is retained for the session.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class HistoryParam(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val enabled: Boolean? = null,
  @SerialName("recent_threads")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val recentThreads: Long? = null,
)
