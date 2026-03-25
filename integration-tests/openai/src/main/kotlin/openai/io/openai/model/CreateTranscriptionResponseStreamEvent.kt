package io.openai.model

import kotlin.Double
import kotlin.Float
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.jsonObject

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface CreateTranscriptionResponseStreamEvent {
  /**
   * Emitted when a diarized transcription returns a completed segment with speaker information. Only emitted when you [create a transcription](/docs/api-reference/audio/create-transcription) with `stream` set to `true` and `response_format` set to `diarized_json`.
   *
   */
  @SerialName("transcript.text.segment")
  @Serializable
  public data class TranscriptTextSegment(
    public val id: String,
    public val start: Float,
    public val end: Float,
    public val text: String,
    public val speaker: String,
  ) : CreateTranscriptionResponseStreamEvent

  /**
   * Emitted when there is an additional text delta. This is also the first event emitted when the transcription starts. Only emitted when you [create a transcription](/docs/api-reference/audio/create-transcription) with the `Stream` parameter set to `true`.
   */
  @SerialName("transcript.text.delta")
  @Serializable
  public data class TranscriptTextDelta(
    public val delta: String,
    public val logprobs: List<Logprobs>? = null,
    @SerialName("segment_id")
    public val segmentId: String? = null,
  ) : CreateTranscriptionResponseStreamEvent {
    @Serializable
    public data class Logprobs(
      public val token: String? = null,
      public val logprob: Double? = null,
      public val bytes: List<Long>? = null,
    )
  }

  /**
   * Emitted when the transcription is complete. Contains the complete transcription text. Only emitted when you [create a transcription](/docs/api-reference/audio/create-transcription) with the `Stream` parameter set to `true`.
   */
  @SerialName("transcript.text.done")
  @Serializable
  public data class TranscriptTextDone(
    public val text: String,
    public val logprobs: List<Logprobs>? = null,
    public val usage: TranscriptTextUsageTokens? = null,
  ) : CreateTranscriptionResponseStreamEvent {
    @Serializable
    public data class Logprobs(
      public val token: String? = null,
      public val logprob: Double? = null,
      public val bytes: List<Long>? = null,
    )
  }
}
