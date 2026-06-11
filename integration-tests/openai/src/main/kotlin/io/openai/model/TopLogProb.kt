package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * The top log probability of a token.
 */
@Serializable
public data class TopLogProb(
  public val token: String,
  public val logprob: Double,
  public val bytes: List<Long>,
)
