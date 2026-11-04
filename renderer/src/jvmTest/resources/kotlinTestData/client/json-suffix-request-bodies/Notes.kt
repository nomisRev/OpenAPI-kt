package io.github.nomisrev.render.test.client.json.suffix.request.bodies

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String
import kotlinx.serialization.json.JsonPrimitive

public class Notes internal constructor(
  private val client: HttpClient,
) {
  public val put: Put = Put(client)

  public class Put internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: String? = null) {
      val response = client.put("notes") {
        body?.let {
          contentType(ContentType("application", "merge-patch+json"))
          setBody(JsonPrimitive(it))
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
