package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Thread item that renders a widget payload.
 */
@Serializable
public data class WidgetMessageItem(
  public val id: String,
  @Required
  public val `object`: Object = Object.ChatkitThreadItem,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("thread_id")
  public val threadId: String,
  @Required
  public val type: Type = Type.ChatkitWidget,
  public val widget: String,
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
    @SerialName("chatkit.widget")
    ChatkitWidget("chatkit.widget"),
    ;
  }
}
