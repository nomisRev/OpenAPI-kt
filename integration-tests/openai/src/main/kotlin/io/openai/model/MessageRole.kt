package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class MessageRole(
  public val `value`: String,
) {
  @SerialName("unknown")
  Unknown("unknown"),
  @SerialName("user")
  User("user"),
  @SerialName("assistant")
  Assistant("assistant"),
  @SerialName("system")
  System("system"),
  @SerialName("critic")
  Critic("critic"),
  @SerialName("discriminator")
  Discriminator("discriminator"),
  @SerialName("developer")
  Developer("developer"),
  @SerialName("tool")
  Tool("tool"),
  ;
}
