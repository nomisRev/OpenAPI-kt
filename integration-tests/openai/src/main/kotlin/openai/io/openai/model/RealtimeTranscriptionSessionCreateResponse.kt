package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A new Realtime transcription session configuration.
 *
 * When a session is created on the server via REST API, the session object
 * also contains an ephemeral key. Default TTL for keys is 10 minutes. This
 * property is not present when a session is updated via the WebSocket API.
 *
 */
@Serializable
public data class RealtimeTranscriptionSessionCreateResponse(
  @SerialName("client_secret")
  public val clientSecret: ClientSecret,
  public val modalities: List<Modalities>? = null,
  @SerialName("input_audio_format")
  public val inputAudioFormat: String? = null,
  @SerialName("input_audio_transcription")
  public val inputAudioTranscription: AudioTranscription? = null,
  @SerialName("turn_detection")
  public val turnDetection: TurnDetection? = null,
) {
  /**
   * Ephemeral key returned by the API. Only present when the session is
   * created on the server via REST API.
   *
   */
  @Serializable
  public data class ClientSecret(
    public val `value`: String,
    @SerialName("expires_at")
    public val expiresAt: Long,
  )

  @Serializable
  public enum class Modalities(
    public val `value`: String,
  ) {
    @SerialName("text")
    Text("text"),
    @SerialName("audio")
    Audio("audio"),
    ;
  }

  /**
   * Configuration for turn detection. Can be set to `null` to turn off. Server
   * VAD means that the model will detect the start and end of speech based on
   * audio volume and respond at the end of user speech.
   *
   */
  @Serializable
  public data class TurnDetection(
    public val type: String? = null,
    public val threshold: Double? = null,
    @SerialName("prefix_padding_ms")
    public val prefixPaddingMs: Long? = null,
    @SerialName("silence_duration_ms")
    public val silenceDurationMs: Long? = null,
  )
}
