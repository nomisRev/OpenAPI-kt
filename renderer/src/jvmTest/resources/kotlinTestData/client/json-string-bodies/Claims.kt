package io.github.nomisrev.render.test.client.json.string.bodies

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.JsonConvertException
import kotlin.String
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive

public class Claims internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: String? = null): Response {
      val response = client.post("claims") {
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(JsonPrimitive(it))
        }
      }
      return when (response.status.value) {
        201 -> Response.Created(response.body<JsonPrimitive>().let { if (it is JsonNull) null else if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") })
        409 -> Response.Conflict(response.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") })
        in 500..599 -> Response.Status5XX(response.status, response.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") })
        else -> Response.Default(response.status, response.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") })
      }
    }

    public sealed interface Response {
      public data class Created(
        public val `value`: String?,
      ) : Response

      public data class Conflict(
        public val `value`: String,
      ) : Response

      public data class Status5XX(
        public val status: HttpStatusCode,
        public val `value`: String?,
      ) : Response

      public data class Default(
        public val status: HttpStatusCode,
        public val `value`: String,
      ) : Response
    }
  }
}
