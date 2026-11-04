package io.github.nomisrev.render.test.client.non.json.request.bodies

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
    public suspend operator fun invoke(body: ByteArray) {
      val response = client.put("avatars") {
        contentType(ContentType.Image.PNG)
        setBody(body)
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
