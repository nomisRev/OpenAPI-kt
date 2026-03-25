package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface ChatCompletionRequestAssistantMessageContentPart {
  /**
   * Learn about [text inputs](/docs/guides/text-generation).
   *
   */
  @JvmInline
  @SerialName("text")
  @Serializable
  public value class Text(
    public val text: String,
  ) : ChatCompletionRequestAssistantMessageContentPart

  @JvmInline
  @SerialName("refusal")
  @Serializable
  public value class Refusal(
    public val refusal: String,
  ) : ChatCompletionRequestAssistantMessageContentPart
}
