package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Response from creating a session and client secret for the Realtime API.
 *
 */
@Serializable
public data class RealtimeCreateClientSecretResponse(
  public val `value`: String,
  @SerialName("expires_at")
  public val expiresAt: Long,
  public val session: Session,
) {
  /**
   * The session configuration for either a realtime or transcription session.
   *
   */
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Session {
    @Serializable
    @JvmInline
    @SerialName("RealtimeSessionCreateResponseGA")
    public value class RealtimeSessionCreateResponseGA(
      public val `value`: io.openai.model.RealtimeSessionCreateResponseGA,
    ) : Session

    @Serializable
    @JvmInline
    @SerialName("RealtimeTranscriptionSessionCreateResponseGA")
    public value class RealtimeTranscriptionSessionCreateResponseGA(
      public val `value`: io.openai.model.RealtimeTranscriptionSessionCreateResponseGA,
    ) : Session
  }
}
