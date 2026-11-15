package io.github.nomisrev.render.test.client.json.suffix.request.bodies

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.collections.List

public class Operations internal constructor(
  private val __client: HttpClient,
) {
  public val patch: Patch = Patch(__client)

  public class Patch internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(body: List<PatchOperation>) {
      val response = __client.patch("operations") {
        contentType(ContentType("application", "json-patch+json"))
        setBody(body)
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
