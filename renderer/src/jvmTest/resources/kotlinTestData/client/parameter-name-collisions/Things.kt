package io.github.nomisrev.render.test.client.parameter.name.collisions

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.cookie
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Things internal constructor(
  private val __client: HttpClient,
) {
  public fun id(id: String): IdPath = IdPath(__client, id)

  public class IdPath internal constructor(
    private val __client: HttpClient,
    private val _id: String,
  ) {
    public val `get`: Get = Get(__client, _id)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke(
        idQuery: String,
        idHeader: String? = null,
        idCookie: String? = null,
      ) {
        val response = __client.get("things/${this._id.encodeURLParameter()}") {
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
