package io.openai.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Usage statistics related to the run. This value will be `null` if the run is not in a terminal state (i.e. `in_progress`, `queued`, etc.).
 */
@Serializable
public data class RunCompletionUsage(
  @SerialName("completion_tokens")
  public val completionTokens: Long,
  @SerialName("prompt_tokens")
  public val promptTokens: Long,
  @SerialName("total_tokens")
  public val totalTokens: Long,
)
