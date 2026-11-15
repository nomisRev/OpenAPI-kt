package io.github.nomisrev.render.test.client.response.`inline`.collection.items

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

public class Names internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): List<Name> {
      val response = __client.get("names")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    @Serializable
    public data class Name(
      public val name: String,
    )
  }
}
