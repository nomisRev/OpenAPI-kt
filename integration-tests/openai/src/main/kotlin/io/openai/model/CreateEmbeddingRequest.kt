package io.openai.model

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
public data class CreateEmbeddingRequest(
  public val input: Input,
  public val model: Model,
  @SerialName("encoding_format")
  public val encodingFormat: EncodingFormat? = null,
  public val dimensions: Long? = null,
  public val user: String? = null,
) {
  @Serializable
  public enum class EncodingFormat(
    public val `value`: String,
  ) {
    @SerialName("float")
    Float("float"),
    @SerialName("base64")
    Base64("base64"),
    ;
  }

  /**
   * Input text to embed, encoded as a string or array of tokens. To embed multiple inputs in a single request, pass an array of strings or array of token arrays. The input must not exceed the max input tokens for the model (8192 tokens for all embedding models), cannot be an empty string, and any array must be 2048 dimensions or less. [Example Python code](https://cookbook.openai.com/examples/how_to_count_tokens_with_tiktoken) for counting tokens. In addition to the per-input token limit, all embedding  models enforce a maximum of 300,000 tokens summed across all inputs in a  single request.
   *
   */
  @Serializable(with = Input.Serializer::class)
  public sealed interface Input {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Input

    @Serializable
    @JvmInline
    public value class CaseStrings(
      public val `value`: List<String>,
    ) : Input

    @Serializable
    @JvmInline
    public value class CaseLongs(
      public val `value`: List<Long>,
    ) : Input

    @Serializable
    @JvmInline
    public value class CaseLongsList(
      public val `value`: List<List<Long>>,
    ) : Input

    public object Serializer : KSerializer<Input> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.CreateEmbeddingRequest.Input", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseStrings", ListSerializer(String.serializer()).descriptor)
        element("CaseLongs", ListSerializer(Long.serializer()).descriptor)
        element("CaseLongsList", ListSerializer(ListSerializer(Long.serializer())).descriptor)
      }

      override fun deserialize(decoder: Decoder): Input {
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

      override fun serialize(encoder: Encoder, `value`: Input) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
          is CaseLongs -> encoder.encodeSerializableValue(ListSerializer(Long.serializer()), value.value)
          is CaseLongsList -> encoder.encodeSerializableValue(ListSerializer(ListSerializer(Long.serializer())), value.value)
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
    public enum class TextEmbeddingAda002OrTextEmbedding3SmallOrTextEmbedding3Large(
      override val `value`: String,
    ) : Model {
      @SerialName("text-embedding-ada-002")
      TextEmbeddingAda002("text-embedding-ada-002"),
      @SerialName("text-embedding-3-small")
      TextEmbedding3Small("text-embedding-3-small"),
      @SerialName("text-embedding-3-large")
      TextEmbedding3Large("text-embedding-3-large"),
      ;
    }

    public object Serializer : KSerializer<Model> {
      override val descriptor: SerialDescriptor = String.serializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Model) {
        when(value) {
          TextEmbeddingAda002OrTextEmbedding3SmallOrTextEmbedding3Large.TextEmbeddingAda002 -> encoder.encodeString("text-embedding-ada-002")
          TextEmbeddingAda002OrTextEmbedding3SmallOrTextEmbedding3Large.TextEmbedding3Small -> encoder.encodeString("text-embedding-3-small")
          TextEmbeddingAda002OrTextEmbedding3SmallOrTextEmbedding3Large.TextEmbedding3Large -> encoder.encodeString("text-embedding-3-large")
          is CaseString -> encoder.encodeString(value.value)
        }
      }

      override fun deserialize(decoder: Decoder): Model = when(val value = decoder.decodeString()) {
        "text-embedding-ada-002" -> TextEmbeddingAda002OrTextEmbedding3SmallOrTextEmbedding3Large.TextEmbeddingAda002
        "text-embedding-3-small" -> TextEmbeddingAda002OrTextEmbedding3SmallOrTextEmbedding3Large.TextEmbedding3Small
        "text-embedding-3-large" -> TextEmbeddingAda002OrTextEmbedding3SmallOrTextEmbedding3Large.TextEmbedding3Large
        else -> CaseString(value)
      }
    }
  }
}
