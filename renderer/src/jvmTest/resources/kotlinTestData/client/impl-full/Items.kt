package io.github.nomisrev.render.test.client.`impl`.full

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public class Items internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(limit: Int? = null): Response {
      val value: List<String> = client.get("/items") {
        limit?.let { parameter("limit", it) }
      }.body()
      return Response(value)
    }

    public data class Response(
      public val `value`: List<String>,
    )
  }

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: String): Response {
      val response = client.post("/items") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      return when (response.status.value) {
        201 -> Response.Created(response.body())
        400 -> Response.BadRequest(response.body())
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface Response {
      public data class Created(
        public val `value`: String,
      ) : Response

      public data class BadRequest(
        public val `value`: Int,
      ) : Response
    }
  }
}
