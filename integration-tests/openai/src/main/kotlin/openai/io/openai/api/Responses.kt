package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.delete
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.openai.model.CompactResource
import io.openai.model.CompactResponseMethodPublicBody
import io.openai.model.CreateResponse
import io.openai.model.Error
import io.openai.model.IncludeEnum
import io.openai.model.Response
import io.openai.model.ResponseStreamEvent
import io.openai.model.TokenCountsBody
import io.openai.model.TokenCountsResource
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List

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
    public suspend fun json(body: CreateResponse): Response = client.post("/responses") {
      `header`(HttpHeaders.Accept, ContentType.Application.Json)
      contentType(ContentType.Application.Json)
      setBody(body)
    }.body()

    public suspend fun textEventStream(body: CreateResponse): ResponseStreamEvent = client.post("/responses") {
      `header`(HttpHeaders.Accept, ContentType("text", "event-stream"))
      contentType(ContentType.Application.Json)
      setBody(body)
    }.body()

    public sealed interface JsonResponse {
      public data class Ok(
        public val `value`: Response,
      ) : JsonResponse
    }

    public sealed interface TextEventStreamResponse {
      public data class Ok(
        public val `value`: ResponseStreamEvent,
      ) : TextEventStreamResponse
    }
  }

  public class ResponseIdPath internal constructor(
    private val client: HttpClient,
    private val responseId: String,
  ) {
    public val delete: Delete = Delete(client, responseId)

    public val `get`: Get = Get(client, responseId)

    public val cancel: Cancel = Cancel(client, responseId)

    public class Delete internal constructor(
      private val client: HttpClient,
      private val responseId: String,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.delete("/responses/$responseId")
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
      ): Response = client.get("/responses/$responseId") {
        include?.let { parameter("include", it) }
        stream?.let { parameter("stream", it) }
        startingAfter?.let { parameter("starting_after", it) }
        includeObfuscation?.let { parameter("include_obfuscation", it) }
      }.body()
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
          val response = client.post("/responses/$responseId/cancel")
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
  }

  public class InputTokens internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: TokenCountsBody? = null): TokenCountsResource = client.post("/responses/input_tokens") {
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }.body()
    }
  }

  public class Compact internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: CompactResponseMethodPublicBody? = null): CompactResource = client.post("/responses/compact") {
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }.body()
    }
  }
}
