package io.github.nomisrev.render.test.client.factory.servers

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

public sealed interface ApiServer {
  public val url: String

  public data object Production : ApiServer {
    override val url: String = "https://api.example.com"
  }

  public data object Staging : ApiServer {
    override val url: String = "https://staging-api.example.com"
  }

  public data class Custom(
    override val url: String,
  ) : ApiServer
}

public fun ApiClient(server: ApiServer = ApiServer.Production, block: HttpClientConfig<*>.() -> Unit = {}): Api {
  val client = HttpClient {
    install(ContentNegotiation) { json() }
    defaultRequest { url(server.url) }
    block()
  }
  return KtorApi(client)
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
