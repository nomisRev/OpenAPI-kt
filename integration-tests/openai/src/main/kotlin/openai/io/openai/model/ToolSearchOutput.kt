package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ToolSearchOutput(
  @Required
  public val type: Type = Type.ToolSearchOutput,
  public val id: String,
  @SerialName("call_id")
  public val callId: String?,
  public val execution: ToolSearchExecutionType,
  public val tools: List<Tool>,
  public val status: FunctionCallOutputStatusEnum,
  @SerialName("created_by")
  public val createdBy: String? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("tool_search_output")
    ToolSearchOutput("tool_search_output"),
    ;
  }
}
