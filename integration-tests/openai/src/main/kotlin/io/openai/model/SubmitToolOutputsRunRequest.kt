package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SubmitToolOutputsRunRequest(
  @SerialName("tool_outputs")
  public val toolOutputs: List<ToolOutputs>,
  public val stream: Boolean? = null,
) {
  @Serializable
  public data class ToolOutputs(
    @SerialName("tool_call_id")
    public val toolCallId: String? = null,
    public val output: String? = null,
  )
}
