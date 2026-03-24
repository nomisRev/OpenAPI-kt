package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * A schema representing an evaluation run.
 *
 */
@Serializable
public data class EvalRun(
  @Required
  public val `object`: Object = Object.EvalRun,
  public val id: String,
  @SerialName("eval_id")
  public val evalId: String,
  public val status: String,
  public val model: String,
  public val name: String,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("report_url")
  public val reportUrl: String,
  @SerialName("result_counts")
  public val resultCounts: ResultCounts,
  @SerialName("per_model_usage")
  public val perModelUsage: List<PerModelUsage>,
  @SerialName("per_testing_criteria_results")
  public val perTestingCriteriaResults: List<PerTestingCriteriaResults>,
  @SerialName("data_source")
  public val dataSource: JsonElement,
  public val metadata: Metadata,
  public val error: EvalApiError,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("eval.run")
    EvalRun("eval.run"),
    ;
  }

  @Serializable
  public data class PerModelUsage(
    @SerialName("model_name")
    public val modelName: String,
    @SerialName("invocation_count")
    public val invocationCount: Long,
    @SerialName("prompt_tokens")
    public val promptTokens: Long,
    @SerialName("completion_tokens")
    public val completionTokens: Long,
    @SerialName("total_tokens")
    public val totalTokens: Long,
    @SerialName("cached_tokens")
    public val cachedTokens: Long,
  )

  @Serializable
  public data class PerTestingCriteriaResults(
    @SerialName("testing_criteria")
    public val testingCriteria: String,
    public val passed: Long,
    public val failed: Long,
  )

  /**
   * Counters summarizing the outcomes of the evaluation run.
   */
  @Serializable
  public data class ResultCounts(
    public val total: Long,
    public val errored: Long,
    public val failed: Long,
    public val passed: Long,
  )
}
