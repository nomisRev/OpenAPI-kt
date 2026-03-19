package io.github.nomisrev.render.test.client.operations.optional

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlin.String
import kotlin.Unit

public class Api internal constructor(
  private val client: HttpClient,
) {
  public val items: Items = Items(client)
}

public fun ApiClient(baseUrl: String, block: HttpClientConfig<*>.() -> Unit = {}): Api {
  val client = HttpClient {
    install(ContentNegotiation) { json() }
    defaultRequest { url(baseUrl) }
    block()
  }
  return Api(client)
}
