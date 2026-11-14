package io.github.nomisrev.render.test.client.parameter.name.collisions

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.cookie
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Things internal constructor(
  private val client: HttpClient,
) {
  public fun id(id: String): IdPath = IdPath(client, id)

  public class IdPath internal constructor(
    private val client: HttpClient,
    private val id: String,
  ) {
    public val `get`: Get = Get(client, id)

    public class Get internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke(
        idQuery: String,
        idHeader: String? = null,
        idCookie: String? = null,
      ) {
        val response = client.get("things/${this.id.encodeURLParameter()}") {
          this.url.encodedParameters.append("id", idQuery.encodeURLParameter())
          if (idHeader != null) {
            `header`("id", idHeader)
          }
          if (idCookie != null) {
            cookie("id", idCookie)
          }
        }
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
