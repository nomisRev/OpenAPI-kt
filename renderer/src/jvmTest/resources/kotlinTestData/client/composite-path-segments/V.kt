package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class V internal constructor(
  private val __client: HttpClient,
) {
  public fun version(version: String): VersionPath = VersionPath(__client, version)

  public class VersionPath internal constructor(
    private val __client: HttpClient,
    private val _version: String,
  ) {
    public val x: X = X(__client, _version)

    public class X internal constructor(
      private val __client: HttpClient,
      private val _version: String,
    ) {
      public val `get`: Get = Get(__client, _version)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _version: String,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("v${this._version.encodeURLParameter()}/x")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
