package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
public data class RunGraderResponse(
  public val reward: Double,
  public val metadata: Metadata,
  @SerialName("sub_rewards")
  @Required
  public val subRewards: JsonArray = emptyList(),
  @SerialName("model_grader_token_usage_per_model")
  @Required
  public val modelGraderTokenUsagePerModel: JsonArray = emptyList(),
) {
  @Serializable
  public data class Metadata(
    public val name: String,
    public val type: String,
    public val errors: Errors,
    @SerialName("execution_time")
    public val executionTime: Double,
    @Required
    public val scores: JsonArray = JsonArray(emptyList()),
    @SerialName("token_usage")
    public val tokenUsage: Long?,
    @SerialName("sampled_model_name")
    public val sampledModelName: String?,
  ) {
    @Serializable
    public data class Errors(
      @SerialName("formula_parse_error")
      public val formulaParseError: Boolean,
      @SerialName("sample_parse_error")
      public val sampleParseError: Boolean,
      @SerialName("truncated_observation_error")
      public val truncatedObservationError: Boolean,
      @SerialName("unresponsive_reward_error")
      public val unresponsiveRewardError: Boolean,
      @SerialName("invalid_variable_error")
      public val invalidVariableError: Boolean,
      @SerialName("other_error")
      public val otherError: Boolean,
      @SerialName("python_grader_server_error")
      public val pythonGraderServerError: Boolean,
      @SerialName("python_grader_server_error_type")
      public val pythonGraderServerErrorType: String?,
      @SerialName("python_grader_runtime_error")
      public val pythonGraderRuntimeError: Boolean,
      @SerialName("python_grader_runtime_error_details")
      public val pythonGraderRuntimeErrorDetails: String?,
      @SerialName("model_grader_server_error")
      public val modelGraderServerError: Boolean,
      @SerialName("model_grader_refusal_error")
      public val modelGraderRefusalError: Boolean,
      @SerialName("model_grader_parse_error")
      public val modelGraderParseError: Boolean,
      @SerialName("model_grader_server_error_details")
      public val modelGraderServerErrorDetails: String?,
    )
  }
}
