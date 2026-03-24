package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Collection of workflow tasks grouped together in the thread.
 */
@Serializable
public data class TaskGroupItem(
  public val id: String,
  @Required
  public val `object`: Object = Object.ChatkitThreadItem,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("thread_id")
  public val threadId: String,
  @Required
  public val type: Type = Type.ChatkitTaskGroup,
  public val tasks: List<TaskGroupTask>,
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
    @SerialName("chatkit.task_group")
    ChatkitTaskGroup("chatkit.task_group"),
    ;
  }
}
