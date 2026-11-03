package io.github.nomisrev.render.test.client.media.type.range.uploads

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlin.AutoCloseable
import kotlin.String
import kotlin.Unit

public class Api internal constructor(
  private val client: HttpClient,
) : AutoCloseable {
  public val avatars: Avatars = Avatars(client)

  public val thumbnails: Thumbnails = Thumbnails(client)

  public val media: Media = Media(client)

  public val documents: Documents = Documents(client)

  public val scans: Scans = Scans(client)

  public val fonts: Fonts = Fonts(client)

  public constructor(baseUrl: String, block: HttpClientConfig<*>.() -> Unit) : this(HttpClient {
    defaultRequest { url(baseUrl) }
    block()
  }
  )

  public constructor(baseUrl: String) : this(HttpClient {
    defaultRequest { url(baseUrl) }
    install(ContentNegotiation) { json() }
  }
  )

  override fun close() {
    client.close()
  }
}
