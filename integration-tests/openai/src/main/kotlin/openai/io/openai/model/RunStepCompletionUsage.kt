package io.openai.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Usage statistics related to the run step. This value will be `null` while the run step's status is `in_progress`.
 */
@Serializable
public data class RunStepCompletionUsage(
  @SerialName("completion_tokens")
  public val completionTokens: Long,
  @SerialName("prompt_tokens")
  public val promptTokens: Long,
  @SerialName("total_tokens")
  public val totalTokens: Long,
)
