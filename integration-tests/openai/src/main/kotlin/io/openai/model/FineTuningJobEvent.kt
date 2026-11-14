package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Fine-tuning job event object
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class FineTuningJobEvent(
  public val `object`: Object,
  public val id: String,
  @SerialName("created_at")
  public val createdAt: Long,
  public val level: Level,
  public val message: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: Type? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
