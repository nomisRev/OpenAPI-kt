package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Indicates that the shell commands finished and returned an exit code.
 */
@Serializable
public data class FunctionShellCallOutputExitOutcome(
  @Required
  public val type: Type = Type.Exit,
  @SerialName("exit_code")
  public val exitCode: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("exit")
    Exit("exit"),
    ;
  }
}
