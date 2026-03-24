package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class RankerVersionType(
  public val `value`: String,
) {
  @SerialName("auto")
  Auto("auto"),
  @SerialName("default-2024-11-15")
  Default20241115("default-2024-11-15"),
  ;
}
