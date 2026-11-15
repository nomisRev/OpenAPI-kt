package io.github.nomisrev.render.test.client.parameter.name.collisions

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.String
import kotlin.collections.List

public class Echo internal constructor(
  private val __client: HttpClient,
) {
  public val post: Post = Post(__client)

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(
      body: String,
      requestBody: List<String>,
      response: String? = null,
    ): String {
      val response = __client.post("echo") {
        this.url.encodedParameters.append("body", body.encodeURLParameter())
        if (response != null) {
          this.url.encodedParameters.append("response", response.encodeURLParameter())
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
