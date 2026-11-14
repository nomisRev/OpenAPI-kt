package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class V internal constructor(
  private val client: HttpClient,
) {
  public fun version(version: String): VersionPath = VersionPath(client, version)

  public class VersionPath internal constructor(
    private val client: HttpClient,
    private val version: String,
  ) {
    public val x: X = X(client, version)

    public class X internal constructor(
      private val client: HttpClient,
      private val version: String,
    ) {
      public val `get`: Get = Get(client, version)

      public class Get internal constructor(
        private val client: HttpClient,
        private val version: String,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("v${this.version.encodeURLParameter()}/x")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
