package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when there is an additional text delta. This is also the first event emitted when the transcription starts. Only emitted when you [create a transcription](/docs/api-reference/audio/create-transcription) with the `Stream` parameter set to `true`.
 */
@Serializable
public data class TranscriptTextDeltaEvent(
  public val type: Type,
  public val delta: String,
  public val logprobs: List<Logprobs>? = null,
  @SerialName("segment_id")
  public val segmentId: String? = null,
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
    @SerialName("transcript.text.delta")
    TranscriptTextDelta("transcript.text.delta"),
    ;
  }
}
