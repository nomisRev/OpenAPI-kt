package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Forces the model to call the shell tool when a tool call is required.
 */
@JvmInline
@Serializable
public value class SpecificFunctionShellParam(
  @Required
  public val type: Type = Type.Shell,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("shell")
    Shell("shell"),
    ;
  }
}
