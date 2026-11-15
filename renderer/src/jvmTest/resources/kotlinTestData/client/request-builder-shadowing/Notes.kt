package io.github.nomisrev.render.test.client.request.builder.shadowing

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Notes internal constructor(
  private val __client: HttpClient,
) {
  public val post: Post = Post(__client)

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(
      url: String,
      body: Note,
      setBody: String? = null,
      contentType: String? = null,
      accept: String? = null,
    ) {
      val response = __client.post("notes") {
        this.url.encodedParameters.append("url", url.encodeURLParameter())
        if (setBody != null) {
          this.url.encodedParameters.append("setBody", setBody.encodeURLParameter())
        }
        if (contentType != null) {
          this.url.encodedParameters.append("contentType", contentType.encodeURLParameter())
        }
        if (accept != null) {
          this.url.encodedParameters.append("accept", accept.encodeURLParameter())
        }
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
