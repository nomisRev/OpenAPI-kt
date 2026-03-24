package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * A schema representing an evaluation run output item.
 *
 */
@Serializable
public data class EvalRunOutputItem(
  @Required
  public val `object`: Object = Object.EvalRunOutputItem,
  public val id: String,
  @SerialName("run_id")
  public val runId: String,
  @SerialName("eval_id")
  public val evalId: String,
  @SerialName("created_at")
  public val createdAt: Long,
  public val status: String,
  @SerialName("datasource_item_id")
  public val datasourceItemId: Long,
  @SerialName("datasource_item")
  public val datasourceItem: JsonElement,
  public val results: List<EvalRunOutputItemResult>,
  public val sample: Sample,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("eval.run.output_item")
    EvalRunOutputItem("eval.run.output_item"),
    ;
  }

  /**
   * A sample containing the input and output of the evaluation run.
   */
  @Serializable
  public data class Sample(
    public val input: List<Input>,
    public val output: List<Output>,
    @SerialName("finish_reason")
    public val finishReason: String,
    public val model: String,
    public val usage: Usage,
    public val error: EvalApiError,
    public val temperature: Double,
    @SerialName("max_completion_tokens")
    public val maxCompletionTokens: Long,
    @SerialName("top_p")
    public val topP: Double,
    public val seed: Long,
  ) {
    /**
     * An input message.
     */
    @Serializable
    public data class Input(
      public val role: String,
      public val content: String,
    )

    @Serializable
    public data class Output(
      public val role: String? = null,
      public val content: String? = null,
    )

    /**
     * Token usage details for the sample.
     */
    @Serializable
    public data class Usage(
      @SerialName("total_tokens")
      public val totalTokens: Long,
      @SerialName("completion_tokens")
      public val completionTokens: Long,
      @SerialName("prompt_tokens")
      public val promptTokens: Long,
      @SerialName("cached_tokens")
      public val cachedTokens: Long,
    )
  }
}
