package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ChatCompletionTokenLogprob(
  public val token: String,
  public val logprob: Double,
  public val bytes: List<Long>?,
  @SerialName("top_logprobs")
  public val topLogprobs: List<TopLogprobs>,
) {
  @Serializable
  public data class TopLogprobs(
    public val token: String,
    public val logprob: Double,
    public val bytes: List<Long>?,
  )
}
