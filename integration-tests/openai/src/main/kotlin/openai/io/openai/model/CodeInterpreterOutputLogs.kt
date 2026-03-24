package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The logs output from the code interpreter.
 */
@Serializable
public data class CodeInterpreterOutputLogs(
  @Required
  public val type: Type = Type.Logs,
  public val logs: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("logs")
    Logs("logs"),
    ;
  }
}
