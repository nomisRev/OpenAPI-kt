package io.github.nomisrev.render.test.client.response.separate.methods.single.status.ref.and.`inline`

import io.ktor.client.HttpClient
import io.ktor.client.call.body
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
    public suspend fun json(): Pet = client.get("/pets") {
      `header`(HttpHeaders.Accept, ContentType.Application.Json)
    }.body()

    public suspend fun xml(): XmlResponse = client.get("/pets") {
      `header`(HttpHeaders.Accept, ContentType.Application.Xml)
    }.body()

    @Serializable
    public data class XmlResponse(
      public val id: Long,
    )
  }
}
