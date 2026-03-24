package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A tool call to run a command on the local shell.
 *
 */
@Serializable
public data class LocalShellToolCall(
  public val type: Type,
  public val id: String,
  @SerialName("call_id")
  public val callId: String,
  public val action: LocalShellExecAction,
  public val status: Status,
) {
  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("in_progress")
    InProgress("in_progress"),
    @SerialName("completed")
    Completed("completed"),
    @SerialName("incomplete")
    Incomplete("incomplete"),
    ;
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("local_shell_call")
    LocalShellCall("local_shell_call"),
    ;
  }
}
