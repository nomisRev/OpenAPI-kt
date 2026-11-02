package io.github.nomisrev.render.test.client.json.string.bodies

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.JsonConvertException
import kotlin.String
import kotlinx.serialization.json.JsonPrimitive

public class Notes internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend fun json(): String = client.get("/notes") {
      `header`(HttpHeaders.Accept, ContentType.Application.Json)
    }.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") }

    public suspend fun textPlain(): String = client.get("/notes") {
      `header`(HttpHeaders.Accept, ContentType.Text.Plain)
    }.body()
  }
}
