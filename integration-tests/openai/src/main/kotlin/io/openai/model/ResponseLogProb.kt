package io.openai.model

import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A logprob is the logarithmic probability that the model assigns to producing 
 * a particular token at a given position in the sequence. Less-negative (higher) 
 * logprob values indicate greater model confidence in that token choice.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ResponseLogProb(
  public val token: String,
  public val logprob: Double,
  @SerialName("top_logprobs")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val topLogprobs: List<TopLogprobs>? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class TopLogprobs(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val token: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val logprob: Double? = null,
  )
}
