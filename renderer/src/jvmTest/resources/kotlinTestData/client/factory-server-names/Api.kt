package io.github.nomisrev.render.test.client.factory.server.names

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
  public val ping: Ping = Ping(__client)

  public constructor(server: ApiServer = ApiServer.ApiExample.Https, block: HttpClientConfig<*>.() -> Unit) : this(HttpClient {
    defaultRequest { url(server.url) }
    block()
    // Ktor appends a relative path to the base URL only when its path ends with `/`.
    defaultRequest { url { if (!encodedPath.endsWith("/")) encodedPath += "/" } }
  }
  )

  public constructor(server: ApiServer = ApiServer.ApiExample.Https) : this(HttpClient {
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

  public sealed interface ApiExample : ApiServer {
    public data object Https : ApiExample {
      override val url: String = "https://api.example.com"
    }

    public data object Http : ApiExample {
      override val url: String = "http://api.example.com"
    }
  }

  public sealed interface RegionalEndpoint : ApiServer {
    public data class Https(
      public val region: Region = Region.Eu,
    ) : RegionalEndpoint {
      override val url: String
        get() = "https://${region.value}.regional.example.com"

      public enum class Region(
        public val `value`: String,
      ) {
        Eu("eu"),
        Us("us"),
        ;
      }
    }

    public data class Http(
      public val region: Region = Region.Eu,
    ) : RegionalEndpoint {
      override val url: String
        get() = "http://${region.value}.regional.example.com"

      public enum class Region(
        public val `value`: String,
      ) {
        Eu("eu"),
        Us("us"),
        ;
      }
    }
  }

  public data object MetricsV1 : ApiServer {
    override val url: String = "https://metrics.example.com/v1"
  }

  public data object MetricsV2 : ApiServer {
    override val url: String = "https://metrics.example.com/v2"
  }

  public data object Backup : ApiServer {
    override val url: String = "https://backup.example.com"
  }

  public data class Custom(
    override val url: String,
  ) : ApiServer
}
