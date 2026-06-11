package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A function tool that can be used to generate a response.
 *
 */
@Serializable
public data class ChatCompletionTool(
  public val type: Type,
  public val function: FunctionObject,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("function")
    Function("function"),
    ;
  }
}
