package io.github.nomisrev.render.test.client.media.type.range.uploads

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.ByteArray
import kotlin.Long

public class Thumbnails internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      size: Long? = null,
      body: ByteArray? = null,
      contentType: ContentType? = null,
    ) {
      val response = client.post("/thumbnails") {
        if (size != null) {
          url.encodedParameters.append("size", size.toString())
        }
        body?.let {
          require(contentType != null && contentType.match(ContentType("image", "*")) && contentType.contentSubtype != "*") {
            "contentType must be a media type in image/*, but was $contentType"
          }
          contentType(contentType)
          setBody(it)
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
