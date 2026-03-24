package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Record of a client side tool invocation initiated by the assistant.
 */
@Serializable
public data class ClientToolCallItem(
  public val id: String,
  @Required
  public val `object`: Object = Object.ChatkitThreadItem,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("thread_id")
  public val threadId: String,
  @Required
  public val type: Type = Type.ChatkitClientToolCall,
  public val status: ClientToolCallStatus,
  @SerialName("call_id")
  public val callId: String,
  public val name: String,
  public val arguments: String,
  public val output: String?,
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
    @SerialName("chatkit.client_tool_call")
    ChatkitClientToolCall("chatkit.client_tool_call"),
    ;
  }
}
