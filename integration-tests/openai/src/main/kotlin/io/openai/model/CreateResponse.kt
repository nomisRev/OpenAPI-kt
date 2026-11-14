package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateResponse(
  public val metadata: Metadata? = null,
  @SerialName("top_logprobs")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val topLogprobs: Long? = null,
  public val temperature: Double? = null,
  @SerialName("top_p")
  public val topP: Double? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val user: String? = null,
  @SerialName("safety_identifier")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val safetyIdentifier: String? = null,
  @SerialName("prompt_cache_key")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val promptCacheKey: String? = null,
  @SerialName("service_tier")
  public val serviceTier: ServiceTier? = null,
  @SerialName("prompt_cache_retention")
  public val promptCacheRetention: PromptCacheRetention? = null,
  @SerialName("previous_response_id")
  public val previousResponseId: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val model: ModelIdsResponses? = null,
  public val reasoning: Reasoning? = null,
  public val background: Boolean? = null,
  @SerialName("max_output_tokens")
  public val maxOutputTokens: Long? = null,
  @SerialName("max_tool_calls")
  public val maxToolCalls: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val text: ResponseTextParam? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val tools: ToolsArray? = null,
  @SerialName("tool_choice")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val toolChoice: ToolChoiceParam? = null,
  public val prompt: Prompt? = null,
  public val truncation: Truncation? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
  public enum class PromptCacheRetention(
    public val `value`: String,
  ) {
    @SerialName("in-memory")
    InMemory("in-memory"),
    `24h`("24h"),
    ;
  }

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
