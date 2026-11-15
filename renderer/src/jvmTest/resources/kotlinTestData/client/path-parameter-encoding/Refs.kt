package io.github.nomisrev.render.test.client.path.parameter.encoding

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.Int

public class Refs internal constructor(
  private val __client: HttpClient,
) {
  public val aB: AB = AB(__client)

  public val c: C = C(__client)

  public fun ref(ref: Int): RefPath = RefPath(__client, ref)

  public class AB internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("refs/a%2Fb")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class C internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("refs/c")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class RefPath internal constructor(
    private val __client: HttpClient,
    private val _ref: Int,
  ) {
    public val `get`: Get = Get(__client, _ref)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _ref: Int,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("refs/${this._ref}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
