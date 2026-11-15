package io.github.nomisrev.render.test.client.operations.basic

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String
import kotlinx.serialization.json.JsonPrimitive

public class Pets internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public val post: Post = Post(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = __client.get("pets")
      if (response.status.value != 200) throw ResponseException(response, "")
    }
  }

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(body: String) {
      val response = __client.post("pets") {
        contentType(ContentType.Application.Json)
        setBody(JsonPrimitive(body))
      }
      if (response.status.value != 200) throw ResponseException(response, "")
    }
  }
}
