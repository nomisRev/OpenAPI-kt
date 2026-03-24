package io.openai.model

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
public data class CreateMessageRequest(
  public val role: Role,
  public val content: Content,
  public val attachments: List<Attachments>? = null,
  public val metadata: Metadata? = null,
) {
  @Serializable
  public data class Attachments(
    @SerialName("file_id")
    public val fileId: String? = null,
    public val tools: List<Tools>? = null,
  ) {
    @Serializable(with = Tools.Serializer::class)
    public sealed interface Tools {
      @Serializable
      @JvmInline
      public value class CaseAssistantToolsCode(
        public val `value`: AssistantToolsCode,
      ) : Tools

      @Serializable
      @JvmInline
      public value class CaseAssistantToolsFileSearchTypeOnly(
        public val `value`: AssistantToolsFileSearchTypeOnly,
      ) : Tools

      public object Serializer : KSerializer<Tools> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.openai.model.CreateMessageRequest.Attachments.Tools", PolymorphicKind.SEALED) {
          element("CaseAssistantToolsCode", AssistantToolsCode.serializer().descriptor)
          element("CaseAssistantToolsFileSearchTypeOnly", AssistantToolsFileSearchTypeOnly.serializer().descriptor)
        }

        override fun deserialize(decoder: Decoder): Tools {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return json.attemptDeserialize(
            value,
            CaseAssistantToolsCode::class to { CaseAssistantToolsCode(decodeFromJsonElement(AssistantToolsCode.serializer(), it)) },
            CaseAssistantToolsFileSearchTypeOnly::class to { CaseAssistantToolsFileSearchTypeOnly(decodeFromJsonElement(AssistantToolsFileSearchTypeOnly.serializer(), it)) },
          )
        }

        override fun serialize(encoder: Encoder, `value`: Tools) {
          when(value) {
            is CaseAssistantToolsCode -> encoder.encodeSerializableValue(AssistantToolsCode.serializer(), value.value)
            is CaseAssistantToolsFileSearchTypeOnly -> encoder.encodeSerializableValue(AssistantToolsFileSearchTypeOnly.serializer(), value.value)
          }
        }
      }
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

    @Serializable(with = Two.Serializer::class)
    public sealed interface Two {
      @Serializable
      @JvmInline
      public value class CaseMessageContentImageFileObject(
        public val `value`: MessageContentImageFileObject,
      ) : Two

      @Serializable
      @JvmInline
      public value class CaseMessageContentImageUrlObject(
        public val `value`: MessageContentImageUrlObject,
      ) : Two

      @Serializable
      @JvmInline
      public value class CaseMessageRequestContentTextObject(
        public val `value`: MessageRequestContentTextObject,
      ) : Two

      public object Serializer : KSerializer<Two> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.openai.model.CreateMessageRequest.Content.Two", PolymorphicKind.SEALED) {
          element("CaseMessageContentImageFileObject", MessageContentImageFileObject.serializer().descriptor)
          element("CaseMessageContentImageUrlObject", MessageContentImageUrlObject.serializer().descriptor)
          element("CaseMessageRequestContentTextObject", MessageRequestContentTextObject.serializer().descriptor)
        }

        override fun deserialize(decoder: Decoder): Two {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return json.attemptDeserialize(
            value,
            CaseMessageContentImageFileObject::class to { CaseMessageContentImageFileObject(decodeFromJsonElement(MessageContentImageFileObject.serializer(), it)) },
            CaseMessageContentImageUrlObject::class to { CaseMessageContentImageUrlObject(decodeFromJsonElement(MessageContentImageUrlObject.serializer(), it)) },
            CaseMessageRequestContentTextObject::class to { CaseMessageRequestContentTextObject(decodeFromJsonElement(MessageRequestContentTextObject.serializer(), it)) },
          )
        }

        override fun serialize(encoder: Encoder, `value`: Two) {
          when(value) {
            is CaseMessageContentImageFileObject -> encoder.encodeSerializableValue(MessageContentImageFileObject.serializer(), value.value)
            is CaseMessageContentImageUrlObject -> encoder.encodeSerializableValue(MessageContentImageUrlObject.serializer(), value.value)
            is CaseMessageRequestContentTextObject -> encoder.encodeSerializableValue(MessageRequestContentTextObject.serializer(), value.value)
          }
        }
      }
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
        return json.attemptDeserialize(
          value,
          CaseTwoList::class to { CaseTwoList(decodeFromJsonElement(ListSerializer(Two.serializer()), it)) },
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
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
