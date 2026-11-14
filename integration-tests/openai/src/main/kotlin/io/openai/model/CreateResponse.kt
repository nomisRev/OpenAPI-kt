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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val metadata: Optional<Metadata> = Optional.Absent,
  @SerialName("top_logprobs")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val topLogprobs: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val temperature: Optional<Double> = Optional.Absent,
  @SerialName("top_p")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val topP: Optional<Double> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val user: String? = null,
  @SerialName("safety_identifier")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val safetyIdentifier: String? = null,
  @SerialName("prompt_cache_key")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val promptCacheKey: String? = null,
  @SerialName("service_tier")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val serviceTier: Optional<ServiceTier> = Optional.Absent,
  @SerialName("prompt_cache_retention")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val promptCacheRetention: Optional<PromptCacheRetention> = Optional.Absent,
  @SerialName("previous_response_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val previousResponseId: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val model: ModelIdsResponses? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val reasoning: Optional<Reasoning> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val background: Optional<Boolean> = Optional.Absent,
  @SerialName("max_output_tokens")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxOutputTokens: Optional<Long> = Optional.Absent,
  @SerialName("max_tool_calls")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxToolCalls: Optional<Long> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val text: ResponseTextParam? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val tools: ToolsArray? = null,
  @SerialName("tool_choice")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val toolChoice: ToolChoiceParam? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val prompt: Optional<Prompt> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val truncation: Optional<Truncation> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val input: InputParam? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val include: Optional<List<IncludeEnum>> = Optional.Absent,
  @SerialName("parallel_tool_calls")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val parallelToolCalls: Optional<Boolean> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val store: Optional<Boolean> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val instructions: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val stream: Optional<Boolean> = Optional.Absent,
  @SerialName("stream_options")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val streamOptions: Optional<ResponseStreamOptions> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val conversation: Optional<ConversationParam> = Optional.Absent,
  @SerialName("context_management")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val contextManagement: Optional<List<ContextManagementParam>> = Optional.Absent,
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
