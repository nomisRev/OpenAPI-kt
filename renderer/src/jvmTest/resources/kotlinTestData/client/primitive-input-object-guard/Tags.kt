package io.github.nomisrev.render.test.client.primitive.input.`object`.guard

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
import kotlin.String
import kotlinx.serialization.Serializable

public class Tags internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(filter: Filter? = null) {
      val response = client.get("/tags") {
        filter?.let { parameter("filter", it) }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
    }

    @Serializable
    public data class Filter(
      public val `value`: String,
    )
  }
}
