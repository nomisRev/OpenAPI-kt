package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * User-authored messages within a thread.
 */
@Serializable
public data class UserMessageItem(
  public val id: String,
  @Required
  public val `object`: Object = Object.ChatkitThreadItem,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("thread_id")
  public val threadId: String,
  @Required
  public val type: Type = Type.ChatkitUserMessage,
  public val content: List<Content>,
  public val attachments: List<Attachment>,
  @SerialName("inference_options")
  public val inferenceOptions: InferenceOptions?,
) {
  /**
   * Content blocks that comprise a user message.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Content {
    @Serializable
    @JvmInline
    @SerialName("UserMessageInputText")
    public value class UserMessageInputText(
      public val `value`: io.openai.model.UserMessageInputText,
    ) : Content

    @Serializable
    @JvmInline
    @SerialName("UserMessageQuotedText")
    public value class UserMessageQuotedText(
      public val `value`: io.openai.model.UserMessageQuotedText,
    ) : Content
  }

  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("chatkit.thread_item")
    ChatkitThreadItem("chatkit.thread_item"),
    ;
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("chatkit.user_message")
    ChatkitUserMessage("chatkit.user_message"),
    ;
  }
}
