package io.github.nomisrev.render.test.client.root.`class`.name.path

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlin.AutoCloseable
import kotlin.String
import kotlin.Unit

public class FilesApi internal constructor(
  private val client: HttpClient,
) : AutoCloseable {
  public val files: Files = Files(client)

  public constructor(server: FilesApiServer, block: HttpClientConfig<*>.() -> Unit) : this(HttpClient {
    defaultRequest { url(server.url) }
    block()
    // Ktor appends a relative path to the base URL only when its path ends with `/`.
    defaultRequest { url { if (!encodedPath.endsWith("/")) encodedPath += "/" } }
  }
  )

  public constructor(server: FilesApiServer) : this(HttpClient {
    defaultRequest { url(server.url) }
    install(ContentNegotiation) { json() }
    // Ktor appends a relative path to the base URL only when its path ends with `/`.
    defaultRequest { url { if (!encodedPath.endsWith("/")) encodedPath += "/" } }
  }
  )

  override fun close() {
    client.close()
  }
}

public data class FilesApiServer(
  public val region: String = "eu",
) {
  public val url: String
    get() = "https://$region.example.com"
}
