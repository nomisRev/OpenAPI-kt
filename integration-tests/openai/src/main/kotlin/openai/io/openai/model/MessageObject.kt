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
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * Represents a message within a [thread](/docs/api-reference/threads).
 */
@Serializable
public data class MessageObject(
  public val id: String,
  public val `object`: Object,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("thread_id")
  public val threadId: String,
  public val status: Status,
  @SerialName("incomplete_details")
  public val incompleteDetails: IncompleteDetails?,
  @SerialName("completed_at")
  public val completedAt: Long?,
  @SerialName("incomplete_at")
  public val incompleteAt: Long?,
  public val role: Role,
  public val content: List<Content>,
  @SerialName("assistant_id")
  public val assistantId: String?,
  @SerialName("run_id")
  public val runId: String?,
  public val attachments: List<Attachments>?,
  public val metadata: Metadata,
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
            buildSerialDescriptor("io.openai.model.MessageObject.Attachments.Tools", PolymorphicKind.SEALED) {
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
    public value class CaseMessageContentImageFileObject(
      public val `value`: MessageContentImageFileObject,
    ) : Content

    @Serializable
    @JvmInline
    public value class CaseMessageContentImageUrlObject(
      public val `value`: MessageContentImageUrlObject,
    ) : Content

    @Serializable
    @JvmInline
    public value class CaseMessageContentTextObject(
      public val `value`: MessageContentTextObject,
    ) : Content

    @Serializable
    @JvmInline
    public value class CaseMessageContentRefusalObject(
      public val `value`: MessageContentRefusalObject,
    ) : Content

    public object Serializer : KSerializer<Content> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.MessageObject.Content", PolymorphicKind.SEALED) {
        element("CaseMessageContentImageFileObject", MessageContentImageFileObject.serializer().descriptor)
        element("CaseMessageContentImageUrlObject", MessageContentImageUrlObject.serializer().descriptor)
        element("CaseMessageContentTextObject", MessageContentTextObject.serializer().descriptor)
        element("CaseMessageContentRefusalObject", MessageContentRefusalObject.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Content {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseMessageContentImageFileObject::class to { CaseMessageContentImageFileObject(decodeFromJsonElement(MessageContentImageFileObject.serializer(), it)) },
          CaseMessageContentImageUrlObject::class to { CaseMessageContentImageUrlObject(decodeFromJsonElement(MessageContentImageUrlObject.serializer(), it)) },
          CaseMessageContentTextObject::class to { CaseMessageContentTextObject(decodeFromJsonElement(MessageContentTextObject.serializer(), it)) },
          CaseMessageContentRefusalObject::class to { CaseMessageContentRefusalObject(decodeFromJsonElement(MessageContentRefusalObject.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Content) {
        when(value) {
          is CaseMessageContentImageFileObject -> encoder.encodeSerializableValue(MessageContentImageFileObject.serializer(), value.value)
          is CaseMessageContentImageUrlObject -> encoder.encodeSerializableValue(MessageContentImageUrlObject.serializer(), value.value)
          is CaseMessageContentTextObject -> encoder.encodeSerializableValue(MessageContentTextObject.serializer(), value.value)
          is CaseMessageContentRefusalObject -> encoder.encodeSerializableValue(MessageContentRefusalObject.serializer(), value.value)
        }
      }
    }
  }

  /**
   * On an incomplete message, details about why the message is incomplete.
   */
  @JvmInline
  @Serializable
  public value class IncompleteDetails(
    public val reason: Reason,
  ) {
    @Serializable
    public enum class Reason(
      public val `value`: String,
    ) {
      @SerialName("content_filter")
      ContentFilter("content_filter"),
      @SerialName("max_tokens")
      MaxTokens("max_tokens"),
      @SerialName("run_cancelled")
      RunCancelled("run_cancelled"),
      @SerialName("run_expired")
      RunExpired("run_expired"),
      @SerialName("run_failed")
      RunFailed("run_failed"),
      ;
    }
  }

  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("thread.message")
    ThreadMessage("thread.message"),
    ;
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

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("in_progress")
    InProgress("in_progress"),
    @SerialName("incomplete")
    Incomplete("incomplete"),
    @SerialName("completed")
    Completed("completed"),
    ;
  }
}
