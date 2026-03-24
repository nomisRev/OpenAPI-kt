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
public sealed interface ChatCompletionRequestAssistantMessageContentPart {
  @Serializable
  @JvmInline
  @SerialName("ChatCompletionRequestMessageContentPartText")
  public value class ChatCompletionRequestMessageContentPartText(
    public val `value`: io.openai.model.ChatCompletionRequestMessageContentPartText,
  ) : ChatCompletionRequestAssistantMessageContentPart

  @Serializable
  @JvmInline
  @SerialName("ChatCompletionRequestMessageContentPartRefusal")
  public value class ChatCompletionRequestMessageContentPartRefusal(
    public val `value`: io.openai.model.ChatCompletionRequestMessageContentPartRefusal,
  ) : ChatCompletionRequestAssistantMessageContentPart
}
