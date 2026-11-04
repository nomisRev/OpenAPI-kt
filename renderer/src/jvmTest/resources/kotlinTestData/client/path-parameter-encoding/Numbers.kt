package io.github.nomisrev.render.test.client.path.parameter.encoding

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.Long

public class Numbers internal constructor(
  private val client: HttpClient,
) {
  public fun n(n: Long): NPath = NPath(client, n)

  public class NPath internal constructor(
    private val client: HttpClient,
    private val n: Long,
  ) {
    public val `get`: Get = Get(client, n)

    public class Get internal constructor(
      private val client: HttpClient,
      private val n: Long,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("/numbers/$n")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
