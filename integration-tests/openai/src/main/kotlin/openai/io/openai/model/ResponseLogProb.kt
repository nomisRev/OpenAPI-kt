package io.openai.model

import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A logprob is the logarithmic probability that the model assigns to producing 
 * a particular token at a given position in the sequence. Less-negative (higher) 
 * logprob values indicate greater model confidence in that token choice.
 *
 */
@Serializable
public data class ResponseLogProb(
  public val token: String,
  public val logprob: Double,
  @SerialName("top_logprobs")
  public val topLogprobs: List<TopLogprobs>? = null,
) {
  @Serializable
  public data class TopLogprobs(
    public val token: String? = null,
    public val logprob: Double? = null,
  )
}
