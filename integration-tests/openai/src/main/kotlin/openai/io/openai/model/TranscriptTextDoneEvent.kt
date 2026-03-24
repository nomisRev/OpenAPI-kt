package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when the transcription is complete. Contains the complete transcription text. Only emitted when you [create a transcription](/docs/api-reference/audio/create-transcription) with the `Stream` parameter set to `true`.
 */
@Serializable
public data class TranscriptTextDoneEvent(
  public val type: Type,
  public val text: String,
  public val logprobs: List<Logprobs>? = null,
  public val usage: TranscriptTextUsageTokens? = null,
) {
  @Serializable
  public data class Logprobs(
    public val token: String? = null,
    public val logprob: Double? = null,
    public val bytes: List<Long>? = null,
  )

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("transcript.text.done")
    TranscriptTextDone("transcript.text.done"),
    ;
  }
}
