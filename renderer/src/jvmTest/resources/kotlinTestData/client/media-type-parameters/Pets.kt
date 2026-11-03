package io.github.nomisrev.render.test.client.media.type.parameters

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

public class Pets internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: Pet): Pet {
      val response = client.post("/pets") {
        contentType(ContentType.Application.Json.withParameter("charset", "utf-8"))
        setBody(body)
      }
      if (response.status.value != 201) throw ResponseException(response, "")
      return response.body()
    }
  }
}
