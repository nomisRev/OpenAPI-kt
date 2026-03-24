package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

/**
 * Defines a function in your own code the model can choose to call. Learn more about [function calling](https://platform.openai.com/docs/guides/function-calling).
 */
@Serializable
public data class FunctionTool(
  @Required
  public val type: Type = Type.Function,
  public val name: String,
  public val description: String? = null,
  @Required
  public val parameters: JsonArray? = null,
  public val strict: Boolean?,
  @SerialName("defer_loading")
  public val deferLoading: Boolean? = null,
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
