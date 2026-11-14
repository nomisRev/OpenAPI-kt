package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import io.openai.model.ChatCompletionDeleted
import io.openai.model.ChatCompletionList
import io.openai.model.ChatCompletionMessageList
import io.openai.model.CreateChatCompletionRequest
import io.openai.model.CreateChatCompletionResponse
import io.openai.model.CreateChatCompletionStreamResponse
import io.openai.model.Metadata
import kotlin.Long
import kotlin.String
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
      ): ChatCompletionList {
        val response = client.get("chat/completions") {
          if (model != null) {
            this.url.encodedParameters.append("model", model.encodeURLParameter())
          }
          if (metadata != null) {
            metadata.values.forEach { this.url.encodedParameters.append(it.key.encodeURLParameter(), it.value.encodeURLParameter()) }
          }
          if (after != null) {
            this.url.encodedParameters.append("after", after.encodeURLParameter())
          }
          if (limit != null) {
            this.url.encodedParameters.append("limit", limit.toString())
          }
          if (order != null) {
            this.url.encodedParameters.append("order", order.value.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }

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
      public suspend fun json(body: CreateChatCompletionRequest): CreateChatCompletionResponse {
        val response = client.post("chat/completions") {
          `header`(HttpHeaders.Accept, ContentType.Application.Json)
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }

      public suspend fun textEventStream(body: CreateChatCompletionRequest): CreateChatCompletionStreamResponse {
        val response = client.post("chat/completions") {
          `header`(HttpHeaders.Accept, ContentType("text", "event-stream"))
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
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
        public suspend operator fun invoke(): ChatCompletionDeleted {
          val response = client.delete("chat/completions/${this.completionId.encodeURLParameter()}")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val completionId: String,
      ) {
        public suspend operator fun invoke(): CreateChatCompletionResponse {
          val response = client.get("chat/completions/${this.completionId.encodeURLParameter()}")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val completionId: String,
      ) {
        public suspend operator fun invoke(metadata: Metadata?): CreateChatCompletionResponse {
          val response = client.post("chat/completions/${this.completionId.encodeURLParameter()}") {
            contentType(ContentType.Application.Json)
            setBody(Body(metadata = metadata))
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        @Serializable
        internal data class Body(
          public val metadata: Metadata?,
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
          ): ChatCompletionMessageList {
            val response = client.get("chat/completions/${this.completionId.encodeURLParameter()}/messages") {
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
              if (limit != null) {
                this.url.encodedParameters.append("limit", limit.toString())
              }
              if (order != null) {
                this.url.encodedParameters.append("order", order.value.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }

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
