package io.openai.model

import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateTranslationResponseVerboseJson(
  public val language: String,
  public val duration: Double,
  public val text: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val segments: List<TranscriptionSegment>? = null,
)
