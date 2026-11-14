package io.github.nomisrev.render.test.client.parameter.name.collisions

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
  private val client: HttpClient,
) : AutoCloseable {
  public val albums: Albums = Albums(client)

  public val forms: Forms = Forms(client)

  public val uploads: Uploads = Uploads(client)

  public val prices: Prices = Prices(client)

  public val segments: Segments = Segments(client)

  public val things: Things = Things(client)

  public val limits: Limits = Limits(client)

  public val reports: Reports = Reports(client)

  public val variants: Variants = Variants(client)

  public val echo: Echo = Echo(client)

  public constructor(baseUrl: String, block: HttpClientConfig<*>.() -> Unit) : this(HttpClient {
    defaultRequest { url(baseUrl) }
    block()
    // Ktor appends a relative path to the base URL only when its path ends with `/`.
    defaultRequest { url { if (!encodedPath.endsWith("/")) encodedPath += "/" } }
  }
  )

  public constructor(baseUrl: String) : this(HttpClient {
    defaultRequest { url(baseUrl) }
    install(ContentNegotiation) { json() }
    // Ktor appends a relative path to the base URL only when its path ends with `/`.
    defaultRequest { url { if (!encodedPath.endsWith("/")) encodedPath += "/" } }
  }
  )

  override fun close() {
    client.close()
  }
}
