package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.http.encodeURLParameter
import kotlin.String

public class V1 internal constructor(
  private val __client: HttpClient,
) {
  public fun name(name: String): NamePath = NamePath(__client, name)

  public class NamePath internal constructor(
    private val __client: HttpClient,
    private val _name: String,
  ) {
    public val cancel: Cancel = Cancel(__client, _name)

    public class Cancel internal constructor(
      private val __client: HttpClient,
      private val _name: String,
    ) {
      public val post: Post = Post(__client, _name)

      public class Post internal constructor(
        private val __client: HttpClient,
        private val _name: String,
      ) {
        public suspend operator fun invoke() {
          val response = __client.post("v1/${this._name.encodeURLParameter()}:cancel")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
