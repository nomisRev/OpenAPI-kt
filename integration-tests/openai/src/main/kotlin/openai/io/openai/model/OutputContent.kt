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
public sealed interface OutputContent {
  @Serializable
  @JvmInline
  @SerialName("OutputTextContent")
  public value class OutputTextContent(
    public val `value`: io.openai.model.OutputTextContent,
  ) : OutputContent

  @Serializable
  @JvmInline
  @SerialName("RefusalContent")
  public value class RefusalContent(
    public val `value`: io.openai.model.RefusalContent,
  ) : OutputContent

  @Serializable
  @JvmInline
  @SerialName("ReasoningTextContent")
  public value class ReasoningTextContent(
    public val `value`: io.openai.model.ReasoningTextContent,
  ) : OutputContent
}
