package io.github.nomisrev.render.test.client.media.type.range.uploads

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.ByteArray
import kotlin.String

public class Fonts internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      contentType: String,
      body: ByteArray,
      bodyContentType: ContentType,
    ) {
      val response = client.post("/fonts") {
        url.encodedParameters.append("contentType", contentType.encodeURLParameter())
        require(bodyContentType.match(ContentType("font", "*")) && bodyContentType.contentSubtype != "*") {
          "bodyContentType must be a media type in font/*, but was $bodyContentType"
        }
        contentType(bodyContentType)
        setBody(body)
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
