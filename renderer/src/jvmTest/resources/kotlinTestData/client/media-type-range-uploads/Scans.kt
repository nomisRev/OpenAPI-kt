package io.github.nomisrev.render.test.client.media.type.range.uploads

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.ByteArray

public class Scans internal constructor(
  private val __client: HttpClient,
) {
  public val post: Post = Post(__client)

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(body: ByteArray, requestType: RequestType) {
      val response = __client.post("scans") {
        contentType(requestType.contentType)
        setBody(body)
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public suspend operator fun invoke(body: ByteArray, contentType: ContentType) {
      val response = __client.post("scans") {
        require(contentType.match(ContentType("image", "*")) && contentType.contentSubtype != "*") {
          "contentType must be a media type in image/*, but was $contentType"
        }
        contentType(contentType)
        setBody(body)
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public enum class RequestType(
      public val contentType: ContentType,
    ) {
      OctetStream(ContentType.Application.OctetStream),
      Pdf(ContentType.Application.Pdf),
      ;
    }
  }
}
