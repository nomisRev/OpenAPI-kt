package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An action to type in text.
 */
@Serializable
public data class TypeParam(
  @Required
  public val type: Type = Type.Type,
  public val text: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("type")
    Type("type"),
    ;
  }
}
