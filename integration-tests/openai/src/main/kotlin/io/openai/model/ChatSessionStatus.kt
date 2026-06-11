package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class ChatSessionStatus(
  public val `value`: String,
) {
  @SerialName("active")
  Active("active"),
  @SerialName("expired")
  Expired("expired"),
  @SerialName("cancelled")
  Cancelled("cancelled"),
  ;
}
