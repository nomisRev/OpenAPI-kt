package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateCompletionRequest(
  public val model: Model,
  public val prompt: Prompt?,
  @SerialName("best_of")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val bestOf: Optional<Long> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val echo: Optional<Boolean> = Optional.Absent,
  @SerialName("frequency_penalty")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val frequencyPenalty: Optional<Double> = Optional.Absent,
  @SerialName("logit_bias")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val logitBias: Optional<Map<String, Long>> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val logprobs: Optional<Long> = Optional.Absent,
  @SerialName("max_tokens")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxTokens: Optional<Long> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val n: Optional<Long> = Optional.Absent,
  @SerialName("presence_penalty")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val presencePenalty: Optional<Double> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val seed: Optional<Long> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val stop: Optional<StopConfiguration> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val stream: Optional<Boolean> = Optional.Absent,
  @SerialName("stream_options")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val streamOptions: Optional<ChatCompletionStreamOptions> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val suffix: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val temperature: Optional<Double> = Optional.Absent,
  @SerialName("top_p")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val topP: Optional<Double> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val user: String? = null,
) {
  @Serializable(with = Model.Serializer::class)
  public sealed interface Model {
    public val `value`: String

    @Serializable
    @JvmInline
    public value class CaseString(
      override val `value`: String,
    ) : Model

    @Serializable
    public enum class Gpt35TurboInstructOrDavinci002OrBabbage002(
      override val `value`: String,
    ) : Model {
      @SerialName("gpt-3.5-turbo-instruct")
      Gpt35TurboInstruct("gpt-3.5-turbo-instruct"),
      @SerialName("davinci-002")
      Davinci002("davinci-002"),
      @SerialName("babbage-002")
      Babbage002("babbage-002"),
      ;
    }

    public object Serializer : KSerializer<Model> {
      override val descriptor: SerialDescriptor = String.serializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Model) {
        when(value) {
          Gpt35TurboInstructOrDavinci002OrBabbage002.Gpt35TurboInstruct -> encoder.encodeString("gpt-3.5-turbo-instruct")
          Gpt35TurboInstructOrDavinci002OrBabbage002.Davinci002 -> encoder.encodeString("davinci-002")
          Gpt35TurboInstructOrDavinci002OrBabbage002.Babbage002 -> encoder.encodeString("babbage-002")
          is CaseString -> encoder.encodeString(value.value)
        }
      }

      override fun deserialize(decoder: Decoder): Model = when(val value = decoder.decodeString()) {
        "gpt-3.5-turbo-instruct" -> Gpt35TurboInstructOrDavinci002OrBabbage002.Gpt35TurboInstruct
        "davinci-002" -> Gpt35TurboInstructOrDavinci002OrBabbage002.Davinci002
        "babbage-002" -> Gpt35TurboInstructOrDavinci002OrBabbage002.Babbage002
        else -> CaseString(value)
      }
    }
  }

  /**
   * The prompt(s) to generate completions for, encoded as a string, array of strings, array of tokens, or array of token arrays.
   *
   * Note that <|endoftext|> is the document separator that the model sees during training, so if a prompt is not specified the model will generate as if from the beginning of a new document.
   *
   */
  @Serializable(with = Prompt.Serializer::class)
  public sealed interface Prompt {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Prompt

    @Serializable
    @JvmInline
    public value class CaseStrings(
      public val `value`: List<String>,
    ) : Prompt

    @Serializable
    @JvmInline
    public value class CaseLongs(
      public val `value`: List<Long>,
    ) : Prompt

    @Serializable
    @JvmInline
    public value class CaseLongsList(
      public val `value`: List<List<Long>>,
    ) : Prompt

    public object Serializer : KSerializer<Prompt> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.CreateCompletionRequest.Prompt", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseStrings", ListSerializer(String.serializer()).descriptor)
        element("CaseLongs", ListSerializer(Long.serializer()).descriptor)
        element("CaseLongsList", ListSerializer(ListSerializer(Long.serializer())).descriptor)
      }

      override fun deserialize(decoder: Decoder): Prompt {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return when {
          value is JsonPrimitive && value.isString -> json.attemptDeserialize(
            value,
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
          value is JsonArray -> json.attemptDeserialize(
            value,
            CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
            CaseLongs::class to { CaseLongs(decodeFromJsonElement(ListSerializer(Long.serializer()), it)) },
            CaseLongsList::class to { CaseLongsList(decodeFromJsonElement(ListSerializer(ListSerializer(Long.serializer())), it)) },
          )
          else -> throw UnionSerializationException(value, emptyMap())
        }
      }

      override fun serialize(encoder: Encoder, `value`: Prompt) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
          is CaseLongs -> encoder.encodeSerializableValue(ListSerializer(Long.serializer()), value.value)
          is CaseLongsList -> encoder.encodeSerializableValue(ListSerializer(ListSerializer(Long.serializer())), value.value)
        }
      }
    }
  }
}
