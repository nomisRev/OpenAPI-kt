package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A tool call to run a function. See the 
 * [function calling guide](/docs/guides/function-calling) for more information.
 *
 */
@Serializable
public data class FunctionToolCallResource(
  public val id: String? = null,
  public val type: Type,
  @SerialName("call_id")
  public val callId: String,
  public val namespace: String? = null,
  public val name: String,
  public val arguments: String,
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
    @SerialName("function_call")
    FunctionCall("function_call"),
    ;
  }
}
