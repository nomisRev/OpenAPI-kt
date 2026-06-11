package io.openai.model

import kotlin.Float
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class TranscriptionWord(
  public val word: String,
  public val start: Float,
  public val end: Float,
)
