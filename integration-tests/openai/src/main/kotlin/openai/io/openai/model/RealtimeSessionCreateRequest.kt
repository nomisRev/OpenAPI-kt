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
 * A new Realtime session configuration, with an ephemeral key. Default TTL
 * for keys is one minute.
 *
 */
@Serializable
public data class RealtimeSessionCreateRequest(
  @SerialName("client_secret")
  public val clientSecret: ClientSecret,
  public val modalities: List<Modalities>? = null,
  public val instructions: String? = null,
  public val voice: VoiceIdsOrCustomVoice? = null,
  @SerialName("input_audio_format")
  public val inputAudioFormat: String? = null,
  @SerialName("output_audio_format")
  public val outputAudioFormat: String? = null,
  @SerialName("input_audio_transcription")
  public val inputAudioTranscription: InputAudioTranscription? = null,
  public val speed: Double? = null,
  public val tracing: Tracing? = null,
  @SerialName("turn_detection")
  public val turnDetection: TurnDetection? = null,
  public val tools: List<Tools>? = null,
  @SerialName("tool_choice")
  public val toolChoice: String? = null,
  public val temperature: Double? = null,
  @SerialName("max_response_output_tokens")
  public val maxResponseOutputTokens: MaxResponseOutputTokens? = null,
  public val truncation: RealtimeTruncation? = null,
  public val prompt: Prompt? = null,
) {
  /**
   * Ephemeral key returned by the API.
   */
  @Serializable
  public data class ClientSecret(
    public val `value`: String,
    @SerialName("expires_at")
    public val expiresAt: Long,
  )

  /**
   * Configuration for input audio transcription, defaults to off and can be
   * set to `null` to turn off once on. Input audio transcription is not native
   * to the model, since the model consumes audio directly. Transcription runs
   * asynchronously and should be treated as rough guidance
   * rather than the representation understood by the model.
   *
   */
  @JvmInline
  @Serializable
  public value class InputAudioTranscription(
    public val model: String? = null,
  )

  /**
   * Maximum number of output tokens for a single assistant response,
   * inclusive of tool calls. Provide an integer between 1 and 4096 to
   * limit output tokens, or `inf` for the maximum available tokens for a
   * given model. Defaults to `inf`.
   *
   */
  @Serializable(with = MaxResponseOutputTokens.Serializer::class)
  public sealed interface MaxResponseOutputTokens {
    @Serializable
    @JvmInline
    public value class CaseLong(
      public val `value`: Long,
    ) : MaxResponseOutputTokens

    @Serializable
    public enum class Inf(
      public val `value`: String,
    ) : MaxResponseOutputTokens {
      @SerialName("inf")
      Inf("inf"),
      ;
    }

    public object Serializer : KSerializer<MaxResponseOutputTokens> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.RealtimeSessionCreateRequest.MaxResponseOutputTokens", PolymorphicKind.SEALED) {
        element("CaseLong", Long.serializer().descriptor)
        element("Inf", Inf.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): MaxResponseOutputTokens {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          Inf::class to { decodeFromJsonElement(Inf.serializer(), it) },
          CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: MaxResponseOutputTokens) {
        when(value) {
          is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
          is Inf -> encoder.encodeSerializableValue(Inf.serializer(), value)
        }
      }
    }
  }

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

  @Serializable
  public data class Tools(
    public val type: Type? = null,
    public val name: String? = null,
    public val description: String? = null,
    public val parameters: JsonElement? = null,
  ) {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("function")
      Function("function"),
      ;
    }
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
          buildSerialDescriptor("io.openai.model.RealtimeSessionCreateRequest.Tracing", PolymorphicKind.SEALED) {
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
