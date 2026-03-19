package io.github.nomisrev.render.test.client.operations.basic

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String

public class Pets internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      client.get("/pets")
    }
  }

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: String) {
      client.post("/pets") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
    }
  }
}
