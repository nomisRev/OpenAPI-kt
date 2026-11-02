package io.github.nomisrev.render.test.client.json.string.bodies

import io.ktor.client.HttpClient
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive

public class Tags internal constructor(
  private val client: HttpClient,
) {
  public val put: Put = Put(client)

  public class Put internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: String) {
      client.put("/tags") {
        contentType(ContentType.Application.Json)
        setBody(JsonPrimitive(body))
      }
    }

    public suspend operator fun invoke(body: Name) {
      client.put("/tags") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
    }

    @Serializable
    public data class Name(
      public val name: String,
    )
  }
}
