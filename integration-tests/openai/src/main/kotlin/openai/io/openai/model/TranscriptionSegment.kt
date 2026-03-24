package io.openai.model

import kotlin.Float
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class TranscriptionSegment(
  public val id: Long,
  public val seek: Long,
  public val start: Float,
  public val end: Float,
  public val text: String,
  public val tokens: List<Long>,
  public val temperature: Float,
  @SerialName("avg_logprob")
  public val avgLogprob: Float,
  @SerialName("compression_ratio")
  public val compressionRatio: Float,
  @SerialName("no_speech_prob")
  public val noSpeechProb: Float,
)
