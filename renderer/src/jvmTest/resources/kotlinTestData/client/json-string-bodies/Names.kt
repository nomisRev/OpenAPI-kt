package io.github.nomisrev.render.test.client.json.string.bodies

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.JsonConvertException
import kotlin.String
import kotlinx.serialization.json.JsonPrimitive

public class Names internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public val put: Put = Put(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: String): String {
      val response = client.post("names") {
        contentType(ContentType.Application.Json)
        setBody(JsonPrimitive(body))
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") }
    }
  }

  public class Put internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: String): String {
      val response = client.put("names") {
        contentType(ContentType.Text.Plain)
        setBody(body)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
