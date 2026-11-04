package io.github.nomisrev.render.test.client.media.type.range.uploads

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.ByteArray

public class Avatars internal constructor(
  private val client: HttpClient,
) {
  public val put: Put = Put(client)

  public class Put internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: ByteArray, contentType: ContentType) {
      val response = client.put("avatars") {
        require(contentType.match(ContentType("image", "*")) && contentType.contentSubtype != "*") {
          "contentType must be a media type in image/*, but was $contentType"
        }
        contentType(contentType)
        setBody(body)
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
