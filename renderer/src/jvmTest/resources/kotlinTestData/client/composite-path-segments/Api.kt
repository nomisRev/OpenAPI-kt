package io.github.nomisrev.render.test.client.composite.path.segments

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
  public val media: Media = Media(__client)

  public val hackathons: Hackathons = Hackathons(__client)

  public val lists: Lists = Lists(__client)

  public val v1: V1 = V1(__client)

  public val v: V = V(__client)

  public val ranges: Ranges = Ranges(__client)

  public val articles: Articles = Articles(__client)

  public val orders: Orders = Orders(__client)

  public val news: News = News(__client)

  public val policy: Policy = Policy(__client)

  public val pulls: Pulls = Pulls(__client)

  public val reports: Reports = Reports(__client)

  public val tags: Tags = Tags(__client)

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
    __client.close()
  }
}
