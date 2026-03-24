package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A tool call to a computer use tool. See the
 * [computer use guide](/docs/guides/tools-computer-use) for more information.
 *
 */
@Serializable
public data class ComputerToolCall(
  @Required
  public val type: Type = Type.ComputerCall,
  public val id: String,
  @SerialName("call_id")
  public val callId: String,
  public val action: ComputerAction? = null,
  public val actions: ComputerActionList? = null,
  @SerialName("pending_safety_checks")
  public val pendingSafetyChecks: List<ComputerCallSafetyCheckParam>,
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
    @SerialName("computer_call")
    ComputerCall("computer_call"),
    ;
  }
}
