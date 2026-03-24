package io.openai.model

import kotlin.Float
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when a diarized transcription returns a completed segment with speaker information. Only emitted when you [create a transcription](/docs/api-reference/audio/create-transcription) with `stream` set to `true` and `response_format` set to `diarized_json`.
 *
 */
@Serializable
public data class TranscriptTextSegmentEvent(
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
