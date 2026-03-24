package io.openai.model

import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Represents a diarized transcription response returned by the model, including the combined transcript and speaker-segment annotations.
 *
 */
@Serializable
public data class CreateTranscriptionResponseDiarizedJson(
  public val task: Task,
  public val duration: Double,
  public val text: String,
  public val segments: List<TranscriptionDiarizedSegment>,
  public val usage: JsonElement? = null,
) {
  @Serializable
  public enum class Task(
    public val `value`: String,
  ) {
    @SerialName("transcribe")
    Transcribe("transcribe"),
    ;
  }
}
