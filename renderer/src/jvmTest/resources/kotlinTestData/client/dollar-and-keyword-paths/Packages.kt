package io.github.nomisrev.render.test.client.dollar.and.keyword.paths

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Packages internal constructor(
  private val client: HttpClient,
) {
  public fun `package`(`package`: String): PackagePath = PackagePath(client, `package`)

  public class PackagePath internal constructor(
    private val client: HttpClient,
    private val `package`: String,
  ) {
    public val `in`: In = In(client, `package`)

    public class In internal constructor(
      private val client: HttpClient,
      private val `package`: String,
    ) {
      public fun `in`(`in`: String): InPath = InPath(client, `package`, `in`)

      public class InPath internal constructor(
        private val client: HttpClient,
        private val `package`: String,
        private val `in`: String,
      ) {
        public val `get`: Get = Get(client, `package`, `in`)

        public class Get internal constructor(
          private val client: HttpClient,
          private val `package`: String,
          private val `in`: String,
        ) {
          public suspend operator fun invoke() {
            val response = client.get("packages/${this.`package`.encodeURLParameter()}/in/${this.`in`.encodeURLParameter()}")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }
      }
    }
  }
}
