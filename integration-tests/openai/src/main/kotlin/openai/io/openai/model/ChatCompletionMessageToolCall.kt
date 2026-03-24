package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A call to a function tool created by the model.
 *
 */
@Serializable
public data class ChatCompletionMessageToolCall(
  public val id: String,
  public val type: Type,
  public val function: Function,
) {
  /**
   * The function that the model called.
   */
  @Serializable
  public data class Function(
    public val name: String,
    public val arguments: String,
  )

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("function")
    Function("function"),
    ;
  }
}
