package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Specifies a tool the model should use. Use to force the model to call a specific custom tool.
 */
@Serializable
public data class ChatCompletionNamedToolChoiceCustom(
  public val type: Type,
  public val custom: Custom,
) {
  @JvmInline
  @Serializable
  public value class Custom(
    public val name: String,
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
