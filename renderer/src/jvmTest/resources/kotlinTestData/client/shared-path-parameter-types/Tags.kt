package io.github.nomisrev.render.test.client.shared.path.parameter.types

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.Int
import kotlin.collections.List

public class Tags internal constructor(
  private val __client: HttpClient,
) {
  public fun ids(ids: List<Int>): IdsIntListPath = IdsIntListPath(__client, ids)

  public fun ids(ids: Int): IdsIntPath = IdsIntPath(__client, ids)

  public class IdsIntListPath internal constructor(
    private val __client: HttpClient,
    private val _ids: List<Int>,
  ) {
    public val `get`: Get = Get(__client, _ids)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _ids: List<Int>,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("tags/${this._ids.joinToString(",")}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class IdsIntPath internal constructor(
    private val __client: HttpClient,
    private val _ids: Int,
  ) {
    public val first: First = First(__client, _ids)

    public class First internal constructor(
      private val __client: HttpClient,
      private val _ids: Int,
    ) {
      public val `get`: Get = Get(__client, _ids)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _ids: Int,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("tags/${this._ids}/first")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
