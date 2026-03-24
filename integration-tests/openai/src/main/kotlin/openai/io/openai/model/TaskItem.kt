package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Task emitted by the workflow to show progress and status updates.
 */
@Serializable
public data class TaskItem(
  public val id: String,
  @Required
  public val `object`: Object = Object.ChatkitThreadItem,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("thread_id")
  public val threadId: String,
  @Required
  public val type: Type = Type.ChatkitTask,
  @SerialName("task_type")
  public val taskType: TaskType,
  public val heading: String?,
  public val summary: String?,
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
    @SerialName("chatkit.task")
    ChatkitTask("chatkit.task"),
    ;
  }
}
