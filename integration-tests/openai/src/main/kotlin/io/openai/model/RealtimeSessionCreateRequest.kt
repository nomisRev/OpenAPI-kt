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
 * A new Realtime session configuration, with an ephemeral key. Default TTL
 * for keys is one minute.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RealtimeSessionCreateRequest(
  @SerialName("client_secret")
  public val clientSecret: ClientSecret,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val modalities: List<Modalities>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val instructions: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val voice: VoiceIdsOrCustomVoice? = null,
  @SerialName("input_audio_format")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val inputAudioFormat: String? = null,
  @SerialName("output_audio_format")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val outputAudioFormat: String? = null,
  @SerialName("input_audio_transcription")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val inputAudioTranscription: InputAudioTranscription? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val speed: Double? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val tracing: Tracing? = null,
  @SerialName("turn_detection")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val turnDetection: TurnDetection? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val tools: List<Tools>? = null,
  @SerialName("tool_choice")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val toolChoice: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val temperature: Double? = null,
  @SerialName("max_response_output_tokens")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxResponseOutputTokens: MaxResponseOutputTokens? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val truncation: RealtimeTruncation? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val prompt: Optional<Prompt> = Optional.Absent,
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class InputAudioTranscription(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Tools(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val type: Type? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val description: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
          buildSerialDescriptor("io.openai.model.RealtimeSessionCreateRequest.Tracing", PolymorphicKind.SEALED) {
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
