package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Indicates that the shell call exceeded its configured time limit.
 */
@JvmInline
@Serializable
public value class FunctionShellCallOutputTimeoutOutcome(
  @Required
  public val type: Type = Type.Timeout,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("timeout")
    Timeout("timeout"),
    ;
  }
}
