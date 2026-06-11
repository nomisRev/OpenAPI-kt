package io.openai.model

import kotlin.Float
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A segment of diarized transcript text with speaker metadata.
 */
@Serializable
public data class TranscriptionDiarizedSegment(
  public val type: Type,
  public val id: String,
  public val start: Float,
  public val end: Float,
  public val text: String,
  public val speaker: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("transcript.text.segment")
    TranscriptTextSegment("transcript.text.segment"),
    ;
  }
}
