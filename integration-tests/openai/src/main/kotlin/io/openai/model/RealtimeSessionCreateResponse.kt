package io.openai.model

import kotlin.Double
import kotlin.Long
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

/**
 * A Realtime session configuration object.
 *
 */
@Serializable
public data class RealtimeSessionCreateResponse(
  public val id: String? = null,
  public val `object`: String? = null,
  @SerialName("expires_at")
  public val expiresAt: Long? = null,
  public val include: List<Include>? = null,
  public val model: String? = null,
  @SerialName("output_modalities")
  public val outputModalities: List<OutputModalities>? = null,
  public val instructions: String? = null,
  public val audio: Audio? = null,
  public val tracing: Tracing? = null,
  @SerialName("turn_detection")
  public val turnDetection: TurnDetection? = null,
  public val tools: List<RealtimeFunctionTool>? = null,
  @SerialName("tool_choice")
  public val toolChoice: String? = null,
  @SerialName("max_output_tokens")
  public val maxOutputTokens: MaxOutputTokens? = null,
) {
  /**
   * Configuration for input and output audio for the session.
   *
   */
  @Serializable
  public data class Audio(
    public val input: Input? = null,
    public val output: Output? = null,
  ) {
    @Serializable
    public data class Input(
      public val format: RealtimeAudioFormats? = null,
      public val transcription: AudioTranscription? = null,
      @SerialName("noise_reduction")
      public val noiseReduction: NoiseReduction? = null,
      @SerialName("turn_detection")
      public val turnDetection: TurnDetection? = null,
    ) {
      /**
       * Configuration for input audio noise reduction.
       *
       */
      @JvmInline
      @Serializable
      public value class NoiseReduction(
        public val type: NoiseReductionType? = null,
      )

      /**
       * Configuration for turn detection.
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

    @Serializable
    public data class Output(
      public val format: RealtimeAudioFormats? = null,
      public val voice: VoiceIdsShared? = null,
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
        return json.attemptDeserialize(
          value,
          Inf::class to { decodeFromJsonElement(Inf.serializer(), it) },
          CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
        )
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
    @Serializable
    public data class TracingConfiguration(
      @SerialName("workflow_name")
      public val workflowName: String? = null,
      @SerialName("group_id")
      public val groupId: String? = null,
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
        return json.attemptDeserialize(
          value,
          TracingConfiguration::class to { decodeFromJsonElement(TracingConfiguration.serializer(), it) },
          Auto::class to { decodeFromJsonElement(Auto.serializer(), it) },
        )
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
