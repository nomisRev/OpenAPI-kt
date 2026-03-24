package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The text content that is part of a message.
 */
@Serializable
public data class MessageRequestContentTextObject(
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
