package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The output of a computer tool call.
 *
 */
@Serializable
public data class ComputerToolCallOutputResource(
  @Required
  public val type: Type = Type.ComputerCallOutput,
  public val id: String? = null,
  @SerialName("call_id")
  public val callId: String,
  @SerialName("acknowledged_safety_checks")
  public val acknowledgedSafetyChecks: List<ComputerCallSafetyCheckParam>? = null,
  public val output: ComputerScreenshotImage,
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
    @SerialName("computer_call_output")
    ComputerCallOutput("computer_call_output"),
    ;
  }
}
