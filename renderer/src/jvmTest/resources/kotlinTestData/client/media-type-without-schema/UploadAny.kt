package io.github.nomisrev.render.test.client.media.type.without.schema

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.utils.EmptyContent
import io.ktor.http.ContentType
import io.ktor.http.content.OutgoingContent
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.String
import kotlin.Unit

public class UploadAny internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      block: String? = null,
      contentType: ContentType? = null,
      requestBlock: HttpRequestBuilder.() -> Unit = {},
    ) {
      val response = client.post("upload-any") {
        if (block != null) {
          this.url.encodedParameters.append("block", block.encodeURLParameter())
        }
        if (contentType != null) {
          require((contentType.match(ContentType.Application.Pdf) || contentType.match(ContentType.Image.PNG)) && contentType.contentSubtype != "*") {
            "contentType must be a media type in application/pdf or image/png, but was $contentType"
          }
        }
        requestBlock()
        if (contentType != null && this.body !is EmptyContent && this.contentType() == null && (this.body as? OutgoingContent)?.contentType == null) {
          this.contentType(contentType)
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
