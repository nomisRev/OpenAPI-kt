package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A call to a custom tool created by the model.
 *
 */
@Serializable
public data class ChatCompletionMessageCustomToolCall(
  public val id: String,
  public val type: Type,
  public val custom: Custom,
) {
  /**
   * The custom tool that the model called.
   */
  @Serializable
  public data class Custom(
    public val name: String,
    public val input: String,
  )

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("custom")
    Custom("custom"),
    ;
  }
}
