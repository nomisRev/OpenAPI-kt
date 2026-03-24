package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Batch(
  public val id: String,
  public val `object`: Object,
  public val endpoint: String,
  public val model: String? = null,
  public val errors: Errors? = null,
  @SerialName("input_file_id")
  public val inputFileId: String,
  @SerialName("completion_window")
  public val completionWindow: String,
  public val status: Status,
  @SerialName("output_file_id")
  public val outputFileId: String? = null,
  @SerialName("error_file_id")
  public val errorFileId: String? = null,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("in_progress_at")
  public val inProgressAt: Long? = null,
  @SerialName("expires_at")
  public val expiresAt: Long? = null,
  @SerialName("finalizing_at")
  public val finalizingAt: Long? = null,
  @SerialName("completed_at")
  public val completedAt: Long? = null,
  @SerialName("failed_at")
  public val failedAt: Long? = null,
  @SerialName("expired_at")
  public val expiredAt: Long? = null,
  @SerialName("cancelling_at")
  public val cancellingAt: Long? = null,
  @SerialName("cancelled_at")
  public val cancelledAt: Long? = null,
  @SerialName("request_counts")
  public val requestCounts: RequestCounts? = null,
  public val usage: Usage? = null,
  public val metadata: Metadata? = null,
) {
  @Serializable
  public data class Errors(
    public val `object`: String? = null,
    public val `data`: List<Data>? = null,
  ) {
    @Serializable
    public data class Data(
      public val code: String? = null,
      public val message: String? = null,
      public val `param`: String? = null,
      public val line: Long? = null,
    )
  }

  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("batch")
    Batch("batch"),
    ;
  }

  /**
   * The request counts for different statuses within the batch.
   */
  @Serializable
  public data class RequestCounts(
    public val total: Long,
    public val completed: Long,
    public val failed: Long,
  )

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("validating")
    Validating("validating"),
    @SerialName("failed")
    Failed("failed"),
    @SerialName("in_progress")
    InProgress("in_progress"),
    @SerialName("finalizing")
    Finalizing("finalizing"),
    @SerialName("completed")
    Completed("completed"),
    @SerialName("expired")
    Expired("expired"),
    @SerialName("cancelling")
    Cancelling("cancelling"),
    @SerialName("cancelled")
    Cancelled("cancelled"),
    ;
  }

  /**
   * Represents token usage details including input tokens, output tokens, a
   * breakdown of output tokens, and the total tokens used. Only populated on
   * batches created after September 7, 2025.
   *
   */
  @Serializable
  public data class Usage(
    @SerialName("input_tokens")
    public val inputTokens: Long,
    @SerialName("input_tokens_details")
    public val inputTokensDetails: InputTokensDetails,
    @SerialName("output_tokens")
    public val outputTokens: Long,
    @SerialName("output_tokens_details")
    public val outputTokensDetails: OutputTokensDetails,
    @SerialName("total_tokens")
    public val totalTokens: Long,
  ) {
    /**
     * A detailed breakdown of the input tokens.
     */
    @JvmInline
    @Serializable
    public value class InputTokensDetails(
      @SerialName("cached_tokens")
      public val cachedTokens: Long,
    )

    /**
     * A detailed breakdown of the output tokens.
     */
    @JvmInline
    @Serializable
    public value class OutputTokensDetails(
      @SerialName("reasoning_tokens")
      public val reasoningTokens: Long,
    )
  }
}
