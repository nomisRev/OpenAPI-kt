package io.github.nomisrev.render.test.client.response.`inline`.collection.items.separate.methods

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

public class Items internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend fun json(): List<Id> = client.get("/items") {
      `header`(HttpHeaders.Accept, ContentType.Application.Json)
    }.body()

    public suspend fun xml(): List<Name> = client.get("/items") {
      `header`(HttpHeaders.Accept, ContentType.Application.Xml)
    }.body()

    @JvmInline
    @Serializable
    public value class Id(
      public val id: String,
    )

    @JvmInline
    @Serializable
    public value class Name(
      public val name: String,
    )

    public sealed interface JsonResponse {
      public data class Ok(
        public val `value`: List<Id>,
      ) : JsonResponse
    }

    public sealed interface XmlResponse {
      public data class Ok(
        public val `value`: List<Name>,
      ) : XmlResponse
    }
  }
}
