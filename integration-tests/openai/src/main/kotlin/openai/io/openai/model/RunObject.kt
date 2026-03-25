package io.openai.model

import kotlin.Boolean
import kotlin.Double
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
 * Represents an execution run on a [thread](/docs/api-reference/threads).
 */
@Serializable
public data class RunObject(
  public val id: String,
  public val `object`: Object,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("thread_id")
  public val threadId: String,
  @SerialName("assistant_id")
  public val assistantId: String,
  public val status: Status,
  @SerialName("required_action")
  public val requiredAction: RequiredAction?,
  @SerialName("last_error")
  public val lastError: LastError?,
  @SerialName("expires_at")
  public val expiresAt: Long?,
  @SerialName("started_at")
  public val startedAt: Long?,
  @SerialName("cancelled_at")
  public val cancelledAt: Long?,
  @SerialName("failed_at")
  public val failedAt: Long?,
  @SerialName("completed_at")
  public val completedAt: Long?,
  @SerialName("incomplete_details")
  public val incompleteDetails: IncompleteDetails?,
  public val model: String,
  public val instructions: String,
  @Required
  public val tools: List<Tools> = emptyList(),
  public val metadata: Metadata,
  public val usage: RunCompletionUsage,
  public val temperature: Double? = null,
  @SerialName("top_p")
  public val topP: Double? = null,
  @SerialName("max_prompt_tokens")
  public val maxPromptTokens: Long?,
  @SerialName("max_completion_tokens")
  public val maxCompletionTokens: Long?,
  @SerialName("truncation_strategy")
  public val truncationStrategy: TruncationObject?,
  @SerialName("tool_choice")
  public val toolChoice: AssistantsApiToolChoiceOption?,
  @SerialName("parallel_tool_calls")
  public val parallelToolCalls: ParallelToolCalls,
  @SerialName("response_format")
  public val responseFormat: AssistantsApiResponseFormatOption,
) {
  /**
   * Details on why the run is incomplete. Will be `null` if the run is not incomplete.
   */
  @JvmInline
  @Serializable
  public value class IncompleteDetails(
    public val reason: Reason? = null,
  ) {
    @Serializable
    public enum class Reason(
      public val `value`: String,
    ) {
      @SerialName("max_completion_tokens")
      MaxCompletionTokens("max_completion_tokens"),
      @SerialName("max_prompt_tokens")
      MaxPromptTokens("max_prompt_tokens"),
      ;
    }
  }

  /**
   * The last error associated with this run. Will be `null` if there are no errors.
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
      @SerialName("invalid_prompt")
      InvalidPrompt("invalid_prompt"),
      ;
    }
  }

  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("thread.run")
    ThreadRun("thread.run"),
    ;
  }

  /**
   * Details on the action required to continue the run. Will be `null` if no action is required.
   */
  @Serializable
  public data class RequiredAction(
    public val type: Type,
    @SerialName("submit_tool_outputs")
    public val submitToolOutputs: SubmitToolOutputs,
  ) {
    /**
     * Details on the tool outputs needed for this run to continue.
     */
    @JvmInline
    @Serializable
    public value class SubmitToolOutputs(
      @SerialName("tool_calls")
      public val toolCalls: List<RunToolCallObject>,
    )

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("submit_tool_outputs")
      SubmitToolOutputs("submit_tool_outputs"),
      ;
    }
  }

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("queued")
    Queued("queued"),
    @SerialName("in_progress")
    InProgress("in_progress"),
    @SerialName("requires_action")
    RequiresAction("requires_action"),
    @SerialName("cancelling")
    Cancelling("cancelling"),
    @SerialName("cancelled")
    Cancelled("cancelled"),
    @SerialName("failed")
    Failed("failed"),
    @SerialName("completed")
    Completed("completed"),
    @SerialName("incomplete")
    Incomplete("incomplete"),
    @SerialName("expired")
    Expired("expired"),
    ;
  }

  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Tools {
    @Serializable
    @SerialName("code_interpreter")
    public data object CodeInterpreter : Tools

    @JvmInline
    @SerialName("file_search")
    @Serializable
    public value class FileSearch(
      @SerialName("file_search")
      public val fileSearch: FileSearch? = null,
    ) : Tools {
      /**
       * Overrides for the file search tool.
       */
      @Serializable
      public data class FileSearch(
        @SerialName("max_num_results")
        public val maxNumResults: Long? = null,
        @SerialName("ranking_options")
        public val rankingOptions: FileSearchRankingOptions? = null,
      )
    }

    @SerialName("function")
    @Serializable
    public data class Function(
      public val description: String? = null,
      public val name: String,
      public val parameters: FunctionParameters? = null,
      public val strict: Boolean? = null,
    ) : Tools
  }
}
