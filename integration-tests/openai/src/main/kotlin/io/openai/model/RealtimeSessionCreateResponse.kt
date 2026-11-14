package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.EncodeDefault
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
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull

/**
 * A Realtime session configuration object.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RealtimeSessionCreateResponse(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val `object`: String? = null,
  @SerialName("expires_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val expiresAt: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val include: List<Include>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val model: String? = null,
  @SerialName("output_modalities")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val outputModalities: List<OutputModalities>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val instructions: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val audio: Audio? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val tracing: Tracing? = null,
  @SerialName("turn_detection")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val turnDetection: TurnDetection? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val tools: List<RealtimeFunctionTool>? = null,
  @SerialName("tool_choice")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val toolChoice: String? = null,
  @SerialName("max_output_tokens")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxOutputTokens: MaxOutputTokens? = null,
) {
  /**
   * Configuration for input and output audio for the session.
   *
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Audio(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val input: Input? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val output: Output? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Input(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val format: RealtimeAudioFormats? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val transcription: AudioTranscription? = null,
      @SerialName("noise_reduction")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val noiseReduction: NoiseReduction? = null,
      @SerialName("turn_detection")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val turnDetection: TurnDetection? = null,
    ) {
      /**
       * Configuration for input audio noise reduction.
       *
       */
      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class NoiseReduction(
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val type: NoiseReductionType? = null,
      )

      /**
       * Configuration for turn detection.
       *
       */
      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class TurnDetection(
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val type: String? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val threshold: Double? = null,
        @SerialName("prefix_padding_ms")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val prefixPaddingMs: Long? = null,
        @SerialName("silence_duration_ms")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val silenceDurationMs: Long? = null,
      )
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Output(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val format: RealtimeAudioFormats? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val voice: VoiceIdsShared? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val speed: Double? = null,
    )
  }

  @Serializable
  public enum class Include(
    public val `value`: String,
  ) {
    @SerialName("item.input_audio_transcription.logprobs")
    ItemInputAudioTranscriptionLogprobs("item.input_audio_transcription.logprobs"),
    ;
  }

  /**
   * Maximum number of output tokens for a single assistant response,
   * inclusive of tool calls. Provide an integer between 1 and 4096 to
   * limit output tokens, or `inf` for the maximum available tokens for a
   * given model. Defaults to `inf`.
   *
   */
  @Serializable(with = MaxOutputTokens.Serializer::class)
  public sealed interface MaxOutputTokens {
    @Serializable
    @JvmInline
    public value class CaseLong(
      public val `value`: Long,
    ) : MaxOutputTokens

    @Serializable
    public enum class Inf(
      public val `value`: String,
    ) : MaxOutputTokens {
      @SerialName("inf")
      Inf("inf"),
      ;
    }

    public object Serializer : KSerializer<MaxOutputTokens> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.RealtimeSessionCreateResponse.MaxOutputTokens", PolymorphicKind.SEALED) {
        element("CaseLong", Long.serializer().descriptor)
        element("Inf", Inf.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): MaxOutputTokens {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return when {
          value is JsonPrimitive && value.isString -> json.attemptDeserialize(
            value,
            Inf::class to { decodeFromJsonElement(Inf.serializer(), it) },
          )
          value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
            value,
            CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
          )
          else -> throw UnionSerializationException(value, emptyMap())
        }
      }

      override fun serialize(encoder: Encoder, `value`: MaxOutputTokens) {
        when(value) {
          is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
          is Inf -> encoder.encodeSerializableValue(Inf.serializer(), value)
        }
      }
    }
  }

  @Serializable
  public enum class OutputModalities(
    public val `value`: String,
  ) {
    @SerialName("text")
    Text("text"),
    @SerialName("audio")
    Audio("audio"),
    ;
  }

  /**
   * Configuration options for tracing. Set to null to disable tracing. Once
   * tracing is enabled for a session, the configuration cannot be modified.
   *
   * `auto` will create a trace for the session with default values for the
   * workflow name, group id, and metadata.
   *
   */
  @Serializable(with = Tracing.Serializer::class)
  public sealed interface Tracing {
    @Serializable
    public enum class Auto(
      public val `value`: String,
    ) : Tracing {
      @SerialName("auto")
      Auto("auto"),
      ;
    }

    /**
     * Granular configuration for tracing.
     *
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class TracingConfiguration(
      @SerialName("workflow_name")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val workflowName: String? = null,
      @SerialName("group_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val groupId: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val metadata: JsonElement? = null,
    ) : Tracing

    public object Serializer : KSerializer<Tracing> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.RealtimeSessionCreateResponse.Tracing", PolymorphicKind.SEALED) {
        element("Auto", Auto.serializer().descriptor)
        element("TracingConfiguration", TracingConfiguration.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Tracing {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return when {
          value is JsonPrimitive && value.isString -> json.attemptDeserialize(
            value,
            Auto::class to { decodeFromJsonElement(Auto.serializer(), it) },
          )
          value is JsonObject -> json.attemptDeserialize(
            value,
            TracingConfiguration::class to { decodeFromJsonElement(TracingConfiguration.serializer(), it) },
          )
          else -> throw UnionSerializationException(value, emptyMap())
        }
      }

      override fun serialize(encoder: Encoder, `value`: Tracing) {
        when(value) {
          is Auto -> encoder.encodeSerializableValue(Auto.serializer(), value)
          is TracingConfiguration -> encoder.encodeSerializableValue(TracingConfiguration.serializer(), value)
        }
      }
    }
  }

  /**
   * Configuration for turn detection. Can be set to `null` to turn off. Server
   * VAD means that the model will detect the start and end of speech based on
   * audio volume and respond at the end of user speech.
   *
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class TurnDetection(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val type: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val threshold: Double? = null,
    @SerialName("prefix_padding_ms")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val prefixPaddingMs: Long? = null,
    @SerialName("silence_duration_ms")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val silenceDurationMs: Long? = null,
  )
}
