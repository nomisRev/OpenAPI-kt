package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

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
  public val stepDetails: JsonElement,
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
