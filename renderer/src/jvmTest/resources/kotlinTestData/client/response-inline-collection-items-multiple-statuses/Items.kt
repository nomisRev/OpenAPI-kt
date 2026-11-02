package io.github.nomisrev.render.test.client.response.`inline`.collection.items.multiple.statuses

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

public class Items internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): Response {
      val response = client.get("/items")
      return when (response.status.value) {
        200 -> Response.Ok(response.body())
        201 -> Response.Created(response.body())
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface Response {
      @Serializable
      public data class Id(
        public val id: String,
      )

      @Serializable
      public data class Name(
        public val name: String,
      )

      public data class Ok(
        public val `value`: List<Id>,
      ) : Response

      public data class Created(
        public val `value`: List<Name>,
      ) : Response
    }
  }
}
