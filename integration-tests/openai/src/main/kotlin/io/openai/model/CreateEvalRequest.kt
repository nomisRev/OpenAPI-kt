package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonElement

@Serializable
public data class CreateEvalRequest(
  public val name: String? = null,
  public val metadata: Metadata? = null,
  @SerialName("data_source_config")
  public val dataSourceConfig: DataSourceConfig,
  @SerialName("testing_criteria")
  public val testingCriteria: List<TestingCriteria>,
) {
  /**
   * The configuration for the data source used for the evaluation runs. Dictates the schema of the data used in the evaluation.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface DataSourceConfig {
    /**
     * A CustomDataSourceConfig object that defines the schema for the data source used for the evaluation runs.
     * This schema is used to define the shape of the data that will be:
     * - Used to define your testing criteria and
     * - What data is required when creating a run
     *
     */
    @SerialName("custom")
    @Serializable
    public data class Custom(
      @SerialName("item_schema")
      public val itemSchema: JsonElement,
      @SerialName("include_sample_schema")
      public val includeSampleSchema: Boolean? = null,
    ) : DataSourceConfig

    /**
     * A data source config which specifies the metadata property of your logs query.
     * This is usually metadata like `usecase=chatbot` or `prompt-version=v2`, etc.
     *
     */
    @JvmInline
    @SerialName("logs")
    @Serializable
    public value class Logs(
      public val metadata: JsonElement? = null,
    ) : DataSourceConfig

    /**
     * Deprecated in favor of LogsDataSourceConfig.
     *
     */
    @JvmInline
    @SerialName("stored_completions")
    @Serializable
    public value class StoredCompletions(
      public val metadata: JsonElement? = null,
    ) : DataSourceConfig
  }

  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface TestingCriteria {
    /**
     * A LabelModelGrader object which uses a model to assign labels to each item
     * in the evaluation.
     *
     */
    @SerialName("label_model")
    @Serializable
    public data class LabelModel(
      public val name: String,
      public val model: String,
      public val input: List<CreateEvalItem>,
      public val labels: List<String>,
      @SerialName("passing_labels")
      public val passingLabels: List<String>,
    ) : TestingCriteria

    /**
     * A StringCheckGrader object that performs a string comparison between input and reference using a specified operation.
     *
     */
    @SerialName("string_check")
    @Serializable
    public data class StringCheck(
      public val name: String,
      public val input: String,
      public val reference: String,
      public val operation: Operation,
    ) : TestingCriteria {
      @Serializable
      public enum class Operation(
        public val `value`: String,
      ) {
        @SerialName("eq")
        Eq("eq"),
        @SerialName("ne")
        Ne("ne"),
        @SerialName("like")
        Like("like"),
        @SerialName("ilike")
        Ilike("ilike"),
        ;
      }
    }

    /**
     * A TextSimilarityGrader object which grades text based on similarity metrics.
     *
     */
    @SerialName("text_similarity")
    @Serializable
    public data class TextSimilarity(
      public val name: String,
      public val input: String,
      public val reference: String,
      @SerialName("evaluation_metric")
      public val evaluationMetric: EvaluationMetric,
      @SerialName("pass_threshold")
      public val passThreshold: Double,
    ) : TestingCriteria {
      @Serializable
      public enum class EvaluationMetric(
        public val `value`: String,
      ) {
        @SerialName("cosine")
        Cosine("cosine"),
        @SerialName("fuzzy_match")
        FuzzyMatch("fuzzy_match"),
        @SerialName("bleu")
        Bleu("bleu"),
        @SerialName("gleu")
        Gleu("gleu"),
        @SerialName("meteor")
        Meteor("meteor"),
        @SerialName("rouge_1")
        Rouge1("rouge_1"),
        @SerialName("rouge_2")
        Rouge2("rouge_2"),
        @SerialName("rouge_3")
        Rouge3("rouge_3"),
        @SerialName("rouge_4")
        Rouge4("rouge_4"),
        @SerialName("rouge_5")
        Rouge5("rouge_5"),
        @SerialName("rouge_l")
        RougeL("rouge_l"),
        ;
      }
    }

    /**
     * A PythonGrader object that runs a python script on the input.
     *
     */
    @SerialName("python")
    @Serializable
    public data class Python(
      public val name: String,
      public val source: String,
      @SerialName("image_tag")
      public val imageTag: String? = null,
      @SerialName("pass_threshold")
      public val passThreshold: Double? = null,
    ) : TestingCriteria

    /**
     * A ScoreModelGrader object that uses a model to assign a score to the input.
     *
     */
    @SerialName("score_model")
    @Serializable
    public data class ScoreModel(
      public val name: String,
      public val model: String,
      @SerialName("sampling_params")
      public val samplingParams: SamplingParams? = null,
      public val input: List<EvalItem>,
      public val range: List<Double>? = null,
      @SerialName("pass_threshold")
      public val passThreshold: Double? = null,
    ) : TestingCriteria {
      /**
       * The sampling parameters for the model.
       */
      @Serializable
      public data class SamplingParams(
        public val seed: Long? = null,
        @SerialName("top_p")
        public val topP: Double? = null,
        public val temperature: Double? = null,
        @SerialName("max_completions_tokens")
        public val maxCompletionsTokens: Long? = null,
        @SerialName("reasoning_effort")
        public val reasoningEffort: ReasoningEffort? = null,
      )
    }
  }
}
