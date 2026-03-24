package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class IncludeEnum(
  public val `value`: String,
) {
  @SerialName("file_search_call.results")
  FileSearchCallResults("file_search_call.results"),
  @SerialName("web_search_call.results")
  WebSearchCallResults("web_search_call.results"),
  @SerialName("web_search_call.action.sources")
  WebSearchCallActionSources("web_search_call.action.sources"),
  @SerialName("message.input_image.image_url")
  MessageInputImageImageUrl("message.input_image.image_url"),
  @SerialName("computer_call_output.output.image_url")
  ComputerCallOutputOutputImageUrl("computer_call_output.output.image_url"),
  @SerialName("code_interpreter_call.outputs")
  CodeInterpreterCallOutputs("code_interpreter_call.outputs"),
  @SerialName("reasoning.encrypted_content")
  ReasoningEncryptedContent("reasoning.encrypted_content"),
  @SerialName("message.output_text.logprobs")
  MessageOutputTextLogprobs("message.output_text.logprobs"),
  ;
}
