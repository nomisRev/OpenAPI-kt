package io.openai.model

import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class CreateTranslationResponseVerboseJson(
  public val language: String,
  public val duration: Double,
  public val text: String,
  public val segments: List<TranscriptionSegment>? = null,
)
