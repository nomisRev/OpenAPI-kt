package io.github.nomisrev.render.test.client.deep.nesting

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import kotlin.Long
import kotlin.String

public class A internal constructor(
  private val client: HttpClient,
) {
  public val b: B = B(client)

  public class B internal constructor(
    private val client: HttpClient,
  ) {
    public fun c(c: String): CPath = CPath(client, c)

    public class CPath internal constructor(
      private val client: HttpClient,
      private val c: String,
    ) {
      public val d: D = D(client, c)

      public class D internal constructor(
        private val client: HttpClient,
        private val c: String,
      ) {
        public fun e(e: Long): EPath = EPath(client, c, e)

        public class EPath internal constructor(
          private val client: HttpClient,
          private val c: String,
          private val e: Long,
        ) {
          public val f: F = F(client, c, e)

          public class F internal constructor(
            private val client: HttpClient,
            private val c: String,
            private val e: Long,
          ) {
            public val `get`: Get = Get(client, c, e)

            public class Get internal constructor(
              private val client: HttpClient,
              private val c: String,
              private val e: Long,
            ) {
              public suspend operator fun invoke() {
                client.get("/a/b/$c/d/$e/f")
              }
            }
          }
        }
      }
    }
  }
}
