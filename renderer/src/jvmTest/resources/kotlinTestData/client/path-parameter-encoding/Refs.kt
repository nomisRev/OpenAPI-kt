package io.github.nomisrev.render.test.client.path.parameter.encoding

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.Int

public class Refs internal constructor(
  private val client: HttpClient,
) {
  public val aB: AB = AB(client)

  public val c: C = C(client)

  public fun ref(ref: Int): RefPath = RefPath(client, ref)

  public class AB internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("refs/a%2Fb")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class C internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("refs/c")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class RefPath internal constructor(
    private val client: HttpClient,
    private val ref: Int,
  ) {
    public val `get`: Get = Get(client, ref)

    public class Get internal constructor(
      private val client: HttpClient,
      private val ref: Int,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("refs/$ref")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
