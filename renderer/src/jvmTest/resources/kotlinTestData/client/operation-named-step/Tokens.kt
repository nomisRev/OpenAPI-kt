package io.github.nomisrev.render.test.client.operation.named.step

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Tokens internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public fun uuid(uuid: String): UuidPath = UuidPath(__client, uuid)

    public suspend operator fun invoke() {
      val response = __client.get("tokens")
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public class UuidPath internal constructor(
      private val __client: HttpClient,
      private val _uuid: String,
    ) {
      public val `get`: Get = Get(__client, _uuid)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _uuid: String,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("tokens/get/${this._uuid.encodeURLParameter()}")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
