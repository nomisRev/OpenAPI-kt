package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.patch
import kotlin.Long

public class Pulls internal constructor(
  private val __client: HttpClient,
) {
  public fun index(index: Long): IndexPath = IndexPath(__client, index)

  public class IndexPath internal constructor(
    private val __client: HttpClient,
    private val _index: Long,
  ) {
    public val patch: Patch = Patch(__client, _index)

    public val diff: Diff = Diff(__client, _index)

    public val dotPatch: DotPatch = DotPatch(__client, _index)

    public class Patch internal constructor(
      private val __client: HttpClient,
      private val _index: Long,
    ) {
      public suspend operator fun invoke() {
        val response = __client.patch("pulls/${this._index}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }

    public class Diff internal constructor(
      private val __client: HttpClient,
      private val _index: Long,
    ) {
      public val `get`: Get = Get(__client, _index)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _index: Long,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("pulls/${this._index}.diff")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }

    public class DotPatch internal constructor(
      private val __client: HttpClient,
      private val _index: Long,
    ) {
      public val `get`: Get = Get(__client, _index)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _index: Long,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("pulls/${this._index}.patch")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
