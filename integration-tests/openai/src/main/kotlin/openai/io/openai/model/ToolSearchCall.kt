package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class ToolSearchCall(
  @Required
  public val type: Type = Type.ToolSearchCall,
  public val id: String,
  @SerialName("call_id")
  public val callId: String?,
  public val execution: ToolSearchExecutionType,
  public val arguments: JsonElement,
  public val status: FunctionCallStatus,
  @SerialName("created_by")
  public val createdBy: String? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("tool_search_call")
    ToolSearchCall("tool_search_call"),
    ;
  }
}
