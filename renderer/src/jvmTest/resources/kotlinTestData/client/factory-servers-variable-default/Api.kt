package io.github.nomisrev.render.test.client.factory.servers.variable.default

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlin.AutoCloseable
import kotlin.String
import kotlin.Unit

public class Api internal constructor(
  private val __client: HttpClient,
) : AutoCloseable {
  public val pets: Pets = Pets(__client)

  public constructor(server: ApiServer = ApiServer.Production(), block: HttpClientConfig<*>.() -> Unit) : this(HttpClient {
    defaultRequest { url(server.url) }
    block()
    // Ktor appends a relative path to the base URL only when its path ends with `/`.
    defaultRequest { url { if (!encodedPath.endsWith("/")) encodedPath += "/" } }
  }
  )

  public constructor(server: ApiServer = ApiServer.Production()) : this(HttpClient {
    defaultRequest { url(server.url) }
    install(ContentNegotiation) { json() }
    // Ktor appends a relative path to the base URL only when its path ends with `/`.
    defaultRequest { url { if (!encodedPath.endsWith("/")) encodedPath += "/" } }
  }
  )

  override fun close() {
    __client.close()
  }
}

public sealed interface ApiServer {
  public val url: String

  public data class Production(
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

  public data object Backup : ApiServer {
    override val url: String = "https://backup-api.example.com"
  }

  public data class Custom(
    override val url: String,
  ) : ApiServer
}
