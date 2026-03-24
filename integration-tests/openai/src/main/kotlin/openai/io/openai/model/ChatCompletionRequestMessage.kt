package io.openai.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("role")
@Serializable
public sealed interface ChatCompletionRequestMessage {
  @Serializable
  @JvmInline
  @SerialName("ChatCompletionRequestDeveloperMessage")
  public value class ChatCompletionRequestDeveloperMessage(
    public val `value`: io.openai.model.ChatCompletionRequestDeveloperMessage,
  ) : ChatCompletionRequestMessage

  @Serializable
  @JvmInline
  @SerialName("ChatCompletionRequestSystemMessage")
  public value class ChatCompletionRequestSystemMessage(
    public val `value`: io.openai.model.ChatCompletionRequestSystemMessage,
  ) : ChatCompletionRequestMessage

  @Serializable
  @JvmInline
  @SerialName("ChatCompletionRequestUserMessage")
  public value class ChatCompletionRequestUserMessage(
    public val `value`: io.openai.model.ChatCompletionRequestUserMessage,
  ) : ChatCompletionRequestMessage

  @Serializable
  @JvmInline
  @SerialName("ChatCompletionRequestAssistantMessage")
  public value class ChatCompletionRequestAssistantMessage(
    public val `value`: io.openai.model.ChatCompletionRequestAssistantMessage,
  ) : ChatCompletionRequestMessage

  @Serializable
  @JvmInline
  @SerialName("ChatCompletionRequestToolMessage")
  public value class ChatCompletionRequestToolMessage(
    public val `value`: io.openai.model.ChatCompletionRequestToolMessage,
  ) : ChatCompletionRequestMessage

  @Serializable
  @JvmInline
  @SerialName("ChatCompletionRequestFunctionMessage")
  public value class ChatCompletionRequestFunctionMessage(
    public val `value`: io.openai.model.ChatCompletionRequestFunctionMessage,
  ) : ChatCompletionRequestMessage
}
