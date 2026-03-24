package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Constrains the tools available to the model to a pre-defined set.
 *
 */
@Serializable
public data class ChatCompletionAllowedToolsChoice(
  public val type: Type,
  @SerialName("allowed_tools")
  public val allowedTools: ChatCompletionAllowedTools,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("allowed_tools")
    AllowedTools("allowed_tools"),
    ;
  }
}
