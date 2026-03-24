package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A collection of keypresses the model would like to perform.
 */
@Serializable
public data class KeyPressAction(
  @Required
  public val type: Type = Type.Keypress,
  public val keys: List<String>,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("keypress")
    Keypress("keypress"),
    ;
  }
}
