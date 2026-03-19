package io.github.nomisrev.render.test.client.root.operations

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.`get`
import io.ktor.serialization.kotlinx.json.json
import kotlin.String
import kotlin.Unit

public class Api internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val health: Health = Health(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      client.get("/")
    }
  }
}

public fun ApiClient(baseUrl: String, block: HttpClientConfig<*>.() -> Unit = {}): Api {
  val client = HttpClient {
    install(ContentNegotiation) { json() }
    defaultRequest { url(baseUrl) }
    block()
  }
  return Api(client)
}
