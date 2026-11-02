package io.github.nomisrev.render.test.client.operations.deprecated

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.Deprecated

public class Legacy internal constructor(
  private val client: HttpClient,
) {
  @Deprecated("Deprecated by the API provider")
  public val `get`: Get = Get(client)

  @Deprecated("Deprecated by the API provider")
  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    @Deprecated("Deprecated by the API provider")
    public suspend operator fun invoke() {
      val response = client.get("/legacy")
      if (response.status.value != 200) throw ResponseException(response, "")
    }
  }
}
