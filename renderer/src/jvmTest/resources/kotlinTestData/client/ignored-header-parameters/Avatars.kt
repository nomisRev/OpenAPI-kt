package io.github.nomisrev.render.test.client.ignored.`header`.parameters

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`header`
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.ByteArray
import kotlin.String

public class Avatars internal constructor(
  private val client: HttpClient,
) {
  public val put: Put = Put(client)

  public class Put internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      body: ByteArray,
      contentType: ContentType,
      xRequestId: String? = null,
    ) {
      val response = client.put("avatars") {
        if (xRequestId != null) {
          `header`("X-Request-Id", xRequestId)
        }
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
