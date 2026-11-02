package io.github.nomisrev.render.test.client.response.with.body

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.JsonConvertException
import kotlin.Int
import kotlin.String
import kotlinx.serialization.json.JsonPrimitive

public class Pets internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: String): Response {
      val response = client.post("/pets") {
        contentType(ContentType.Application.Json)
        setBody(JsonPrimitive(body))
      }
      return when (response.status.value) {
        201 -> Response.Created(response.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") })
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
