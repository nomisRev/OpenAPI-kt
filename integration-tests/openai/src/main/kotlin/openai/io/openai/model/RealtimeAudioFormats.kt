package io.openai.model

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

@Serializable(with = RealtimeAudioFormats.Serializer::class)
public sealed interface RealtimeAudioFormats {
  /**
   * The PCM audio format. Only a 24kHz sample rate is supported.
   */
  @Serializable
  public data class AudioPcm(
    public val type: Type? = null,
    public val rate: Rate? = null,
  ) : RealtimeAudioFormats {
    @Serializable
    public enum class Rate {
      `24000`,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("audio/pcm")
      AudioPcm("audio/pcm"),
      ;
    }
  }

  /**
   * The G.711 μ-law format.
   */
  @JvmInline
  @Serializable
  public value class AudioPcmu(
    public val type: Type? = null,
  ) : RealtimeAudioFormats {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("audio/pcmu")
      AudioPcmu("audio/pcmu"),
      ;
    }
  }

  /**
   * The G.711 A-law format.
   */
  @JvmInline
  @Serializable
  public value class AudioPcma(
    public val type: Type? = null,
  ) : RealtimeAudioFormats {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("audio/pcma")
      AudioPcma("audio/pcma"),
      ;
    }
  }

  public object Serializer : KSerializer<RealtimeAudioFormats> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.RealtimeAudioFormats", PolymorphicKind.SEALED) {
      element("AudioPcm", AudioPcm.serializer().descriptor)
      element("AudioPcmu", AudioPcmu.serializer().descriptor)
      element("AudioPcma", AudioPcma.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): RealtimeAudioFormats {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        AudioPcm::class to { decodeFromJsonElement(AudioPcm.serializer(), it) },
        AudioPcmu::class to { decodeFromJsonElement(AudioPcmu.serializer(), it) },
        AudioPcma::class to { decodeFromJsonElement(AudioPcma.serializer(), it) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: RealtimeAudioFormats) {
      when(value) {
        is AudioPcm -> encoder.encodeSerializableValue(AudioPcm.serializer(), value)
        is AudioPcmu -> encoder.encodeSerializableValue(AudioPcmu.serializer(), value)
        is AudioPcma -> encoder.encodeSerializableValue(AudioPcma.serializer(), value)
      }
    }
  }
}
