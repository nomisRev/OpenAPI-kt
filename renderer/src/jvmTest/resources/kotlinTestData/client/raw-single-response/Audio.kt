package io.github.nomisrev.render.test.client.raw.single.response

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import io.ktor.client.statement.HttpResponse

public class Audio internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): HttpResponse {
      val response = client.get("/audio")
      return response
    }
  }
}
