package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface OutputContent {
  /**
   * A text output from the model.
   */
  @SerialName("output_text")
  @Serializable
  public data class OutputText(
    public val text: String,
    public val annotations: List<Annotation>,
    public val logprobs: List<LogProb>,
  ) : OutputContent

  /**
   * A refusal from the model.
   */
  @JvmInline
  @SerialName("refusal")
  @Serializable
  public value class Refusal(
    public val refusal: String,
  ) : OutputContent

  /**
   * Reasoning text from the model.
   */
  @JvmInline
  @SerialName("reasoning_text")
  @Serializable
  public value class ReasoningText(
    public val text: String,
  ) : OutputContent
}
