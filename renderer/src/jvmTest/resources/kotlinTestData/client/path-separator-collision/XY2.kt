package io.github.nomisrev.render.test.client.path.separator.collision

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete

public class XY2 internal constructor(
  private val client: HttpClient,
) {
  public val a: A = A(client)

  public class A internal constructor(
    private val client: HttpClient,
  ) {
    public val delete: Delete = Delete(client)

    public val `get`: Get = Get(client)

    public class Delete internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = client.delete("x_y/a")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("x_y/a")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
