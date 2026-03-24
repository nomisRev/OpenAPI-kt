package io.openai.model

import kotlin.Double
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Usage statistics for models billed by audio input duration.
 */
@Serializable
public data class TranscriptTextUsageDuration(
  public val type: Type,
  public val seconds: Double,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("duration")
    Duration("duration"),
    ;
  }
}
