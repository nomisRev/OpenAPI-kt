package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonElement

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface ResponseStreamEvent {
  /**
   * Emitted when there is a partial audio response.
   */
  @SerialName("response.audio.delta")
  @Serializable
  public data class ResponseAudioDelta(
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
    public val delta: String,
  ) : ResponseStreamEvent

  /**
   * Emitted when the audio response is complete.
   */
  @JvmInline
  @SerialName("response.audio.done")
  @Serializable
  public value class ResponseAudioDone(
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when there is a partial transcript of audio.
   */
  @SerialName("response.audio.transcript.delta")
  @Serializable
  public data class ResponseAudioTranscriptDelta(
    public val delta: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when the full audio transcript is completed.
   */
  @JvmInline
  @SerialName("response.audio.transcript.done")
  @Serializable
  public value class ResponseAudioTranscriptDone(
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when a partial code snippet is streamed by the code interpreter.
   */
  @SerialName("response.code_interpreter_call_code.delta")
  @Serializable
  public data class ResponseCodeInterpreterCallCodeDelta(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
    public val delta: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when the code snippet is finalized by the code interpreter.
   */
  @SerialName("response.code_interpreter_call_code.done")
  @Serializable
  public data class ResponseCodeInterpreterCallCodeDone(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
    public val code: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when the code interpreter call is completed.
   */
  @SerialName("response.code_interpreter_call.completed")
  @Serializable
  public data class ResponseCodeInterpreterCallCompleted(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when a code interpreter call is in progress.
   */
  @SerialName("response.code_interpreter_call.in_progress")
  @Serializable
  public data class ResponseCodeInterpreterCallInProgress(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when the code interpreter is actively interpreting the code snippet.
   */
  @SerialName("response.code_interpreter_call.interpreting")
  @Serializable
  public data class ResponseCodeInterpreterCallInterpreting(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when the model response is complete.
   */
  @SerialName("response.completed")
  @Serializable
  public data class ResponseCompleted(
    public val response: Response,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when a new content part is added.
   */
  @SerialName("response.content_part.added")
  @Serializable
  public data class ResponseContentPartAdded(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("content_index")
    public val contentIndex: Long,
    public val part: OutputContent,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when a content part is done.
   */
  @SerialName("response.content_part.done")
  @Serializable
  public data class ResponseContentPartDone(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("content_index")
    public val contentIndex: Long,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
    public val part: OutputContent,
  ) : ResponseStreamEvent

  /**
   * An event that is emitted when a response is created.
   *
   */
  @SerialName("response.created")
  @Serializable
  public data class ResponseCreated(
    public val response: Response,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when an error occurs.
   */
  @SerialName("error")
  @Serializable
  public data class Error(
    public val code: String?,
    public val message: String,
    public val `param`: String?,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when a file search call is completed (results found).
   */
  @SerialName("response.file_search_call.completed")
  @Serializable
  public data class ResponseFileSearchCallCompleted(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when a file search call is initiated.
   */
  @SerialName("response.file_search_call.in_progress")
  @Serializable
  public data class ResponseFileSearchCallInProgress(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when a file search is currently searching.
   */
  @SerialName("response.file_search_call.searching")
  @Serializable
  public data class ResponseFileSearchCallSearching(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when there is a partial function-call arguments delta.
   */
  @SerialName("response.function_call_arguments.delta")
  @Serializable
  public data class ResponseFunctionCallArgumentsDelta(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
    public val delta: String,
  ) : ResponseStreamEvent

  /**
   * Emitted when function-call arguments are finalized.
   */
  @SerialName("response.function_call_arguments.done")
  @Serializable
  public data class ResponseFunctionCallArgumentsDone(
    @SerialName("item_id")
    public val itemId: String,
    public val name: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
    public val arguments: String,
  ) : ResponseStreamEvent

  /**
   * Emitted when the response is in progress.
   */
  @SerialName("response.in_progress")
  @Serializable
  public data class ResponseInProgress(
    public val response: Response,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * An event that is emitted when a response fails.
   *
   */
  @SerialName("response.failed")
  @Serializable
  public data class ResponseFailed(
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
    public val response: Response,
  ) : ResponseStreamEvent

  /**
   * An event that is emitted when a response finishes as incomplete.
   *
   */
  @SerialName("response.incomplete")
  @Serializable
  public data class ResponseIncomplete(
    public val response: Response,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when a new output item is added.
   */
  @SerialName("response.output_item.added")
  @Serializable
  public data class ResponseOutputItemAdded(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
    public val item: OutputItem,
  ) : ResponseStreamEvent

  /**
   * Emitted when an output item is marked done.
   */
  @SerialName("response.output_item.done")
  @Serializable
  public data class ResponseOutputItemDone(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
    public val item: OutputItem,
  ) : ResponseStreamEvent

  /**
   * Emitted when a new reasoning summary part is added.
   */
  @SerialName("response.reasoning_summary_part.added")
  @Serializable
  public data class ResponseReasoningSummaryPartAdded(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("summary_index")
    public val summaryIndex: Long,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
    public val part: Part,
  ) : ResponseStreamEvent {
    /**
     * The summary part that was added.
     *
     */
    @Serializable
    public data class Part(
      public val type: Type,
      public val text: String,
    ) {
      @Serializable
      public enum class Type(
        public val `value`: String,
      ) {
        @SerialName("summary_text")
        SummaryText("summary_text"),
        ;
      }
    }
  }

  /**
   * Emitted when a reasoning summary part is completed.
   */
  @SerialName("response.reasoning_summary_part.done")
  @Serializable
  public data class ResponseReasoningSummaryPartDone(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("summary_index")
    public val summaryIndex: Long,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
    public val part: Part,
  ) : ResponseStreamEvent {
    /**
     * The completed summary part.
     *
     */
    @Serializable
    public data class Part(
      public val type: Type,
      public val text: String,
    ) {
      @Serializable
      public enum class Type(
        public val `value`: String,
      ) {
        @SerialName("summary_text")
        SummaryText("summary_text"),
        ;
      }
    }
  }

  /**
   * Emitted when a delta is added to a reasoning summary text.
   */
  @SerialName("response.reasoning_summary_text.delta")
  @Serializable
  public data class ResponseReasoningSummaryTextDelta(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("summary_index")
    public val summaryIndex: Long,
    public val delta: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when a reasoning summary text is completed.
   */
  @SerialName("response.reasoning_summary_text.done")
  @Serializable
  public data class ResponseReasoningSummaryTextDone(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("summary_index")
    public val summaryIndex: Long,
    public val text: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when a delta is added to a reasoning text.
   */
  @SerialName("response.reasoning_text.delta")
  @Serializable
  public data class ResponseReasoningTextDelta(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("content_index")
    public val contentIndex: Long,
    public val delta: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when a reasoning text is completed.
   */
  @SerialName("response.reasoning_text.done")
  @Serializable
  public data class ResponseReasoningTextDone(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("content_index")
    public val contentIndex: Long,
    public val text: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when there is a partial refusal text.
   */
  @SerialName("response.refusal.delta")
  @Serializable
  public data class ResponseRefusalDelta(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("content_index")
    public val contentIndex: Long,
    public val delta: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when refusal text is finalized.
   */
  @SerialName("response.refusal.done")
  @Serializable
  public data class ResponseRefusalDone(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("content_index")
    public val contentIndex: Long,
    public val refusal: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when there is an additional text delta.
   */
  @SerialName("response.output_text.delta")
  @Serializable
  public data class ResponseOutputTextDelta(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("content_index")
    public val contentIndex: Long,
    public val delta: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
    public val logprobs: List<ResponseLogProb>,
  ) : ResponseStreamEvent

  /**
   * Emitted when text content is finalized.
   */
  @SerialName("response.output_text.done")
  @Serializable
  public data class ResponseOutputTextDone(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("content_index")
    public val contentIndex: Long,
    public val text: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
    public val logprobs: List<ResponseLogProb>,
  ) : ResponseStreamEvent

  /**
   * Emitted when a web search call is completed.
   */
  @SerialName("response.web_search_call.completed")
  @Serializable
  public data class ResponseWebSearchCallCompleted(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when a web search call is initiated.
   */
  @SerialName("response.web_search_call.in_progress")
  @Serializable
  public data class ResponseWebSearchCallInProgress(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when a web search call is executing.
   */
  @SerialName("response.web_search_call.searching")
  @Serializable
  public data class ResponseWebSearchCallSearching(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when an image generation tool call has completed and the final image is available.
   *
   */
  @SerialName("response.image_generation_call.completed")
  @Serializable
  public data class ResponseImageGenerationCallCompleted(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
    @SerialName("item_id")
    public val itemId: String,
  ) : ResponseStreamEvent

  /**
   * Emitted when an image generation tool call is actively generating an image (intermediate state).
   *
   */
  @SerialName("response.image_generation_call.generating")
  @Serializable
  public data class ResponseImageGenerationCallGenerating(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when an image generation tool call is in progress.
   *
   */
  @SerialName("response.image_generation_call.in_progress")
  @Serializable
  public data class ResponseImageGenerationCallInProgress(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when a partial image is available during image generation streaming.
   *
   */
  @SerialName("response.image_generation_call.partial_image")
  @Serializable
  public data class ResponseImageGenerationCallPartialImage(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
    @SerialName("partial_image_index")
    public val partialImageIndex: Long,
    @SerialName("partial_image_b64")
    public val partialImageB64: String,
  ) : ResponseStreamEvent

  /**
   * Emitted when there is a delta (partial update) to the arguments of an MCP tool call.
   *
   */
  @SerialName("response.mcp_call_arguments.delta")
  @Serializable
  public data class ResponseMcpCallArgumentsDelta(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
    public val delta: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when the arguments for an MCP tool call are finalized.
   *
   */
  @SerialName("response.mcp_call_arguments.done")
  @Serializable
  public data class ResponseMcpCallArgumentsDone(
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
    public val arguments: String,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when an MCP  tool call has completed successfully.
   *
   */
  @SerialName("response.mcp_call.completed")
  @Serializable
  public data class ResponseMcpCallCompleted(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when an MCP  tool call has failed.
   *
   */
  @SerialName("response.mcp_call.failed")
  @Serializable
  public data class ResponseMcpCallFailed(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when an MCP  tool call is in progress.
   *
   */
  @SerialName("response.mcp_call.in_progress")
  @Serializable
  public data class ResponseMcpCallInProgress(
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
  ) : ResponseStreamEvent

  /**
   * Emitted when the list of available MCP tools has been successfully retrieved.
   *
   */
  @SerialName("response.mcp_list_tools.completed")
  @Serializable
  public data class ResponseMcpListToolsCompleted(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when the attempt to list available MCP tools has failed.
   *
   */
  @SerialName("response.mcp_list_tools.failed")
  @Serializable
  public data class ResponseMcpListToolsFailed(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when the system is in the process of retrieving the list of available MCP tools.
   *
   */
  @SerialName("response.mcp_list_tools.in_progress")
  @Serializable
  public data class ResponseMcpListToolsInProgress(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Emitted when an annotation is added to output text content.
   *
   */
  @SerialName("response.output_text.annotation.added")
  @Serializable
  public data class ResponseOutputTextAnnotationAdded(
    @SerialName("item_id")
    public val itemId: String,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("content_index")
    public val contentIndex: Long,
    @SerialName("annotation_index")
    public val annotationIndex: Long,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
    public val `annotation`: JsonElement,
  ) : ResponseStreamEvent

  /**
   * Emitted when a response is queued and waiting to be processed.
   *
   */
  @SerialName("response.queued")
  @Serializable
  public data class ResponseQueued(
    public val response: Response,
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
  ) : ResponseStreamEvent

  /**
   * Event representing a delta (partial update) to the input of a custom tool call.
   *
   */
  @SerialName("response.custom_tool_call_input.delta")
  @Serializable
  public data class ResponseCustomToolCallInputDelta(
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
    public val delta: String,
  ) : ResponseStreamEvent

  /**
   * Event indicating that input for a custom tool call is complete.
   *
   */
  @SerialName("response.custom_tool_call_input.done")
  @Serializable
  public data class ResponseCustomToolCallInputDone(
    @SerialName("sequence_number")
    public val sequenceNumber: Long,
    @SerialName("output_index")
    public val outputIndex: Long,
    @SerialName("item_id")
    public val itemId: String,
    public val input: String,
  ) : ResponseStreamEvent
}
