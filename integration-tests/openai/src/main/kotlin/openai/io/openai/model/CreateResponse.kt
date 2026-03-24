package io.openai.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CreateResponse(
  @SerialName("previous_response_id")
  public val previousResponseId: String? = null,
  public val model: ModelIdsResponses? = null,
  public val reasoning: Reasoning? = null,
  public val background: Boolean? = null,
  @SerialName("max_output_tokens")
  public val maxOutputTokens: Long? = null,
  @SerialName("max_tool_calls")
  public val maxToolCalls: Long? = null,
  public val text: ResponseTextParam? = null,
  public val tools: ToolsArray? = null,
  @SerialName("tool_choice")
  public val toolChoice: ToolChoiceParam? = null,
  public val prompt: Prompt? = null,
  public val truncation: Truncation? = null,
  public val input: InputParam? = null,
  public val include: List<IncludeEnum>? = null,
  @SerialName("parallel_tool_calls")
  public val parallelToolCalls: Boolean? = null,
  public val store: Boolean? = null,
  public val instructions: String? = null,
  public val stream: Boolean? = null,
  @SerialName("stream_options")
  public val streamOptions: ResponseStreamOptions? = null,
  public val conversation: ConversationParam? = null,
  @SerialName("context_management")
  public val contextManagement: List<ContextManagementParam>? = null,
) {
  @Serializable
  public enum class Truncation(
    public val `value`: String,
  ) {
    @SerialName("auto")
    Auto("auto"),
    @SerialName("disabled")
    Disabled("disabled"),
    ;
  }
}
