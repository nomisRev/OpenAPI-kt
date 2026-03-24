package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ChatCompletionRequestMessageContentPartRefusal(
  public val type: Type,
  public val refusal: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("refusal")
    Refusal("refusal"),
    ;
  }
}
