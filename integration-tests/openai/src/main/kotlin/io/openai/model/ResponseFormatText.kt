package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Default response format. Used to generate text responses.
 *
 */
@Serializable
public data class ResponseFormatText(
  public val type: Type,
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
