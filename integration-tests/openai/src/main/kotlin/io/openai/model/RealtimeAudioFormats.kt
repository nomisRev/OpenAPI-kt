package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class AudioPcm(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val type: Type? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val rate: Rate? = null,
  ) : RealtimeAudioFormats {
    @Serializable(with = Rate.Serializer::class)
    public enum class Rate(
      public val `value`: Long,
    ) {
      `24000`(24_000L),
      ;

      internal object Serializer : KSerializer<Rate> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Rate", PrimitiveKind.LONG)

        override fun serialize(encoder: Encoder, `value`: Rate) {
          encoder.encodeLong(value.value)
        }

        override fun deserialize(decoder: Decoder): Rate {
          val value = decoder.decodeLong()
          val member = Rate.entries.firstOrNull { it.value == value }
          return member ?: throw SerializationException("""Rate has no member with value $value""")
        }
      }
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class AudioPcmu(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class AudioPcma(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
