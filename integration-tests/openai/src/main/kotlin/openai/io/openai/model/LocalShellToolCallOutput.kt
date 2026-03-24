package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The output of a local shell tool call.
 *
 */
@Serializable
public data class LocalShellToolCallOutput(
  public val type: Type,
  public val id: String,
  public val output: String,
  public val status: Status? = null,
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
    @SerialName("local_shell_call_output")
    LocalShellCallOutput("local_shell_call_output"),
    ;
  }
}
