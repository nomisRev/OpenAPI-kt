package io.github.nomisrev.render.test.client.parameter.styles

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.Int
import kotlin.collections.List

public class Label internal constructor(
  private val client: HttpClient,
) {
  public fun ids(ids: List<Int>): IdsPath = IdsPath(client, ids)

  public class IdsPath internal constructor(
    private val client: HttpClient,
    private val ids: List<Int>,
  ) {
    public val `get`: Get = Get(client, ids)

    public class Get internal constructor(
      private val client: HttpClient,
      private val ids: List<Int>,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("label/${if (this.ids.isEmpty()) "" else ".${this.ids.joinToString(",")}"}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
