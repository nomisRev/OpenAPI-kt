package io.openai.model

import kotlin.OptIn
import kotlin.String
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
  @SerialName("text")
  @Serializable
  public data class Text(
    public val text: String,
  ) : ChatCompletionRequestAssistantMessageContentPart

  @SerialName("refusal")
  @Serializable
  public data class Refusal(
    public val refusal: String,
  ) : ChatCompletionRequestAssistantMessageContentPart
}
