package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ChatCompletionRequestFunctionMessage(
  public val role: Role,
  public val content: String?,
  public val name: String,
) {
  @Serializable
  public enum class Role(
    public val `value`: String,
  ) {
    @SerialName("function")
    Function("function"),
    ;
  }
}
