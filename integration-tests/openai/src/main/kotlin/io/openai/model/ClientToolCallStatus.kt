package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class ClientToolCallStatus(
  public val `value`: String,
) {
  @SerialName("in_progress")
  InProgress("in_progress"),
  @SerialName("completed")
  Completed("completed"),
  ;
}
