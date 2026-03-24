package io.openai.model

import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable
public data class CreateTranscriptionRequest(
  public val `file`: ByteArray,
  public val model: Model,
  public val language: String? = null,
  public val prompt: String? = null,
  @SerialName("response_format")
  public val responseFormat: AudioResponseFormat? = null,
  public val temperature: Double? = null,
  public val include: List<TranscriptionInclude>? = null,
  @SerialName("timestamp_granularities")
  public val timestampGranularities: List<TimestampGranularities>? = null,
  public val stream: Boolean? = null,
  @SerialName("chunking_strategy")
  public val chunkingStrategy: ChunkingStrategy? = null,
  @SerialName("known_speaker_names")
  public val knownSpeakerNames: List<String>? = null,
  @SerialName("known_speaker_references")
  public val knownSpeakerReferences: List<String>? = null,
) {
  /**
   * Controls how the audio is cut into chunks. When set to `"auto"`, the server first normalizes loudness and then uses voice activity detection (VAD) to choose boundaries. `server_vad` object can be provided to tweak VAD detection parameters manually. If unset, the audio is transcribed as a single block. Required when using `gpt-4o-transcribe-diarize` for inputs longer than 30 seconds. 
   */
  @Serializable(with = ChunkingStrategy.Serializer::class)
  public sealed interface ChunkingStrategy {
    @Serializable
    public enum class Auto(
      public val `value`: String,
    ) : ChunkingStrategy {
      @SerialName("auto")
      Auto("auto"),
      ;
    }

    @Serializable
    @JvmInline
    public value class CaseVadConfig(
      public val `value`: VadConfig,
    ) : ChunkingStrategy

    public object Serializer : KSerializer<ChunkingStrategy> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.CreateTranscriptionRequest.ChunkingStrategy", PolymorphicKind.SEALED) {
        element("Auto", Auto.serializer().descriptor)
        element("CaseVadConfig", VadConfig.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): ChunkingStrategy {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          Auto::class to { decodeFromJsonElement(Auto.serializer(), it) },
          CaseVadConfig::class to { CaseVadConfig(decodeFromJsonElement(VadConfig.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: ChunkingStrategy) {
        when(value) {
          is Auto -> encoder.encodeSerializableValue(Auto.serializer(), value)
          is CaseVadConfig -> encoder.encodeSerializableValue(VadConfig.serializer(), value.value)
        }
      }
    }
  }

  @Serializable(with = Model.Serializer::class)
  public sealed interface Model {
    public val `value`: String

    @Serializable
    @JvmInline
    public value class CaseString(
      override val `value`: String,
    ) : Model

    @Serializable
    public enum class CaseEnum(
      override val `value`: String,
    ) : Model {
      @SerialName("whisper-1")
      Whisper1("whisper-1"),
      @SerialName("gpt-4o-transcribe")
      Gpt4oTranscribe("gpt-4o-transcribe"),
      @SerialName("gpt-4o-mini-transcribe")
      Gpt4oMiniTranscribe("gpt-4o-mini-transcribe"),
      @SerialName("gpt-4o-mini-transcribe-2025-12-15")
      Gpt4oMiniTranscribe20251215("gpt-4o-mini-transcribe-2025-12-15"),
      @SerialName("gpt-4o-transcribe-diarize")
      Gpt4oTranscribeDiarize("gpt-4o-transcribe-diarize"),
      ;
    }

    public object Serializer : KSerializer<Model> {
      override val descriptor: SerialDescriptor = String.serializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Model) {
        when(value) {
          CaseEnum.Whisper1 -> encoder.encodeString("whisper-1")
          CaseEnum.Gpt4oTranscribe -> encoder.encodeString("gpt-4o-transcribe")
          CaseEnum.Gpt4oMiniTranscribe -> encoder.encodeString("gpt-4o-mini-transcribe")
          CaseEnum.Gpt4oMiniTranscribe20251215 -> encoder.encodeString("gpt-4o-mini-transcribe-2025-12-15")
          CaseEnum.Gpt4oTranscribeDiarize -> encoder.encodeString("gpt-4o-transcribe-diarize")
          is CaseString -> encoder.encodeString(value.value)
        }
      }

      override fun deserialize(decoder: Decoder): Model = when(val value = decoder.decodeString()) {
        "whisper-1" -> CaseEnum.Whisper1
        "gpt-4o-transcribe" -> CaseEnum.Gpt4oTranscribe
        "gpt-4o-mini-transcribe" -> CaseEnum.Gpt4oMiniTranscribe
        "gpt-4o-mini-transcribe-2025-12-15" -> CaseEnum.Gpt4oMiniTranscribe20251215
        "gpt-4o-transcribe-diarize" -> CaseEnum.Gpt4oTranscribeDiarize
        else -> CaseString(value)
      }
    }
  }

  @Serializable
  public enum class TimestampGranularities(
    public val `value`: String,
  ) {
    @SerialName("word")
    Word("word"),
    @SerialName("segment")
    Segment("segment"),
    ;
  }
}
