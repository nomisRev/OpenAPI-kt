package io.github.nomisrev.example

import io.github.nomisrev.openai.ChatCompletionRequestMessage.CaseChatCompletionRequestUserMessage
import io.github.nomisrev.openai.ChatCompletionRequestUserMessage
import io.github.nomisrev.openai.ChatCompletionRequestUserMessage.Content.CaseString
import io.github.nomisrev.openai.CreateChatCompletionRequest
import io.github.nomisrev.openai.OpenAI
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Password(val value: String)

suspend fun main() {
  println(Json.encodeToJsonElement(Password("secret")))
  println(Json.decodeFromString(Password.serializer(), "\"secret\""))
//  val ai = OpenAI(configuredClient("MY_API_KEY"))
//  ai.chat.completions.createChatCompletion(
//    CreateChatCompletionRequest(
//      listOf(
//        CaseChatCompletionRequestUserMessage(
//          ChatCompletionRequestUserMessage(CaseString("Hello, how are you?"))
//        )
//      ),
//      CreateChatCompletionRequest.Model.Gpt35Turbo
//    )
//  )
}

private fun configuredClient(
  token: String,
  org: String? = null
): HttpClient = HttpClient {
  defaultRequest {
    url("https://api.openai.com/v1/")
    org?.let { headers.append("OpenAI-Organization", it) }
    bearerAuth(token)
  }
  install(ContentNegotiation) {
    json(
      Json {
        ignoreUnknownKeys = true
        isLenient = true
        @Suppress("OPT_IN_USAGE")
        explicitNulls = false
      }
    )
  }
}
