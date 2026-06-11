package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Learn about [text inputs](/docs/guides/text-generation).
 *
 */
@Serializable
public data class ChatCompletionRequestMessageContentPartText(
  public val type: Type,
  public val text: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("text")
    Text("text"),
    ;
  }
}
