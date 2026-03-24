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
 * Realtime session object configuration.
 */
@Serializable
public data class RealtimeSessionCreateRequestGA(
  public val type: Type,
  @SerialName("output_modalities")
  public val outputModalities: List<OutputModalities>? = null,
  public val model: Model? = null,
  public val instructions: String? = null,
  public val audio: Audio? = null,
  public val include: List<Include>? = null,
  public val tracing: Tracing? = null,
  public val tools: List<Tools>? = null,
  @SerialName("tool_choice")
  public val toolChoice: ToolChoice? = null,
  @SerialName("max_output_tokens")
  public val maxOutputTokens: MaxOutputTokens? = null,
  public val truncation: RealtimeTruncation? = null,
  public val prompt: Prompt? = null,
) {
  /**
   * Configuration for input and output audio.
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
      public val turnDetection: RealtimeTurnDetection? = null,
    ) {
      /**
       * Configuration for input audio noise reduction. This can be set to `null` to turn off.
       * Noise reduction filters audio added to the input audio buffer before it is sent to VAD and the model.
       * Filtering the audio can improve VAD and turn detection accuracy (reducing false positives) and model performance by improving perception of the input audio.
       *
       */
      @JvmInline
      @Serializable
      public value class NoiseReduction(
        public val type: NoiseReductionType? = null,
      )
    }

    @Serializable
    public data class Output(
      public val format: RealtimeAudioFormats? = null,
      public val voice: VoiceIdsOrCustomVoice? = null,
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
          buildSerialDescriptor("io.openai.model.RealtimeSessionCreateRequestGA.MaxOutputTokens", PolymorphicKind.SEALED) {
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
      @SerialName("gpt-realtime")
      GptRealtime("gpt-realtime"),
      @SerialName("gpt-realtime-1.5")
      GptRealtime15("gpt-realtime-1.5"),
      @SerialName("gpt-realtime-2025-08-28")
      GptRealtime20250828("gpt-realtime-2025-08-28"),
      @SerialName("gpt-4o-realtime-preview")
      Gpt4oRealtimePreview("gpt-4o-realtime-preview"),
      @SerialName("gpt-4o-realtime-preview-2024-10-01")
      Gpt4oRealtimePreview20241001("gpt-4o-realtime-preview-2024-10-01"),
      @SerialName("gpt-4o-realtime-preview-2024-12-17")
      Gpt4oRealtimePreview20241217("gpt-4o-realtime-preview-2024-12-17"),
      @SerialName("gpt-4o-realtime-preview-2025-06-03")
      Gpt4oRealtimePreview20250603("gpt-4o-realtime-preview-2025-06-03"),
      @SerialName("gpt-4o-mini-realtime-preview")
      Gpt4oMiniRealtimePreview("gpt-4o-mini-realtime-preview"),
      @SerialName("gpt-4o-mini-realtime-preview-2024-12-17")
      Gpt4oMiniRealtimePreview20241217("gpt-4o-mini-realtime-preview-2024-12-17"),
      @SerialName("gpt-realtime-mini")
      GptRealtimeMini("gpt-realtime-mini"),
      @SerialName("gpt-realtime-mini-2025-10-06")
      GptRealtimeMini20251006("gpt-realtime-mini-2025-10-06"),
      @SerialName("gpt-realtime-mini-2025-12-15")
      GptRealtimeMini20251215("gpt-realtime-mini-2025-12-15"),
      @SerialName("gpt-audio-1.5")
      GptAudio15("gpt-audio-1.5"),
      @SerialName("gpt-audio-mini")
      GptAudioMini("gpt-audio-mini"),
      @SerialName("gpt-audio-mini-2025-10-06")
      GptAudioMini20251006("gpt-audio-mini-2025-10-06"),
      @SerialName("gpt-audio-mini-2025-12-15")
      GptAudioMini20251215("gpt-audio-mini-2025-12-15"),
      ;
    }

    public object Serializer : KSerializer<Model> {
      override val descriptor: SerialDescriptor = String.serializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Model) {
        when(value) {
          CaseEnum.GptRealtime -> encoder.encodeString("gpt-realtime")
          CaseEnum.GptRealtime15 -> encoder.encodeString("gpt-realtime-1.5")
          CaseEnum.GptRealtime20250828 -> encoder.encodeString("gpt-realtime-2025-08-28")
          CaseEnum.Gpt4oRealtimePreview -> encoder.encodeString("gpt-4o-realtime-preview")
          CaseEnum.Gpt4oRealtimePreview20241001 -> encoder.encodeString("gpt-4o-realtime-preview-2024-10-01")
          CaseEnum.Gpt4oRealtimePreview20241217 -> encoder.encodeString("gpt-4o-realtime-preview-2024-12-17")
          CaseEnum.Gpt4oRealtimePreview20250603 -> encoder.encodeString("gpt-4o-realtime-preview-2025-06-03")
          CaseEnum.Gpt4oMiniRealtimePreview -> encoder.encodeString("gpt-4o-mini-realtime-preview")
          CaseEnum.Gpt4oMiniRealtimePreview20241217 -> encoder.encodeString("gpt-4o-mini-realtime-preview-2024-12-17")
          CaseEnum.GptRealtimeMini -> encoder.encodeString("gpt-realtime-mini")
          CaseEnum.GptRealtimeMini20251006 -> encoder.encodeString("gpt-realtime-mini-2025-10-06")
          CaseEnum.GptRealtimeMini20251215 -> encoder.encodeString("gpt-realtime-mini-2025-12-15")
          CaseEnum.GptAudio15 -> encoder.encodeString("gpt-audio-1.5")
          CaseEnum.GptAudioMini -> encoder.encodeString("gpt-audio-mini")
          CaseEnum.GptAudioMini20251006 -> encoder.encodeString("gpt-audio-mini-2025-10-06")
          CaseEnum.GptAudioMini20251215 -> encoder.encodeString("gpt-audio-mini-2025-12-15")
          is CaseString -> encoder.encodeString(value.value)
        }
      }

      override fun deserialize(decoder: Decoder): Model = when(val value = decoder.decodeString()) {
        "gpt-realtime" -> CaseEnum.GptRealtime
        "gpt-realtime-1.5" -> CaseEnum.GptRealtime15
        "gpt-realtime-2025-08-28" -> CaseEnum.GptRealtime20250828
        "gpt-4o-realtime-preview" -> CaseEnum.Gpt4oRealtimePreview
        "gpt-4o-realtime-preview-2024-10-01" -> CaseEnum.Gpt4oRealtimePreview20241001
        "gpt-4o-realtime-preview-2024-12-17" -> CaseEnum.Gpt4oRealtimePreview20241217
        "gpt-4o-realtime-preview-2025-06-03" -> CaseEnum.Gpt4oRealtimePreview20250603
        "gpt-4o-mini-realtime-preview" -> CaseEnum.Gpt4oMiniRealtimePreview
        "gpt-4o-mini-realtime-preview-2024-12-17" -> CaseEnum.Gpt4oMiniRealtimePreview20241217
        "gpt-realtime-mini" -> CaseEnum.GptRealtimeMini
        "gpt-realtime-mini-2025-10-06" -> CaseEnum.GptRealtimeMini20251006
        "gpt-realtime-mini-2025-12-15" -> CaseEnum.GptRealtimeMini20251215
        "gpt-audio-1.5" -> CaseEnum.GptAudio15
        "gpt-audio-mini" -> CaseEnum.GptAudioMini
        "gpt-audio-mini-2025-10-06" -> CaseEnum.GptAudioMini20251006
        "gpt-audio-mini-2025-12-15" -> CaseEnum.GptAudioMini20251215
        else -> CaseString(value)
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
   * How the model chooses tools. Provide one of the string modes or force a specific
   * function/MCP tool.
   *
   */
  @Serializable(with = ToolChoice.Serializer::class)
  public sealed interface ToolChoice {
    @Serializable
    @JvmInline
    public value class CaseToolChoiceOptions(
      public val `value`: ToolChoiceOptions,
    ) : ToolChoice

    @Serializable
    @JvmInline
    public value class CaseToolChoiceFunction(
      public val `value`: ToolChoiceFunction,
    ) : ToolChoice

    @Serializable
    @JvmInline
    public value class CaseToolChoiceMCP(
      public val `value`: ToolChoiceMCP,
    ) : ToolChoice

    public object Serializer : KSerializer<ToolChoice> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.RealtimeSessionCreateRequestGA.ToolChoice", PolymorphicKind.SEALED) {
        element("CaseToolChoiceOptions", ToolChoiceOptions.serializer().descriptor)
        element("CaseToolChoiceFunction", ToolChoiceFunction.serializer().descriptor)
        element("CaseToolChoiceMCP", ToolChoiceMCP.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): ToolChoice {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseToolChoiceOptions::class to { CaseToolChoiceOptions(decodeFromJsonElement(ToolChoiceOptions.serializer(), it)) },
          CaseToolChoiceFunction::class to { CaseToolChoiceFunction(decodeFromJsonElement(ToolChoiceFunction.serializer(), it)) },
          CaseToolChoiceMCP::class to { CaseToolChoiceMCP(decodeFromJsonElement(ToolChoiceMCP.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: ToolChoice) {
        when(value) {
          is CaseToolChoiceOptions -> encoder.encodeSerializableValue(ToolChoiceOptions.serializer(), value.value)
          is CaseToolChoiceFunction -> encoder.encodeSerializableValue(ToolChoiceFunction.serializer(), value.value)
          is CaseToolChoiceMCP -> encoder.encodeSerializableValue(ToolChoiceMCP.serializer(), value.value)
        }
      }
    }
  }

  @Serializable(with = Tools.Serializer::class)
  public sealed interface Tools {
    @Serializable
    @JvmInline
    public value class CaseRealtimeFunctionTool(
      public val `value`: RealtimeFunctionTool,
    ) : Tools

    @Serializable
    @JvmInline
    public value class CaseMCPTool(
      public val `value`: MCPTool,
    ) : Tools

    public object Serializer : KSerializer<Tools> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.RealtimeSessionCreateRequestGA.Tools", PolymorphicKind.SEALED) {
        element("CaseRealtimeFunctionTool", RealtimeFunctionTool.serializer().descriptor)
        element("CaseMCPTool", MCPTool.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Tools {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseRealtimeFunctionTool::class to { CaseRealtimeFunctionTool(decodeFromJsonElement(RealtimeFunctionTool.serializer(), it)) },
          CaseMCPTool::class to { CaseMCPTool(decodeFromJsonElement(MCPTool.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Tools) {
        when(value) {
          is CaseRealtimeFunctionTool -> encoder.encodeSerializableValue(RealtimeFunctionTool.serializer(), value.value)
          is CaseMCPTool -> encoder.encodeSerializableValue(MCPTool.serializer(), value.value)
        }
      }
    }
  }

  /**
   * Realtime API can write session traces to the [Traces Dashboard](/logs?api=traces). Set to null to disable tracing. Once
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
          buildSerialDescriptor("io.openai.model.RealtimeSessionCreateRequestGA.Tracing", PolymorphicKind.SEALED) {
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

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("realtime")
    Realtime("realtime"),
    ;
  }
}
