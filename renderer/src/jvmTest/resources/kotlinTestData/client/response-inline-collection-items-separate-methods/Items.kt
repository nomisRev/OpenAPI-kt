package io.github.nomisrev.render.test.client.response.`inline`.collection.items.separate.methods

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

public class Items internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend fun json(): List<Id> {
      val response = __client.get("items") {
        `header`(HttpHeaders.Accept, ContentType.Application.Json)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    public suspend fun xml(): List<Name> {
      val response = __client.get("items") {
        `header`(HttpHeaders.Accept, ContentType.Application.Xml)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    @Serializable
    public data class Id(
      public val id: String,
    )

    @Serializable
    public data class Name(
      public val name: String,
    )
  }
}
