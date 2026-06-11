package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The log probability of a token.
 */
@Serializable
public data class LogProb(
  public val token: String,
  public val logprob: Double,
  public val bytes: List<Long>,
  @SerialName("top_logprobs")
  public val topLogprobs: List<TopLogProb>,
)
