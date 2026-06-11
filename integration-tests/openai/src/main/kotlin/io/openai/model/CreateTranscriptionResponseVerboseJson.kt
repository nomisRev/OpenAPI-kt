package io.openai.model

import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Represents a verbose json transcription response returned by model, based on the provided input.
 */
@Serializable
public data class CreateTranscriptionResponseVerboseJson(
  public val language: String,
  public val duration: Double,
  public val text: String,
  public val words: List<TranscriptionWord>? = null,
  public val segments: List<TranscriptionSegment>? = null,
  public val usage: TranscriptTextUsageDuration? = null,
)
