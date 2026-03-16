package io.github.nomisrev.render.test.client.response.mixed

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlin.String
import kotlin.Unit

public interface Api {
  public val pets: Pets
}

public fun ApiClient(baseUrl: String, block: HttpClientConfig<*>.() -> Unit = {}): Api {
  val client = HttpClient {
    install(ContentNegotiation) { json() }
    defaultRequest { url(baseUrl) }
    block()
  }
  return KtorApi(client)
}

internal class KtorApi(
  private val client: HttpClient,
) : Api {
  override val pets: Pets = KtorPets(client)
}
