package io.github.nomisrev.render.test.client.literal.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Ops internal constructor(
  private val __client: HttpClient,
) {
  public val a: A = A(__client)

  public val underscore: Underscore = Underscore(__client)

  public class A internal constructor(
    private val __client: HttpClient,
  ) {
    public val b: B = B(__client)

    public class B internal constructor(
      private val __client: HttpClient,
    ) {
      public val c: C = C(__client)

      public class C internal constructor(
        private val __client: HttpClient,
      ) {
        public val `get`: Get = Get(__client)

        public class Get internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke() {
            val response = __client.get("ops/a:b:c")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }
      }
    }
  }

  public class Underscore internal constructor(
    private val __client: HttpClient,
  ) {
    public val x: X = X(__client)

    public class X internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("ops/_/x")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
