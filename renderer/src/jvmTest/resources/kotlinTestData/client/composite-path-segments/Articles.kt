package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Articles internal constructor(
  private val __client: HttpClient,
) {
  public val json: Json = Json(__client)

  public val xml: Xml = Xml(__client)

  public class Json internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("articles.json")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class Xml internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("articles.xml")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
