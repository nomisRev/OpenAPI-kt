package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The `fine_tuning.job.checkpoint` object represents a model checkpoint for a fine-tuning job that is ready to use.
 *
 */
@Serializable
public data class FineTuningJobCheckpoint(
  public val id: String,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("fine_tuned_model_checkpoint")
  public val fineTunedModelCheckpoint: String,
  @SerialName("step_number")
  public val stepNumber: Long,
  public val metrics: Metrics,
  @SerialName("fine_tuning_job_id")
  public val fineTuningJobId: String,
  public val `object`: Object,
) {
  /**
   * Metrics at the step number during the fine-tuning job.
   */
  @Serializable
  public data class Metrics(
    public val step: Double? = null,
    @SerialName("train_loss")
    public val trainLoss: Double? = null,
    @SerialName("train_mean_token_accuracy")
    public val trainMeanTokenAccuracy: Double? = null,
    @SerialName("valid_loss")
    public val validLoss: Double? = null,
    @SerialName("valid_mean_token_accuracy")
    public val validMeanTokenAccuracy: Double? = null,
    @SerialName("full_valid_loss")
    public val fullValidLoss: Double? = null,
    @SerialName("full_valid_mean_token_accuracy")
    public val fullValidMeanTokenAccuracy: Double? = null,
  )

  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("fine_tuning.job.checkpoint")
    FineTuningJobCheckpoint("fine_tuning.job.checkpoint"),
    ;
  }
}
