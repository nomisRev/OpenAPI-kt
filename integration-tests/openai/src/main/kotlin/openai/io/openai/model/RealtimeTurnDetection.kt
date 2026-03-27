package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * Configuration for turn detection, ether Server VAD or Semantic VAD. This can be set to `null` to turn off, in which case the client must manually trigger model response.
 *
 * Server VAD means that the model will detect the start and end of speech based on audio volume and respond at the end of user speech.
 *
 * Semantic VAD is more advanced and uses a turn detection model (in conjunction with VAD) to semantically estimate whether the user has finished speaking, then dynamically sets a timeout based on this probability. For example, if user audio trails off with "uhhm", the model will score a low probability of turn end and wait longer for the user to continue speaking. This can be useful for more natural conversations, but may have a higher latency.
 *
 */
@Serializable(with = RealtimeTurnDetection.Serializer::class)
public sealed interface RealtimeTurnDetection {
  /**
   * Server-side voice activity detection (VAD) which flips on when user speech is detected and off after a period of silence.
   */
  @Serializable
  public data class ServerVAD(
    @Required
    public val type: String = "server_vad",
    public val threshold: Double? = null,
    @SerialName("prefix_padding_ms")
    public val prefixPaddingMs: Long? = null,
    @SerialName("silence_duration_ms")
    public val silenceDurationMs: Long? = null,
    @SerialName("create_response")
    public val createResponse: Boolean? = null,
    @SerialName("interrupt_response")
    public val interruptResponse: Boolean? = null,
    @SerialName("idle_timeout_ms")
    public val idleTimeoutMs: Long? = null,
  ) : RealtimeTurnDetection

  /**
   * Server-side semantic turn detection which uses a model to determine when the user has finished speaking.
   */
  @Serializable
  public data class SemanticVAD(
    public val type: String,
    public val eagerness: Eagerness? = null,
    @SerialName("create_response")
    public val createResponse: Boolean? = null,
    @SerialName("interrupt_response")
    public val interruptResponse: Boolean? = null,
  ) : RealtimeTurnDetection {
    @Serializable
    public enum class Eagerness(
      public val `value`: String,
    ) {
      @SerialName("low")
      Low("low"),
      @SerialName("medium")
      Medium("medium"),
      @SerialName("high")
      High("high"),
      @SerialName("auto")
      Auto("auto"),
      ;
    }
  }

  public object Serializer : KSerializer<RealtimeTurnDetection> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.RealtimeTurnDetection", PolymorphicKind.SEALED) {
      element("ServerVAD", ServerVAD.serializer().descriptor)
      element("SemanticVAD", SemanticVAD.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): RealtimeTurnDetection {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      val obj = value as? JsonObject
      val tag = (obj?.get("type") as? JsonPrimitive)?.content
      when(tag) {
        else -> {
          return json.attemptDeserialize(
            value,
            ServerVAD::class to { decodeFromJsonElement(ServerVAD.serializer(), it) },
            SemanticVAD::class to { decodeFromJsonElement(SemanticVAD.serializer(), it) },
          )
        }
      }
    }

    override fun serialize(encoder: Encoder, `value`: RealtimeTurnDetection) {
      when(value) {
        is ServerVAD -> encoder.encodeSerializableValue(ServerVAD.serializer(), value)
        is SemanticVAD -> encoder.encodeSerializableValue(SemanticVAD.serializer(), value)
      }
    }
  }
}
