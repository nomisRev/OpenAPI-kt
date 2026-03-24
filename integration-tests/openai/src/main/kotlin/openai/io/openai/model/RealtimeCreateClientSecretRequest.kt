package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * Create a session and client secret for the Realtime API. The request can specify
 * either a realtime or a transcription session configuration.
 * [Learn more about the Realtime API](/docs/guides/realtime).
 *
 */
@Serializable
public data class RealtimeCreateClientSecretRequest(
  @SerialName("expires_after")
  public val expiresAfter: ExpiresAfter? = null,
  public val session: Session? = null,
) {
  /**
   * Configuration for the client secret expiration. Expiration refers to the time after which
   * a client secret will no longer be valid for creating sessions. The session itself may
   * continue after that time once started. A secret can be used to create multiple sessions
   * until it expires.
   *
   */
  @Serializable
  public data class ExpiresAfter(
    public val anchor: Anchor? = null,
    public val seconds: Long? = null,
  ) {
    @Serializable
    public enum class Anchor(
      public val `value`: String,
    ) {
      @SerialName("created_at")
      CreatedAt("created_at"),
      ;
    }
  }

  /**
   * Session configuration to use for the client secret. Choose either a realtime
   * session or a transcription session.
   *
   */
  @Serializable(with = Session.Serializer::class)
  public sealed interface Session {
    @Serializable
    @JvmInline
    public value class CaseRealtimeSessionCreateRequestGA(
      public val `value`: RealtimeSessionCreateRequestGA,
    ) : Session

    @Serializable
    @JvmInline
    public value class CaseRealtimeTranscriptionSessionCreateRequestGA(
      public val `value`: RealtimeTranscriptionSessionCreateRequestGA,
    ) : Session

    public object Serializer : KSerializer<Session> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.RealtimeCreateClientSecretRequest.Session", PolymorphicKind.SEALED) {
        element("CaseRealtimeSessionCreateRequestGA", RealtimeSessionCreateRequestGA.serializer().descriptor)
        element("CaseRealtimeTranscriptionSessionCreateRequestGA", RealtimeTranscriptionSessionCreateRequestGA.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Session {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseRealtimeSessionCreateRequestGA::class to { CaseRealtimeSessionCreateRequestGA(decodeFromJsonElement(RealtimeSessionCreateRequestGA.serializer(), it)) },
          CaseRealtimeTranscriptionSessionCreateRequestGA::class to { CaseRealtimeTranscriptionSessionCreateRequestGA(decodeFromJsonElement(RealtimeTranscriptionSessionCreateRequestGA.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Session) {
        when(value) {
          is CaseRealtimeSessionCreateRequestGA -> encoder.encodeSerializableValue(RealtimeSessionCreateRequestGA.serializer(), value.value)
          is CaseRealtimeTranscriptionSessionCreateRequestGA -> encoder.encodeSerializableValue(RealtimeTranscriptionSessionCreateRequestGA.serializer(), value.value)
        }
      }
    }
  }
}
