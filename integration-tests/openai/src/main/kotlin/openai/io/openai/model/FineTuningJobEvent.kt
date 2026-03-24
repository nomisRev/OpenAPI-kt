package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Fine-tuning job event object
 */
@Serializable
public data class FineTuningJobEvent(
  public val `object`: Object,
  public val id: String,
  @SerialName("created_at")
  public val createdAt: Long,
  public val level: Level,
  public val message: String,
  public val type: Type? = null,
  public val `data`: JsonElement? = null,
) {
  @Serializable
  public enum class Level(
    public val `value`: String,
  ) {
    @SerialName("info")
    Info("info"),
    @SerialName("warn")
    Warn("warn"),
    @SerialName("error")
    Error("error"),
    ;
  }

  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("fine_tuning.job.event")
    FineTuningJobEvent("fine_tuning.job.event"),
    ;
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("message")
    Message("message"),
    @SerialName("metrics")
    Metrics("metrics"),
    ;
  }
}
