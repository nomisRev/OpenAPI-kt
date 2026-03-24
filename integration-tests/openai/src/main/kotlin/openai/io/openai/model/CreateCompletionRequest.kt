package io.openai.model

import kotlin.Boolean
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
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable
public data class CreateCompletionRequest(
  public val model: Model,
  public val prompt: Prompt?,
  @SerialName("best_of")
  public val bestOf: Long? = null,
  public val echo: Boolean? = null,
  @SerialName("frequency_penalty")
  public val frequencyPenalty: Double? = null,
  @SerialName("logit_bias")
  public val logitBias: List<Long>? = null,
  public val logprobs: Long? = null,
  @SerialName("max_tokens")
  public val maxTokens: Long? = null,
  public val n: Long? = null,
  @SerialName("presence_penalty")
  public val presencePenalty: Double? = null,
  public val seed: Long? = null,
  public val stop: StopConfiguration? = null,
  public val stream: Boolean? = null,
  @SerialName("stream_options")
  public val streamOptions: ChatCompletionStreamOptions? = null,
  public val suffix: String? = null,
  public val temperature: Double? = null,
  @SerialName("top_p")
  public val topP: Double? = null,
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
        return json.attemptDeserialize(
          value,
          CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
          CaseLongs::class to { CaseLongs(decodeFromJsonElement(ListSerializer(Long.serializer()), it)) },
          CaseLongsList::class to { CaseLongsList(decodeFromJsonElement(ListSerializer(ListSerializer(Long.serializer())), it)) },
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
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
