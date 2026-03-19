package io.github.nomisrev.render.test.client.factory.server.variables

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
  public val pets: Pets = Pets(client)
}

public sealed interface ApiServer {
  public val url: String

  public data class Configurable(
    public val environment: Environment = Environment.Prod,
    public val version: String = "v1",
  ) : ApiServer {
    override val url: String
      get() = "https://${environment.value}.api.example.com/$version"

    public enum class Environment(
      public val `value`: String,
    ) {
      Prod("prod"),
      Staging("staging"),
      Dev("dev"),
      ;
    }
  }

  public data class Custom(
    override val url: String,
  ) : ApiServer
}

public fun ApiClient(server: ApiServer = ApiServer.Configurable(), block: HttpClientConfig<*>.() -> Unit = {}): Api {
  val client = HttpClient {
    install(ContentNegotiation) { json() }
    defaultRequest { url(server.url) }
    block()
  }
  return Api(client)
}

public fun ApiClient(baseUrl: String, block: HttpClientConfig<*>.() -> Unit = {}): Api {
  val client = HttpClient {
    install(ContentNegotiation) { json() }
    defaultRequest { url(baseUrl) }
    block()
  }
  return Api(client)
}
