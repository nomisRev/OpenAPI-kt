package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Unconstrained free-form text.
 */
@JvmInline
@Serializable
public value class CustomTextFormatParam(
  @Required
  public val type: Type = Type.Text,
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
