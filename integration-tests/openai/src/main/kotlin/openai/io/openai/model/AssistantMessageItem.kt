package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Assistant-authored message within a thread.
 */
@Serializable
public data class AssistantMessageItem(
  public val id: String,
  @Required
  public val `object`: Object = Object.ChatkitThreadItem,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("thread_id")
  public val threadId: String,
  @Required
  public val type: Type = Type.ChatkitAssistantMessage,
  public val content: List<ResponseOutputText>,
) {
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
    @SerialName("chatkit.assistant_message")
    ChatkitAssistantMessage("chatkit.assistant_message"),
    ;
  }
}
