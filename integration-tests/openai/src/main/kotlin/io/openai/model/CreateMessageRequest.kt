package io.openai.model

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
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

@Serializable
public data class CreateMessageRequest(
  public val role: Role,
  public val content: Content,
  public val attachments: List<Attachments>? = null,
  public val metadata: Metadata? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Attachments(
    @SerialName("file_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val fileId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val tools: List<Tools>? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("type")
    @Serializable
    public sealed interface Tools {
      @Serializable
      @SerialName("code_interpreter")
      public data object CodeInterpreter : Tools

      @Serializable
      @SerialName("file_search")
      public data object FileSearch : Tools
    }
  }

  @Serializable(with = Content.Serializer::class)
  public sealed interface Content {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Content

    @Serializable
    @JvmInline
    public value class CaseTwoList(
      public val `value`: List<Two>,
    ) : Content

    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("type")
    @Serializable
    public sealed interface Two {
      /**
       * References an image [File](/docs/api-reference/files) in the content of a message.
       */
      @SerialName("image_file")
      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class ImageFile(
        @SerialName("file_id")
        public val fileId: String,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val detail: Detail? = null,
      ) : Two {
        @Serializable
        public enum class Detail(
          public val `value`: String,
        ) {
          @SerialName("auto")
          Auto("auto"),
          @SerialName("low")
          Low("low"),
          @SerialName("high")
          High("high"),
          ;
        }
      }

      /**
       * References an image URL in the content of a message.
       */
      @SerialName("image_url")
      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class ImageUrl(
        public val url: String,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val detail: Detail? = null,
      ) : Two {
        @Serializable
        public enum class Detail(
          public val `value`: String,
        ) {
          @SerialName("auto")
          Auto("auto"),
          @SerialName("low")
          Low("low"),
          @SerialName("high")
          High("high"),
          ;
        }
      }

      /**
       * The text content that is part of a message.
       */
      @SerialName("text")
      @Serializable
      public data class Text(
        public val text: String,
      ) : Two
    }

    public object Serializer : KSerializer<Content> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.CreateMessageRequest.Content", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseTwoList", ListSerializer(Two.serializer()).descriptor)
      }

      override fun deserialize(decoder: Decoder): Content {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return when {
          value is JsonPrimitive && value.isString -> json.attemptDeserialize(
            value,
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
          value is JsonArray -> json.attemptDeserialize(
            value,
            CaseTwoList::class to { CaseTwoList(decodeFromJsonElement(ListSerializer(Two.serializer()), it)) },
          )
          else -> throw UnionSerializationException(value, emptyMap())
        }
      }

      override fun serialize(encoder: Encoder, `value`: Content) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseTwoList -> encoder.encodeSerializableValue(ListSerializer(Two.serializer()), value.value)
        }
      }
    }
  }

  @Serializable
  public enum class Role(
    public val `value`: String,
  ) {
    @SerialName("user")
    User("user"),
    @SerialName("assistant")
    Assistant("assistant"),
    ;
  }
}
