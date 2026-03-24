package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A tool that allows the model to execute shell commands in a local environment.
 */
@JvmInline
@Serializable
public value class LocalShellToolParam(
  @Required
  public val type: Type = Type.LocalShell,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("local_shell")
    LocalShell("local_shell"),
    ;
  }
}
