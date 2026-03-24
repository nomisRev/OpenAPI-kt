package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when the speech synthesis is complete and all audio has been streamed.
 */
@Serializable
public data class SpeechAudioDoneEvent(
  public val type: Type,
  public val usage: Usage,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("speech.audio.done")
    SpeechAudioDone("speech.audio.done"),
    ;
  }

  /**
   * Token usage statistics for the request.
   *
   */
  @Serializable
  public data class Usage(
    @SerialName("input_tokens")
    public val inputTokens: Long,
    @SerialName("output_tokens")
    public val outputTokens: Long,
    @SerialName("total_tokens")
    public val totalTokens: Long,
  )
}
