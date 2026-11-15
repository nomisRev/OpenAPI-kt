package io.github.nomisrev.render.test.client.root.`class`.name.title

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlin.AutoCloseable
import kotlin.String
import kotlin.Unit

public class CSGOV3Scores internal constructor(
  private val __client: HttpClient,
) : AutoCloseable {
  public val scores: Scores = Scores(__client)

  public constructor(server: CSGOV3ScoresServer, block: HttpClientConfig<*>.() -> Unit) : this(HttpClient {
    defaultRequest { url(server.url) }
    block()
    // Ktor appends a relative path to the base URL only when its path ends with `/`.
    defaultRequest { url { if (!encodedPath.endsWith("/")) encodedPath += "/" } }
  }
  )

  public constructor(server: CSGOV3ScoresServer) : this(HttpClient {
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

public data class CSGOV3ScoresServer(
  public val region: String = "eu",
) {
  public val url: String
    get() = "https://$region.example.com"
}
