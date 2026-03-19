package io.github.nomisrev.render.test.client.operations.params

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.cookie
import io.ktor.client.request.parameter
import kotlin.String

public class Search internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      query: String,
      xApiKey: String,
      session: String,
    ) {
      client.get("/search") {
        parameter("query", query)
        `header`("X-Api-Key", xApiKey)
        cookie("session", session)
      }
    }
  }
}
