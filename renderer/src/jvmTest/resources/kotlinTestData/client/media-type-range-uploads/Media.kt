package io.github.nomisrev.render.test.client.media.type.range.uploads

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.ByteArray

public class Media internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: ByteArray, contentType: ContentType) {
      val response = client.post("media") {
        require((contentType.match(ContentType("image", "*")) || contentType.match(ContentType("video", "*"))) && contentType.contentSubtype != "*") {
          "contentType must be a media type in image/* or video/*, but was $contentType"
        }
        contentType(contentType)
        setBody(body)
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
