package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class VideoStatus(
  public val `value`: String,
) {
  @SerialName("queued")
  Queued("queued"),
  @SerialName("in_progress")
  InProgress("in_progress"),
  @SerialName("completed")
  Completed("completed"),
  @SerialName("failed")
  Failed("failed"),
  ;
}
