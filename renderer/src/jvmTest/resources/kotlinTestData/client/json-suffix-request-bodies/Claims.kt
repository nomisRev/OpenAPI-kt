package io.github.nomisrev.render.test.client.json.suffix.request.bodies

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

public class Claims internal constructor(
  private val __client: HttpClient,
) {
  public val patch: Patch = Patch(__client)

  public class Patch internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(body: ClaimPatch) {
      val response = __client.patch("claims") {
        contentType(ContentType("application", "merge-patch+json"))
        setBody(body)
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
