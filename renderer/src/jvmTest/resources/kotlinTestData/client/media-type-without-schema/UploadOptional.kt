package io.github.nomisrev.render.test.client.media.type.without.schema

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.utils.EmptyContent
import io.ktor.http.ContentType
import io.ktor.http.content.OutgoingContent
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
        block()
        if (this.body !is EmptyContent && this.contentType() == null && (this.body as? OutgoingContent)?.contentType == null) {
          this.contentType(ContentType.Application.Pdf)
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
