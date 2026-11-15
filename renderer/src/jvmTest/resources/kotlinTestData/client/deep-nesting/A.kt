package io.github.nomisrev.render.test.client.deep.nesting

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.Long
import kotlin.String

public class A internal constructor(
  private val __client: HttpClient,
) {
  public val b: B = B(__client)

  public class B internal constructor(
    private val __client: HttpClient,
  ) {
    public fun c(c: String): CPath = CPath(__client, c)

    public class CPath internal constructor(
      private val __client: HttpClient,
      private val _c: String,
    ) {
      public val d: D = D(__client, _c)

      public class D internal constructor(
        private val __client: HttpClient,
        private val _c: String,
      ) {
        public fun e(e: Long): EPath = EPath(__client, _c, e)

        public class EPath internal constructor(
          private val __client: HttpClient,
          private val _c: String,
          private val _e: Long,
        ) {
          public val f: F = F(__client, _c, _e)

          public class F internal constructor(
            private val __client: HttpClient,
            private val _c: String,
            private val _e: Long,
          ) {
            public val `get`: Get = Get(__client, _c, _e)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _c: String,
              private val _e: Long,
            ) {
              public suspend operator fun invoke() {
                val response = __client.get("a/b/${this._c.encodeURLParameter()}/d/${this._e}/f")
                if (response.status.value != 200) throw ResponseException(response, "")
              }
            }
          }
        }
      }
    }
  }
}
