package io.openai.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface ThreadItem {
  @Serializable
  @JvmInline
  @SerialName("UserMessageItem")
  public value class UserMessageItem(
    public val `value`: io.openai.model.UserMessageItem,
  ) : ThreadItem

  @Serializable
  @JvmInline
  @SerialName("AssistantMessageItem")
  public value class AssistantMessageItem(
    public val `value`: io.openai.model.AssistantMessageItem,
  ) : ThreadItem

  @Serializable
  @JvmInline
  @SerialName("WidgetMessageItem")
  public value class WidgetMessageItem(
    public val `value`: io.openai.model.WidgetMessageItem,
  ) : ThreadItem

  @Serializable
  @JvmInline
  @SerialName("ClientToolCallItem")
  public value class ClientToolCallItem(
    public val `value`: io.openai.model.ClientToolCallItem,
  ) : ThreadItem

  @Serializable
  @JvmInline
  @SerialName("TaskItem")
  public value class TaskItem(
    public val `value`: io.openai.model.TaskItem,
  ) : ThreadItem

  @Serializable
  @JvmInline
  @SerialName("TaskGroupItem")
  public value class TaskGroupItem(
    public val `value`: io.openai.model.TaskGroupItem,
  ) : ThreadItem
}
