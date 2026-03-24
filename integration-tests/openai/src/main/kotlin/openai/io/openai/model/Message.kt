package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * A message to or from the model.
 */
@Serializable
public data class Message(
  @Required
  public val type: Type = Type.Message,
  public val id: String,
  public val status: MessageStatus,
  public val role: MessageRole,
  public val content: List<Content>,
) {
  /**
   * A content part that makes up an input or output item.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Content {
    @Serializable
    @JvmInline
    @SerialName("InputTextContent")
    public value class InputTextContent(
      public val `value`: io.openai.model.InputTextContent,
    ) : Content

    @Serializable
    @JvmInline
    @SerialName("OutputTextContent")
    public value class OutputTextContent(
      public val `value`: io.openai.model.OutputTextContent,
    ) : Content

    @Serializable
    @JvmInline
    @SerialName("TextContent")
    public value class TextContent(
      public val `value`: io.openai.model.TextContent,
    ) : Content

    @Serializable
    @JvmInline
    @SerialName("SummaryTextContent")
    public value class SummaryTextContent(
      public val `value`: io.openai.model.SummaryTextContent,
    ) : Content

    @Serializable
    @JvmInline
    @SerialName("ReasoningTextContent")
    public value class ReasoningTextContent(
      public val `value`: io.openai.model.ReasoningTextContent,
    ) : Content

    @Serializable
    @JvmInline
    @SerialName("RefusalContent")
    public value class RefusalContent(
      public val `value`: io.openai.model.RefusalContent,
    ) : Content

    @Serializable
    @JvmInline
    @SerialName("InputImageContent")
    public value class InputImageContent(
      public val `value`: io.openai.model.InputImageContent,
    ) : Content

    @Serializable
    @JvmInline
    @SerialName("ComputerScreenshotContent")
    public value class ComputerScreenshotContent(
      public val `value`: io.openai.model.ComputerScreenshotContent,
    ) : Content

    @Serializable
    @JvmInline
    @SerialName("InputFileContent")
    public value class InputFileContent(
      public val `value`: io.openai.model.InputFileContent,
    ) : Content
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("message")
    Message("message"),
    ;
  }
}
