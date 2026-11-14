package io.openai.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SubmitToolOutputsRunRequest(
  @SerialName("tool_outputs")
  public val toolOutputs: List<ToolOutputs>,
  public val stream: Boolean? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ToolOutputs(
    @SerialName("tool_call_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val toolCallId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val output: String? = null,
  )
}
