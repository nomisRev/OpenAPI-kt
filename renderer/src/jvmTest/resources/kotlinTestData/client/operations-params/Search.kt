package io.github.nomisrev.render.test.client.operations.params

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.cookie
import io.ktor.client.request.parameter
import kotlin.String

public interface Search {
  public val `get`: Get

  public interface Get {
    public suspend operator fun invoke(
      query: String,
      xApiKey: String,
      session: String,
    )
  }
}

internal class KtorSearch(
  private val client: HttpClient,
) : Search {
  override val `get`: Search.Get = object : Search.Get {
    override suspend operator fun invoke(
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
