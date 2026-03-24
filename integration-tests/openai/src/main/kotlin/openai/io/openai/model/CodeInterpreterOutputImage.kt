package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The image output from the code interpreter.
 */
@Serializable
public data class CodeInterpreterOutputImage(
  @Required
  public val type: Type = Type.Image,
  public val url: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("image")
    Image("image"),
    ;
  }
}
