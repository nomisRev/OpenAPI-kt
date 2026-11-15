package io.github.nomisrev.render.test.client.shared.path.parameter.types

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.Int
import kotlin.collections.List

public class Tags internal constructor(
  private val client: HttpClient,
) {
  public fun ids(ids: List<Int>): IdsIntListPath = IdsIntListPath(client, ids)

  public fun ids(ids: Int): IdsIntPath = IdsIntPath(client, ids)

  public class IdsIntListPath internal constructor(
    private val client: HttpClient,
    private val ids: List<Int>,
  ) {
    public val `get`: Get = Get(client, ids)

    public class Get internal constructor(
      private val client: HttpClient,
      private val ids: List<Int>,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("tags/${this.ids.joinToString(",")}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class IdsIntPath internal constructor(
    private val client: HttpClient,
    private val ids: Int,
  ) {
    public val first: First = First(client, ids)

    public class First internal constructor(
      private val client: HttpClient,
      private val ids: Int,
    ) {
      public val `get`: Get = Get(client, ids)

      public class Get internal constructor(
        private val client: HttpClient,
        private val ids: Int,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("tags/${this.ids}/first")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
