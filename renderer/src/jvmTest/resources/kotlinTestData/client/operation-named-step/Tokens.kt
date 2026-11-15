package io.github.nomisrev.render.test.client.operation.named.step

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Tokens internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public fun uuid(uuid: String): UuidPath = UuidPath(client, uuid)

    public suspend operator fun invoke() {
      val response = client.get("tokens")
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public class UuidPath internal constructor(
      private val client: HttpClient,
      private val uuid: String,
    ) {
      public val `get`: Get = Get(client, uuid)

      public class Get internal constructor(
        private val client: HttpClient,
        private val uuid: String,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("tokens/get/${this.uuid.encodeURLParameter()}")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
