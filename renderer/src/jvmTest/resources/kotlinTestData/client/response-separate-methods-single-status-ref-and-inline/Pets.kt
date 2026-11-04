package io.github.nomisrev.render.test.client.response.separate.methods.single.status.ref.and.`inline`

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlin.Long
import kotlinx.serialization.Serializable

public class Pets internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend fun json(): Pet {
      val response = client.get("pets") {
        `header`(HttpHeaders.Accept, ContentType.Application.Json)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    public suspend fun xml(): XmlResponse {
      val response = client.get("pets") {
        `header`(HttpHeaders.Accept, ContentType.Application.Xml)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    @Serializable
    public data class XmlResponse(
      public val id: Long,
    )
  }
}
