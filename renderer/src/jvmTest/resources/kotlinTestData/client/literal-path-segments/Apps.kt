package io.github.nomisrev.render.test.client.literal.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Apps internal constructor(
  private val client: HttpClient,
) {
  public val dash: Dash = Dash(client)

  public class Dash internal constructor(
    private val client: HttpClient,
  ) {
    public val config: Config = Config(client)

    public class Config internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("apps/-/config")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
