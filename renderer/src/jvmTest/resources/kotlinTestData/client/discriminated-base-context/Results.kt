package io.github.nomisrev.render.test.client.discriminated.base.context

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

public class Results internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public val post: Post = Post(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): ResultRead {
      val response = __client.get("results")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(body: ResultWrite) {
      val response = __client.post("results") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
