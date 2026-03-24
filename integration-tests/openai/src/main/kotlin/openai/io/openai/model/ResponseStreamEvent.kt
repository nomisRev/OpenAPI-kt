package io.openai.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface ResponseStreamEvent {
  @Serializable
  @JvmInline
  @SerialName("ResponseAudioDeltaEvent")
  public value class ResponseAudioDeltaEvent(
    public val `value`: io.openai.model.ResponseAudioDeltaEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseAudioDoneEvent")
  public value class ResponseAudioDoneEvent(
    public val `value`: io.openai.model.ResponseAudioDoneEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseAudioTranscriptDeltaEvent")
  public value class ResponseAudioTranscriptDeltaEvent(
    public val `value`: io.openai.model.ResponseAudioTranscriptDeltaEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseAudioTranscriptDoneEvent")
  public value class ResponseAudioTranscriptDoneEvent(
    public val `value`: io.openai.model.ResponseAudioTranscriptDoneEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseCodeInterpreterCallCodeDeltaEvent")
  public value class ResponseCodeInterpreterCallCodeDeltaEvent(
    public val `value`: io.openai.model.ResponseCodeInterpreterCallCodeDeltaEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseCodeInterpreterCallCodeDoneEvent")
  public value class ResponseCodeInterpreterCallCodeDoneEvent(
    public val `value`: io.openai.model.ResponseCodeInterpreterCallCodeDoneEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseCodeInterpreterCallCompletedEvent")
  public value class ResponseCodeInterpreterCallCompletedEvent(
    public val `value`: io.openai.model.ResponseCodeInterpreterCallCompletedEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseCodeInterpreterCallInProgressEvent")
  public value class ResponseCodeInterpreterCallInProgressEvent(
    public val `value`: io.openai.model.ResponseCodeInterpreterCallInProgressEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseCodeInterpreterCallInterpretingEvent")
  public value class ResponseCodeInterpreterCallInterpretingEvent(
    public val `value`: io.openai.model.ResponseCodeInterpreterCallInterpretingEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseCompletedEvent")
  public value class ResponseCompletedEvent(
    public val `value`: io.openai.model.ResponseCompletedEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseContentPartAddedEvent")
  public value class ResponseContentPartAddedEvent(
    public val `value`: io.openai.model.ResponseContentPartAddedEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseContentPartDoneEvent")
  public value class ResponseContentPartDoneEvent(
    public val `value`: io.openai.model.ResponseContentPartDoneEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseCreatedEvent")
  public value class ResponseCreatedEvent(
    public val `value`: io.openai.model.ResponseCreatedEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseErrorEvent")
  public value class ResponseErrorEvent(
    public val `value`: io.openai.model.ResponseErrorEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseFileSearchCallCompletedEvent")
  public value class ResponseFileSearchCallCompletedEvent(
    public val `value`: io.openai.model.ResponseFileSearchCallCompletedEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseFileSearchCallInProgressEvent")
  public value class ResponseFileSearchCallInProgressEvent(
    public val `value`: io.openai.model.ResponseFileSearchCallInProgressEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseFileSearchCallSearchingEvent")
  public value class ResponseFileSearchCallSearchingEvent(
    public val `value`: io.openai.model.ResponseFileSearchCallSearchingEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseFunctionCallArgumentsDeltaEvent")
  public value class ResponseFunctionCallArgumentsDeltaEvent(
    public val `value`: io.openai.model.ResponseFunctionCallArgumentsDeltaEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseFunctionCallArgumentsDoneEvent")
  public value class ResponseFunctionCallArgumentsDoneEvent(
    public val `value`: io.openai.model.ResponseFunctionCallArgumentsDoneEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseInProgressEvent")
  public value class ResponseInProgressEvent(
    public val `value`: io.openai.model.ResponseInProgressEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseFailedEvent")
  public value class ResponseFailedEvent(
    public val `value`: io.openai.model.ResponseFailedEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseIncompleteEvent")
  public value class ResponseIncompleteEvent(
    public val `value`: io.openai.model.ResponseIncompleteEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseOutputItemAddedEvent")
  public value class ResponseOutputItemAddedEvent(
    public val `value`: io.openai.model.ResponseOutputItemAddedEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseOutputItemDoneEvent")
  public value class ResponseOutputItemDoneEvent(
    public val `value`: io.openai.model.ResponseOutputItemDoneEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseReasoningSummaryPartAddedEvent")
  public value class ResponseReasoningSummaryPartAddedEvent(
    public val `value`: io.openai.model.ResponseReasoningSummaryPartAddedEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseReasoningSummaryPartDoneEvent")
  public value class ResponseReasoningSummaryPartDoneEvent(
    public val `value`: io.openai.model.ResponseReasoningSummaryPartDoneEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseReasoningSummaryTextDeltaEvent")
  public value class ResponseReasoningSummaryTextDeltaEvent(
    public val `value`: io.openai.model.ResponseReasoningSummaryTextDeltaEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseReasoningSummaryTextDoneEvent")
  public value class ResponseReasoningSummaryTextDoneEvent(
    public val `value`: io.openai.model.ResponseReasoningSummaryTextDoneEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseReasoningTextDeltaEvent")
  public value class ResponseReasoningTextDeltaEvent(
    public val `value`: io.openai.model.ResponseReasoningTextDeltaEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseReasoningTextDoneEvent")
  public value class ResponseReasoningTextDoneEvent(
    public val `value`: io.openai.model.ResponseReasoningTextDoneEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseRefusalDeltaEvent")
  public value class ResponseRefusalDeltaEvent(
    public val `value`: io.openai.model.ResponseRefusalDeltaEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseRefusalDoneEvent")
  public value class ResponseRefusalDoneEvent(
    public val `value`: io.openai.model.ResponseRefusalDoneEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseTextDeltaEvent")
  public value class ResponseTextDeltaEvent(
    public val `value`: io.openai.model.ResponseTextDeltaEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseTextDoneEvent")
  public value class ResponseTextDoneEvent(
    public val `value`: io.openai.model.ResponseTextDoneEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseWebSearchCallCompletedEvent")
  public value class ResponseWebSearchCallCompletedEvent(
    public val `value`: io.openai.model.ResponseWebSearchCallCompletedEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseWebSearchCallInProgressEvent")
  public value class ResponseWebSearchCallInProgressEvent(
    public val `value`: io.openai.model.ResponseWebSearchCallInProgressEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseWebSearchCallSearchingEvent")
  public value class ResponseWebSearchCallSearchingEvent(
    public val `value`: io.openai.model.ResponseWebSearchCallSearchingEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseImageGenCallCompletedEvent")
  public value class ResponseImageGenCallCompletedEvent(
    public val `value`: io.openai.model.ResponseImageGenCallCompletedEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseImageGenCallGeneratingEvent")
  public value class ResponseImageGenCallGeneratingEvent(
    public val `value`: io.openai.model.ResponseImageGenCallGeneratingEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseImageGenCallInProgressEvent")
  public value class ResponseImageGenCallInProgressEvent(
    public val `value`: io.openai.model.ResponseImageGenCallInProgressEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseImageGenCallPartialImageEvent")
  public value class ResponseImageGenCallPartialImageEvent(
    public val `value`: io.openai.model.ResponseImageGenCallPartialImageEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseMCPCallArgumentsDeltaEvent")
  public value class ResponseMCPCallArgumentsDeltaEvent(
    public val `value`: io.openai.model.ResponseMCPCallArgumentsDeltaEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseMCPCallArgumentsDoneEvent")
  public value class ResponseMCPCallArgumentsDoneEvent(
    public val `value`: io.openai.model.ResponseMCPCallArgumentsDoneEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseMCPCallCompletedEvent")
  public value class ResponseMCPCallCompletedEvent(
    public val `value`: io.openai.model.ResponseMCPCallCompletedEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseMCPCallFailedEvent")
  public value class ResponseMCPCallFailedEvent(
    public val `value`: io.openai.model.ResponseMCPCallFailedEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseMCPCallInProgressEvent")
  public value class ResponseMCPCallInProgressEvent(
    public val `value`: io.openai.model.ResponseMCPCallInProgressEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseMCPListToolsCompletedEvent")
  public value class ResponseMCPListToolsCompletedEvent(
    public val `value`: io.openai.model.ResponseMCPListToolsCompletedEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseMCPListToolsFailedEvent")
  public value class ResponseMCPListToolsFailedEvent(
    public val `value`: io.openai.model.ResponseMCPListToolsFailedEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseMCPListToolsInProgressEvent")
  public value class ResponseMCPListToolsInProgressEvent(
    public val `value`: io.openai.model.ResponseMCPListToolsInProgressEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseOutputTextAnnotationAddedEvent")
  public value class ResponseOutputTextAnnotationAddedEvent(
    public val `value`: io.openai.model.ResponseOutputTextAnnotationAddedEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseQueuedEvent")
  public value class ResponseQueuedEvent(
    public val `value`: io.openai.model.ResponseQueuedEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseCustomToolCallInputDeltaEvent")
  public value class ResponseCustomToolCallInputDeltaEvent(
    public val `value`: io.openai.model.ResponseCustomToolCallInputDeltaEvent,
  ) : ResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ResponseCustomToolCallInputDoneEvent")
  public value class ResponseCustomToolCallInputDoneEvent(
    public val `value`: io.openai.model.ResponseCustomToolCallInputDoneEvent,
  ) : ResponseStreamEvent
}
