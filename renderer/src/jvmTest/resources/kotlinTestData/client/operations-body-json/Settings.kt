package io.github.nomisrev.render.test.client.operations.body.json

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

public class Settings internal constructor(
  private val __client: HttpClient,
) {
  public val patch: Patch = Patch(__client)

  public class Patch internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(body: UpdateSettingsRequest? = null) {
      val response = __client.patch("settings") {
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
    }
  }
}
