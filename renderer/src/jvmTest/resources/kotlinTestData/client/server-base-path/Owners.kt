package io.github.nomisrev.render.test.client.server.base.path

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Owners internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public fun id(id: String): IdPath = IdPath(client, id)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = client.get("owners")
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }

  public class IdPath internal constructor(
    private val client: HttpClient,
    private val id: String,
  ) {
    public val `get`: Get = Get(client, id)

    public class Get internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("owners/${this.id.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
