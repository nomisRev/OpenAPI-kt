package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class OrderEnum(
  public val `value`: String,
) {
  @SerialName("asc")
  Asc("asc"),
  @SerialName("desc")
  Desc("desc"),
  ;
}
