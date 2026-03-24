package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A call to a custom tool created by the model.
 *
 */
@Serializable
public data class CustomToolCall(
  public val type: Type,
  public val id: String? = null,
  @SerialName("call_id")
  public val callId: String,
  public val namespace: String? = null,
  public val name: String,
  public val input: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("custom_tool_call")
    CustomToolCall("custom_tool_call"),
    ;
  }
}
