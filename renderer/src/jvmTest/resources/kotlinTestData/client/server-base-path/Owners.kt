package io.github.nomisrev.render.test.client.server.base.path

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Owners internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public fun id(id: String): IdPath = IdPath(__client, id)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = __client.get("owners")
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }

  public class IdPath internal constructor(
    private val __client: HttpClient,
    private val _id: String,
  ) {
    public val `get`: Get = Get(__client, _id)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("owners/${this._id.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
