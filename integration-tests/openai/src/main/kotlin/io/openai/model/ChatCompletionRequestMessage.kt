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
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("role")
@Serializable
public sealed interface ChatCompletionRequestMessage {
  /**
   * Developer-provided instructions that the model should follow, regardless of
   * messages sent by the user. With o1 models and newer, `developer` messages
   * replace the previous `system` messages.
   *
   */
  @SerialName("developer")
  @Serializable
  public data class Developer(
    public val content: Content,
    public val name: String? = null,
  ) : ChatCompletionRequestMessage {
    /**
     * The contents of the developer message.
     */
    @Serializable(with = Content.Serializer::class)
    public sealed interface Content {
      @Serializable
      @JvmInline
      public value class CaseString(
        public val `value`: String,
      ) : Content

      @Serializable
      @JvmInline
      public value class CaseChatCompletionRequestMessageContentPartTextList(
        public val `value`: List<ChatCompletionRequestMessageContentPartText>,
      ) : Content

      public object Serializer : KSerializer<Content> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.openai.model.ChatCompletionRequestMessage.Developer.Content", PolymorphicKind.SEALED) {
          element("CaseString", String.serializer().descriptor)
          element("CaseChatCompletionRequestMessageContentPartTextList", ListSerializer(ChatCompletionRequestMessageContentPartText.serializer()).descriptor)
        }

        override fun deserialize(decoder: Decoder): Content {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return json.attemptDeserialize(
            value,
            CaseChatCompletionRequestMessageContentPartTextList::class to { CaseChatCompletionRequestMessageContentPartTextList(decodeFromJsonElement(ListSerializer(ChatCompletionRequestMessageContentPartText.serializer()), it)) },
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
        }

        override fun serialize(encoder: Encoder, `value`: Content) {
          when(value) {
            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            is CaseChatCompletionRequestMessageContentPartTextList -> encoder.encodeSerializableValue(ListSerializer(ChatCompletionRequestMessageContentPartText.serializer()), value.value)
          }
        }
      }
    }
  }

  /**
   * Developer-provided instructions that the model should follow, regardless of
   * messages sent by the user. With o1 models and newer, use `developer` messages
   * for this purpose instead.
   *
   */
  @SerialName("system")
  @Serializable
  public data class System(
    public val content: Content,
    public val name: String? = null,
  ) : ChatCompletionRequestMessage {
    /**
     * The contents of the system message.
     */
    @Serializable(with = Content.Serializer::class)
    public sealed interface Content {
      @Serializable
      @JvmInline
      public value class CaseString(
        public val `value`: String,
      ) : Content

      @Serializable
      @JvmInline
      public value class CaseChatCompletionRequestMessageContentPartTextList(
        public val `value`: List<ChatCompletionRequestMessageContentPartText>,
      ) : Content

      public object Serializer : KSerializer<Content> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.openai.model.ChatCompletionRequestMessage.System.Content", PolymorphicKind.SEALED) {
          element("CaseString", String.serializer().descriptor)
          element("CaseChatCompletionRequestMessageContentPartTextList", ListSerializer(ChatCompletionRequestMessageContentPartText.serializer()).descriptor)
        }

        override fun deserialize(decoder: Decoder): Content {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return json.attemptDeserialize(
            value,
            CaseChatCompletionRequestMessageContentPartTextList::class to { CaseChatCompletionRequestMessageContentPartTextList(decodeFromJsonElement(ListSerializer(ChatCompletionRequestMessageContentPartText.serializer()), it)) },
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
        }

        override fun serialize(encoder: Encoder, `value`: Content) {
          when(value) {
            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            is CaseChatCompletionRequestMessageContentPartTextList -> encoder.encodeSerializableValue(ListSerializer(ChatCompletionRequestMessageContentPartText.serializer()), value.value)
          }
        }
      }
    }
  }

  /**
   * Messages sent by an end user, containing prompts or additional context
   * information.
   *
   */
  @SerialName("user")
  @Serializable
  public data class User(
    public val content: Content,
    public val name: String? = null,
  ) : ChatCompletionRequestMessage {
    /**
     * The contents of the user message.
     *
     */
    @Serializable(with = Content.Serializer::class)
    public sealed interface Content {
      @Serializable
      @JvmInline
      public value class CaseString(
        public val `value`: String,
      ) : Content

      @Serializable
      @JvmInline
      public value class CaseChatCompletionRequestUserMessageContentPartList(
        public val `value`: List<ChatCompletionRequestUserMessageContentPart>,
      ) : Content

      public object Serializer : KSerializer<Content> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.openai.model.ChatCompletionRequestMessage.User.Content", PolymorphicKind.SEALED) {
          element("CaseString", String.serializer().descriptor)
          element("CaseChatCompletionRequestUserMessageContentPartList", ListSerializer(ChatCompletionRequestUserMessageContentPart.serializer()).descriptor)
        }

        override fun deserialize(decoder: Decoder): Content {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return json.attemptDeserialize(
            value,
            CaseChatCompletionRequestUserMessageContentPartList::class to { CaseChatCompletionRequestUserMessageContentPartList(decodeFromJsonElement(ListSerializer(ChatCompletionRequestUserMessageContentPart.serializer()), it)) },
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
        }

        override fun serialize(encoder: Encoder, `value`: Content) {
          when(value) {
            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            is CaseChatCompletionRequestUserMessageContentPartList -> encoder.encodeSerializableValue(ListSerializer(ChatCompletionRequestUserMessageContentPart.serializer()), value.value)
          }
        }
      }
    }
  }

  /**
   * Messages sent by the model in response to user messages.
   *
   */
  @SerialName("assistant")
  @Serializable
  public data class Assistant(
    public val content: Content? = null,
    public val refusal: String? = null,
    public val name: String? = null,
    public val audio: Audio? = null,
    @SerialName("tool_calls")
    public val toolCalls: ChatCompletionMessageToolCalls? = null,
    @SerialName("function_call")
    public val functionCall: FunctionCall? = null,
  ) : ChatCompletionRequestMessage {
    /**
     * Data about a previous audio response from the model.
     * [Learn more](/docs/guides/audio).
     *
     */
    @JvmInline
    @Serializable
    public value class Audio(
      public val id: String,
    )

    /**
     * The contents of the assistant message. Required unless `tool_calls` or `function_call` is specified.
     *
     */
    @Serializable(with = Content.Serializer::class)
    public sealed interface Content {
      @Serializable
      @JvmInline
      public value class CaseString(
        public val `value`: String,
      ) : Content

      @Serializable
      @JvmInline
      public value class CaseChatCompletionRequestAssistantMessageContentPartList(
        public val `value`: List<ChatCompletionRequestAssistantMessageContentPart>,
      ) : Content

      public object Serializer : KSerializer<Content> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.openai.model.ChatCompletionRequestMessage.Assistant.Content", PolymorphicKind.SEALED) {
          element("CaseString", String.serializer().descriptor)
          element("CaseChatCompletionRequestAssistantMessageContentPartList", ListSerializer(ChatCompletionRequestAssistantMessageContentPart.serializer()).descriptor)
        }

        override fun deserialize(decoder: Decoder): Content {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return json.attemptDeserialize(
            value,
            CaseChatCompletionRequestAssistantMessageContentPartList::class to { CaseChatCompletionRequestAssistantMessageContentPartList(decodeFromJsonElement(ListSerializer(ChatCompletionRequestAssistantMessageContentPart.serializer()), it)) },
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
        }

        override fun serialize(encoder: Encoder, `value`: Content) {
          when(value) {
            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            is CaseChatCompletionRequestAssistantMessageContentPartList -> encoder.encodeSerializableValue(ListSerializer(ChatCompletionRequestAssistantMessageContentPart.serializer()), value.value)
          }
        }
      }
    }

    /**
     * Deprecated and replaced by `tool_calls`. The name and arguments of a function that should be called, as generated by the model.
     */
    @Serializable
    public data class FunctionCall(
      public val arguments: String,
      public val name: String,
    )
  }

  @SerialName("tool")
  @Serializable
  public data class Tool(
    public val content: Content,
    @SerialName("tool_call_id")
    public val toolCallId: String,
  ) : ChatCompletionRequestMessage {
    /**
     * The contents of the tool message.
     */
    @Serializable(with = Content.Serializer::class)
    public sealed interface Content {
      @Serializable
      @JvmInline
      public value class CaseString(
        public val `value`: String,
      ) : Content

      @Serializable
      @JvmInline
      public value class CaseChatCompletionRequestMessageContentPartTextList(
        public val `value`: List<ChatCompletionRequestMessageContentPartText>,
      ) : Content

      public object Serializer : KSerializer<Content> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.openai.model.ChatCompletionRequestMessage.Tool.Content", PolymorphicKind.SEALED) {
          element("CaseString", String.serializer().descriptor)
          element("CaseChatCompletionRequestMessageContentPartTextList", ListSerializer(ChatCompletionRequestMessageContentPartText.serializer()).descriptor)
        }

        override fun deserialize(decoder: Decoder): Content {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return json.attemptDeserialize(
            value,
            CaseChatCompletionRequestMessageContentPartTextList::class to { CaseChatCompletionRequestMessageContentPartTextList(decodeFromJsonElement(ListSerializer(ChatCompletionRequestMessageContentPartText.serializer()), it)) },
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
        }

        override fun serialize(encoder: Encoder, `value`: Content) {
          when(value) {
            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            is CaseChatCompletionRequestMessageContentPartTextList -> encoder.encodeSerializableValue(ListSerializer(ChatCompletionRequestMessageContentPartText.serializer()), value.value)
          }
        }
      }
    }
  }

  @SerialName("function")
  @Serializable
  public data class Function(
    public val content: String?,
    public val name: String,
  ) : ChatCompletionRequestMessage
}
