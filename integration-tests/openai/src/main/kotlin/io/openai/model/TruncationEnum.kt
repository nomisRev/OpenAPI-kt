package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class TruncationEnum(
  public val `value`: String,
) {
  @SerialName("auto")
  Auto("auto"),
  @SerialName("disabled")
  Disabled("disabled"),
  ;
}
