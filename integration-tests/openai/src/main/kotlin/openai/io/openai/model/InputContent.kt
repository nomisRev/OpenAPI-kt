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
public sealed interface InputContent {
  @Serializable
  @JvmInline
  @SerialName("InputTextContent")
  public value class InputTextContent(
    public val `value`: io.openai.model.InputTextContent,
  ) : InputContent

  @Serializable
  @JvmInline
  @SerialName("InputImageContent")
  public value class InputImageContent(
    public val `value`: io.openai.model.InputImageContent,
  ) : InputContent

  @Serializable
  @JvmInline
  @SerialName("InputFileContent")
  public value class InputFileContent(
    public val `value`: io.openai.model.InputFileContent,
  ) : InputContent
}
