package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.http.encodeURLParameter
import kotlin.String

public class V1 internal constructor(
  private val client: HttpClient,
) {
  public fun name(name: String): NamePath = NamePath(client, name)

  public class NamePath internal constructor(
    private val client: HttpClient,
    private val name: String,
  ) {
    public val cancel: Cancel = Cancel(client, name)

    public class Cancel internal constructor(
      private val client: HttpClient,
      private val name: String,
    ) {
      public val post: Post = Post(client, name)

      public class Post internal constructor(
        private val client: HttpClient,
        private val name: String,
      ) {
        public suspend operator fun invoke() {
          val response = client.post("v1/${this.name.encodeURLParameter()}:cancel")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
