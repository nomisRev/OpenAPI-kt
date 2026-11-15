package io.github.nomisrev.render.test.client.literal.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Apps internal constructor(
  private val __client: HttpClient,
) {
  public val dash: Dash = Dash(__client)

  public class Dash internal constructor(
    private val __client: HttpClient,
  ) {
    public val config: Config = Config(__client)

    public class Config internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("apps/-/config")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
