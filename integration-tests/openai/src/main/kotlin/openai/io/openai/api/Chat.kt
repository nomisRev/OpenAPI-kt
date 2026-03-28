package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.delete
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.openai.model.ChatCompletionDeleted
import io.openai.model.ChatCompletionList
import io.openai.model.ChatCompletionMessageList
import io.openai.model.CreateChatCompletionRequest
import io.openai.model.CreateChatCompletionResponse
import io.openai.model.CreateChatCompletionStreamResponse
import io.openai.model.Metadata
import kotlin.Long
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Chat internal constructor(
  private val client: HttpClient,
) {
  public val completions: Completions = Completions(client)

  public class Completions internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public fun completionId(completionId: String): CompletionIdPath = CompletionIdPath(client, completionId)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        model: String? = null,
        metadata: Metadata? = null,
        after: String? = null,
        limit: Long? = 20L,
        order: Order? = Order.Asc,
      ): ChatCompletionList = client.get("/chat/completions") {
        model?.let { parameter("model", it) }
        metadata?.let { parameter("metadata", it) }
        after?.let { parameter("after", it) }
        limit?.let { parameter("limit", it) }
        order?.let { parameter("order", it.value) }
      }.body()

      @Serializable
      public enum class Order(
        public val `value`: String,
      ) {
        @SerialName("asc")
        Asc("asc"),
        @SerialName("desc")
        Desc("desc"),
        ;
      }
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend fun json(body: CreateChatCompletionRequest): CreateChatCompletionResponse = client.post("/chat/completions") {
        `header`(HttpHeaders.Accept, ContentType.Application.Json)
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()

      public suspend fun textEventStream(body: CreateChatCompletionRequest): CreateChatCompletionStreamResponse = client.post("/chat/completions") {
        `header`(HttpHeaders.Accept, ContentType("text", "event-stream"))
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()
    }

    public class CompletionIdPath internal constructor(
      private val client: HttpClient,
      private val completionId: String,
    ) {
      public val delete: Delete = Delete(client, completionId)

      public val `get`: Get = Get(client, completionId)

      public val post: Post = Post(client, completionId)

      public val messages: Messages = Messages(client, completionId)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val completionId: String,
      ) {
        public suspend operator fun invoke(): ChatCompletionDeleted = client.delete("/chat/completions/$completionId").body()
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val completionId: String,
      ) {
        public suspend operator fun invoke(): CreateChatCompletionResponse = client.get("/chat/completions/$completionId").body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val completionId: String,
      ) {
        public suspend operator fun invoke(metadata: Metadata): CreateChatCompletionResponse = client.post("/chat/completions/$completionId") {
          contentType(ContentType.Application.Json)
          setBody(Body(metadata = metadata))
        }.body()

        @JvmInline
        @Serializable
        internal value class Body(
          public val metadata: Metadata,
        )
      }

      public class Messages internal constructor(
        private val client: HttpClient,
        private val completionId: String,
      ) {
        public val `get`: Get = Get(client, completionId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val completionId: String,
        ) {
          public suspend operator fun invoke(
            after: String? = null,
            limit: Long? = 20L,
            order: Order? = Order.Asc,
          ): ChatCompletionMessageList = client.get("/chat/completions/$completionId/messages") {
            after?.let { parameter("after", it) }
            limit?.let { parameter("limit", it) }
            order?.let { parameter("order", it.value) }
          }.body()

          @Serializable
          public enum class Order(
            public val `value`: String,
          ) {
            @SerialName("asc")
            Asc("asc"),
            @SerialName("desc")
            Desc("desc"),
            ;
          }
        }
      }
    }
  }
}
