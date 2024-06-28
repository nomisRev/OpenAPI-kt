package io.github.nomisrev.example

import io.github.nomisrev.openai.ChatCompletionRequestMessage.CaseChatCompletionRequestUserMessage
import io.github.nomisrev.openai.ChatCompletionRequestUserMessage
import io.github.nomisrev.openai.ChatCompletionRequestUserMessage.Content.CaseString
import io.github.nomisrev.openai.CreateChatCompletionRequest
import io.github.nomisrev.openai.OpenAI
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

suspend fun main() {
  val ai = OpenAI(configuredClient())
  ai.chat.completions.createChatCompletion(
    CreateChatCompletionRequest(
      listOf(
        CaseChatCompletionRequestUserMessage(
          ChatCompletionRequestUserMessage(CaseString("Hello, how are you?"))
        )
      ),
      CreateChatCompletionRequest.Model.Gpt35Turbo
    )
  )
}

private fun configuredClient(): HttpClient = HttpClient {
  install(ContentNegotiation) {
    json(
      Json {
        ignoreUnknownKeys = true
        prettyPrint = false
        isLenient = true
        @Suppress("OPT_IN_USAGE")
        explicitNulls = false
        classDiscriminator = "_type_"
      }
    )
  }
  install(HttpTimeout) {
    requestTimeoutMillis = 45_000
    connectTimeoutMillis = 45_000
    socketTimeoutMillis = 45_000
  }
  install(HttpRequestRetry) {
    maxRetries = 5
    retryIf { _, response -> !response.status.isSuccess() }
    retryOnExceptionIf { _, _ -> true }
    delayMillis { retry -> retry * 1000L }
  }
}
