package io.github.nomisrev.render.test.client.literal.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Ops internal constructor(
  private val client: HttpClient,
) {
  public val a: A = A(client)

  public val underscore: Underscore = Underscore(client)

  public class A internal constructor(
    private val client: HttpClient,
  ) {
    public val b: B = B(client)

    public class B internal constructor(
      private val client: HttpClient,
    ) {
      public val c: C = C(client)

      public class C internal constructor(
        private val client: HttpClient,
      ) {
        public val `get`: Get = Get(client)

        public class Get internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke() {
            val response = client.get("ops/a:b:c")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }
      }
    }
  }

  public class Underscore internal constructor(
    private val client: HttpClient,
  ) {
    public val x: X = X(client)

    public class X internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("ops/_/x")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
