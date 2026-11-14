package io.github.nomisrev.render.test.client.media.type.without.schema

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Unit

public class UploadOptional internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(block: HttpRequestBuilder.() -> Unit = {}) {
      val response = client.post("upload-optional") {
        contentType(ContentType.Application.Pdf)
        block()
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
