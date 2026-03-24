package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class AssistantToolsFunction(
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
