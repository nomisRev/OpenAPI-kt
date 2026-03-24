package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class RealtimeFunctionTool(
  public val type: Type? = null,
  public val name: String? = null,
  public val description: String? = null,
  public val parameters: JsonElement? = null,
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
