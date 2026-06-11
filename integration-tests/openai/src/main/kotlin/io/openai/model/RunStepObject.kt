package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Represents a step in execution of a run.
 *
 */
@Serializable
public data class RunStepObject(
  public val id: String,
  public val `object`: Object,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("assistant_id")
  public val assistantId: String,
  @SerialName("thread_id")
  public val threadId: String,
  @SerialName("run_id")
  public val runId: String,
  public val type: Type,
  public val status: Status,
  @SerialName("step_details")
  public val stepDetails: StepDetails,
  @SerialName("last_error")
  public val lastError: LastError?,
  @SerialName("expired_at")
  public val expiredAt: Long?,
  @SerialName("cancelled_at")
  public val cancelledAt: Long?,
  @SerialName("failed_at")
  public val failedAt: Long?,
  @SerialName("completed_at")
  public val completedAt: Long?,
  public val metadata: Metadata,
  public val usage: RunStepCompletionUsage,
) {
  /**
   * The last error associated with this run step. Will be `null` if there are no errors.
   */
  @Serializable
  public data class LastError(
    public val code: Code,
    public val message: String,
  ) {
    @Serializable
    public enum class Code(
      public val `value`: String,
    ) {
      @SerialName("server_error")
      ServerError("server_error"),
      @SerialName("rate_limit_exceeded")
      RateLimitExceeded("rate_limit_exceeded"),
      ;
    }
  }

  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("thread.run.step")
    ThreadRunStep("thread.run.step"),
    ;
  }

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("in_progress")
    InProgress("in_progress"),
    @SerialName("cancelled")
    Cancelled("cancelled"),
    @SerialName("failed")
    Failed("failed"),
    @SerialName("completed")
    Completed("completed"),
    @SerialName("expired")
    Expired("expired"),
    ;
  }

  /**
   * The details of the run step.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface StepDetails {
    /**
     * Details of the message creation by the run step.
     */
    @JvmInline
    @SerialName("message_creation")
    @Serializable
    public value class MessageCreation(
      @SerialName("message_id")
      public val messageId: String,
    ) : StepDetails

    /**
     * Details of the tool call.
     */
    @JvmInline
    @SerialName("tool_calls")
    @Serializable
    public value class ToolCalls(
      @SerialName("tool_calls")
      public val toolCalls: List<ToolCalls>,
    ) : StepDetails {
      @OptIn(ExperimentalSerializationApi::class)
      @JsonClassDiscriminator("type")
      @Serializable
      public sealed interface ToolCalls {
        /**
         * Details of the Code Interpreter tool call the run step was involved in.
         */
        @SerialName("code_interpreter")
        @Serializable
        public data class CodeInterpreter(
          public val id: String,
          @SerialName("code_interpreter")
          public val codeInterpreter: CodeInterpreter,
        ) : ToolCalls {
          /**
           * The Code Interpreter tool call definition.
           */
          @Serializable
          public data class CodeInterpreter(
            public val input: String,
            public val outputs: List<Outputs>,
          ) {
            @OptIn(ExperimentalSerializationApi::class)
            @JsonClassDiscriminator("type")
            @Serializable
            public sealed interface Outputs {
              /**
               * Text output from the Code Interpreter tool call as part of a run step.
               */
              @JvmInline
              @SerialName("logs")
              @Serializable
              public value class Logs(
                public val logs: String,
              ) : Outputs

              @JvmInline
              @SerialName("image")
              @Serializable
              public value class Image(
                @SerialName("file_id")
                public val fileId: String,
              ) : Outputs
            }
          }
        }

        @SerialName("file_search")
        @Serializable
        public data class FileSearch(
          public val id: String,
          @SerialName("file_search")
          public val fileSearch: FileSearch,
        ) : ToolCalls {
          /**
           * For now, this is always going to be an empty object.
           */
          @Serializable
          public data class FileSearch(
            @SerialName("ranking_options")
            public val rankingOptions:
                RunStepDetailsToolCallsFileSearchRankingOptionsObject? = null,
            public val results: List<RunStepDetailsToolCallsFileSearchResultObject>? = null,
          )
        }

        @SerialName("function")
        @Serializable
        public data class Function(
          public val id: String,
          public val function: Function,
        ) : ToolCalls {
          /**
           * The definition of the function that was called.
           */
          @Serializable
          public data class Function(
            public val name: String,
            public val arguments: String,
            public val output: String?,
          )
        }
      }
    }
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("message_creation")
    MessageCreation("message_creation"),
    @SerialName("tool_calls")
    ToolCalls("tool_calls"),
    ;
  }
}
