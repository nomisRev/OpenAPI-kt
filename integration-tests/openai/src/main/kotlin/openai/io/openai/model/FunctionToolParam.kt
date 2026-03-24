package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class FunctionToolParam(
  public val name: String,
  public val description: String? = null,
  public val parameters: EmptyModelParam? = null,
  public val strict: Boolean? = null,
  @Required
  public val type: Type = Type.Function,
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
