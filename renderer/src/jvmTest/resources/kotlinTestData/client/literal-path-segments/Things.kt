package io.github.nomisrev.render.test.client.literal.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.post

public class Things internal constructor(
  private val __client: HttpClient,
) {
  public val batch: Batch = Batch(__client)

  public class Batch internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public val run: Run = Run(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("things/~batch")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }

    public class Run internal constructor(
      private val __client: HttpClient,
    ) {
      public val post: Post = Post(__client)

      public class Post internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = __client.post("things/~batch:run")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
