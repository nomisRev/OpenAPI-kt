package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class ApplyPatchCallOutputStatus(
  public val `value`: String,
) {
  @SerialName("completed")
  Completed("completed"),
  @SerialName("failed")
  Failed("failed"),
  ;
}
