package io.github.nomisrev.render.test.client.dollar.and.keyword.paths

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Packages internal constructor(
  private val __client: HttpClient,
) {
  public fun `package`(`package`: String): PackagePath = PackagePath(__client, `package`)

  public class PackagePath internal constructor(
    private val __client: HttpClient,
    private val _package: String,
  ) {
    public val `in`: In = In(__client, _package)

    public class In internal constructor(
      private val __client: HttpClient,
      private val _package: String,
    ) {
      public fun `in`(`in`: String): InPath = InPath(__client, _package, `in`)

      public class InPath internal constructor(
        private val __client: HttpClient,
        private val _package: String,
        private val _in: String,
      ) {
        public val `get`: Get = Get(__client, _package, _in)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _package: String,
          private val _in: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.get("packages/${this._package.encodeURLParameter()}/in/${this._in.encodeURLParameter()}")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }
      }
    }
  }
}
