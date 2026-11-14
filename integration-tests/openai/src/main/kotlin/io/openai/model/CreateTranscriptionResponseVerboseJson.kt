package io.openai.model

import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Represents a verbose json transcription response returned by model, based on the provided input.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateTranscriptionResponseVerboseJson(
  public val language: String,
  public val duration: Double,
  public val text: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val words: List<TranscriptionWord>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val segments: List<TranscriptionSegment>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val usage: TranscriptTextUsageDuration? = null,
)
