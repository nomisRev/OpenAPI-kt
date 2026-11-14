package io.github.nomisrev.render.test.client.`enum`.entry.names

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
  public val items: Items = Items(client)

  public constructor(server: ApiServer, block: HttpClientConfig<*>.() -> Unit) : this(HttpClient {
    defaultRequest { url(server.url) }
    block()
    // Ktor appends a relative path to the base URL only when its path ends with `/`.
    defaultRequest { url { if (!encodedPath.endsWith("/")) encodedPath += "/" } }
  }
  )

  public constructor(server: ApiServer) : this(HttpClient {
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

public data class ApiServer(
  public val kind: Kind = Kind.AwsMinusKms,
) {
  public val url: String
    get() = "https://${kind.value}.example.com"

  public enum class Kind(
    public val `value`: String,
  ) {
    AwsColonKms("aws:kms"),
    AwsMinusKms("aws-kms"),
    ;
  }
}
