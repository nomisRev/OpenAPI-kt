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

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface ThreadItem {
  /**
   * User-authored messages within a thread.
   */
  @SerialName("chatkit.user_message")
  @Serializable
  public data class ChatkitUserMessage(
    public val id: String,
    @Required
    public val `object`: Object = Object.ChatkitThreadItem,
    @SerialName("created_at")
    public val createdAt: Long,
    @SerialName("thread_id")
    public val threadId: String,
    public val content: List<Content>,
    public val attachments: List<Attachment>,
    @SerialName("inference_options")
    public val inferenceOptions: InferenceOptions?,
  ) : ThreadItem {
    /**
     * Content blocks that comprise a user message.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("type")
    @Serializable
    public sealed interface Content {
      /**
       * Text block that a user contributed to the thread.
       */
      @JvmInline
      @SerialName("input_text")
      @Serializable
      public value class InputText(
        public val text: String,
      ) : Content

      /**
       * Quoted snippet that the user referenced in their message.
       */
      @JvmInline
      @SerialName("quoted_text")
      @Serializable
      public value class QuotedText(
        public val text: String,
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
  }

  /**
   * Assistant-authored message within a thread.
   */
  @SerialName("chatkit.assistant_message")
  @Serializable
  public data class ChatkitAssistantMessage(
    public val id: String,
    @Required
    public val `object`: Object = Object.ChatkitThreadItem,
    @SerialName("created_at")
    public val createdAt: Long,
    @SerialName("thread_id")
    public val threadId: String,
    public val content: List<ResponseOutputText>,
  ) : ThreadItem {
    @Serializable
    public enum class Object(
      public val `value`: String,
    ) {
      @SerialName("chatkit.thread_item")
      ChatkitThreadItem("chatkit.thread_item"),
      ;
    }
  }

  /**
   * Thread item that renders a widget payload.
   */
  @SerialName("chatkit.widget")
  @Serializable
  public data class ChatkitWidget(
    public val id: String,
    @Required
    public val `object`: Object = Object.ChatkitThreadItem,
    @SerialName("created_at")
    public val createdAt: Long,
    @SerialName("thread_id")
    public val threadId: String,
    public val widget: String,
  ) : ThreadItem {
    @Serializable
    public enum class Object(
      public val `value`: String,
    ) {
      @SerialName("chatkit.thread_item")
      ChatkitThreadItem("chatkit.thread_item"),
      ;
    }
  }

  /**
   * Record of a client side tool invocation initiated by the assistant.
   */
  @SerialName("chatkit.client_tool_call")
  @Serializable
  public data class ChatkitClientToolCall(
    public val id: String,
    @Required
    public val `object`: Object = Object.ChatkitThreadItem,
    @SerialName("created_at")
    public val createdAt: Long,
    @SerialName("thread_id")
    public val threadId: String,
    public val status: ClientToolCallStatus,
    @SerialName("call_id")
    public val callId: String,
    public val name: String,
    public val arguments: String,
    public val output: String?,
  ) : ThreadItem {
    @Serializable
    public enum class Object(
      public val `value`: String,
    ) {
      @SerialName("chatkit.thread_item")
      ChatkitThreadItem("chatkit.thread_item"),
      ;
    }
  }

  /**
   * Task emitted by the workflow to show progress and status updates.
   */
  @SerialName("chatkit.task")
  @Serializable
  public data class ChatkitTask(
    public val id: String,
    @Required
    public val `object`: Object = Object.ChatkitThreadItem,
    @SerialName("created_at")
    public val createdAt: Long,
    @SerialName("thread_id")
    public val threadId: String,
    @SerialName("task_type")
    public val taskType: TaskType,
    public val heading: String?,
    public val summary: String?,
  ) : ThreadItem {
    @Serializable
    public enum class Object(
      public val `value`: String,
    ) {
      @SerialName("chatkit.thread_item")
      ChatkitThreadItem("chatkit.thread_item"),
      ;
    }
  }

  /**
   * Collection of workflow tasks grouped together in the thread.
   */
  @SerialName("chatkit.task_group")
  @Serializable
  public data class ChatkitTaskGroup(
    public val id: String,
    @Required
    public val `object`: Object = Object.ChatkitThreadItem,
    @SerialName("created_at")
    public val createdAt: Long,
    @SerialName("thread_id")
    public val threadId: String,
    public val tasks: List<TaskGroupTask>,
  ) : ThreadItem {
    @Serializable
    public enum class Object(
      public val `value`: String,
    ) {
      @SerialName("chatkit.thread_item")
      ChatkitThreadItem("chatkit.thread_item"),
      ;
    }
  }
}
