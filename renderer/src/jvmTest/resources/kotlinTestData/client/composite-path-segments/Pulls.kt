package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.patch
import kotlin.Long

public class Pulls internal constructor(
  private val client: HttpClient,
) {
  public fun index(index: Long): IndexPath = IndexPath(client, index)

  public class IndexPath internal constructor(
    private val client: HttpClient,
    private val index: Long,
  ) {
    public val patch: Patch = Patch(client, index)

    public val diff: Diff = Diff(client, index)

    public val dotPatch: DotPatch = DotPatch(client, index)

    public class Patch internal constructor(
      private val client: HttpClient,
      private val index: Long,
    ) {
      public suspend operator fun invoke() {
        val response = client.patch("pulls/${this.index}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }

    public class Diff internal constructor(
      private val client: HttpClient,
      private val index: Long,
    ) {
      public val `get`: Get = Get(client, index)

      public class Get internal constructor(
        private val client: HttpClient,
        private val index: Long,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("pulls/${this.index}.diff")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }

    public class DotPatch internal constructor(
      private val client: HttpClient,
      private val index: Long,
    ) {
      public val `get`: Get = Get(client, index)

      public class Get internal constructor(
        private val client: HttpClient,
        private val index: Long,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("pulls/${this.index}.patch")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
