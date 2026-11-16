package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sent when a fine-tuning job has been cancelled.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class WebhookFineTuningJobCancelled(
  @SerialName("created_at")
  public val createdAt: Long,
  public val id: String,
  public val `data`: Data,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val `object`: Object? = null,
  public val type: Type,
) {
  /**
   * Event data payload.
   *
   */
  @Serializable
  public data class Data(
    public val id: String,
  )

  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("event")
    Event("event"),
    ;
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("fine_tuning.job.cancelled")
    FineTuningJobCancelled("fine_tuning.job.cancelled"),
    ;
  }
}
