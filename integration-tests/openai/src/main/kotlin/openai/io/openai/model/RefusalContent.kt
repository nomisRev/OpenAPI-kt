package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A refusal from the model.
 */
@Serializable
public data class RefusalContent(
  @Required
  public val type: Type = Type.Refusal,
  public val refusal: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("refusal")
    Refusal("refusal"),
    ;
  }
}
