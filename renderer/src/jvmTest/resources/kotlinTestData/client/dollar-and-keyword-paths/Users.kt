package io.github.nomisrev.render.test.client.dollar.and.keyword.paths

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Users internal constructor(
  private val client: HttpClient,
) {
  public fun id(id: String): IdPath = IdPath(client, id)

  public class IdPath internal constructor(
    private val client: HttpClient,
    private val id: String,
  ) {
    public val links: Links = Links(client, id)

    public class Links internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("users/${this.id.encodeURLParameter()}/\$links")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
