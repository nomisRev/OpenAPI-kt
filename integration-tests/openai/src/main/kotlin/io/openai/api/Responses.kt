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
import io.openai.model.CompactResource
import io.openai.model.CompactResponseMethodPublicBody
import io.openai.model.CreateResponse
import io.openai.model.Error
import io.openai.model.IncludeEnum
import io.openai.model.Response
import io.openai.model.ResponseItemList
import io.openai.model.ResponseStreamEvent
import io.openai.model.TokenCountsBody
import io.openai.model.TokenCountsResource
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Responses internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public val inputTokens: InputTokens = InputTokens(client)

  public val compact: Compact = Compact(client)

  public fun responseId(responseId: String): ResponseIdPath = ResponseIdPath(client, responseId)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend fun json(body: CreateResponse): Response {
      val response = client.post("responses") {
        `header`(HttpHeaders.Accept, ContentType.Application.Json)
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    public suspend fun textEventStream(body: CreateResponse): ResponseStreamEvent {
      val response = client.post("responses") {
        `header`(HttpHeaders.Accept, ContentType("text", "event-stream"))
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class ResponseIdPath internal constructor(
    private val client: HttpClient,
    private val responseId: String,
  ) {
    public val delete: Delete = Delete(client, responseId)

    public val `get`: Get = Get(client, responseId)

    public val cancel: Cancel = Cancel(client, responseId)

    public val inputItems: InputItems = InputItems(client, responseId)

    public class Delete internal constructor(
      private val client: HttpClient,
      private val responseId: String,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.delete("responses/${this.responseId.encodeURLParameter()}")
        return when (response.status.value) {
          200 -> Response.Ok
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data object Ok : Response

        public data class NotFound(
          public val `value`: Error,
        ) : Response
      }
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val responseId: String,
    ) {
      public suspend operator fun invoke(
        include: List<IncludeEnum>? = null,
        stream: Boolean? = null,
        startingAfter: Long? = null,
        includeObfuscation: Boolean? = null,
      ): Response {
        val response = client.get("responses/${this.responseId.encodeURLParameter()}") {
          include?.forEach { this.url.encodedParameters.append("include", it.value.encodeURLParameter()) }
          if (stream != null) {
            this.url.encodedParameters.append("stream", stream.toString())
          }
          if (startingAfter != null) {
            this.url.encodedParameters.append("starting_after", startingAfter.toString())
          }
          if (includeObfuscation != null) {
            this.url.encodedParameters.append("include_obfuscation", includeObfuscation.toString())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Cancel internal constructor(
      private val client: HttpClient,
      private val responseId: String,
    ) {
      public val post: Post = Post(client, responseId)

      public class Post internal constructor(
        private val client: HttpClient,
        private val responseId: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.post("responses/${this.responseId.encodeURLParameter()}/cancel")
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: io.openai.model.Response,
          ) : Response

          public data class NotFound(
            public val `value`: Error,
          ) : Response
        }
      }
    }

    public class InputItems internal constructor(
      private val client: HttpClient,
      private val responseId: String,
    ) {
      public val `get`: Get = Get(client, responseId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val responseId: String,
      ) {
        public suspend operator fun invoke(
          limit: Long? = 20L,
          order: Order? = null,
          after: String? = null,
          include: List<IncludeEnum>? = null,
        ): ResponseItemList {
          val response = client.get("responses/${this.responseId.encodeURLParameter()}/input_items") {
            if (limit != null) {
              this.url.encodedParameters.append("limit", limit.toString())
            }
            if (order != null) {
              this.url.encodedParameters.append("order", order.value.encodeURLParameter())
            }
            if (after != null) {
              this.url.encodedParameters.append("after", after.encodeURLParameter())
            }
            include?.forEach { this.url.encodedParameters.append("include", it.value.encodeURLParameter()) }
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

  public class InputTokens internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: TokenCountsBody? = null): TokenCountsResource {
        val response = client.post("responses/input_tokens") {
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }

  public class Compact internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: CompactResponseMethodPublicBody? = null): CompactResource {
        val response = client.post("responses/compact") {
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }
}
