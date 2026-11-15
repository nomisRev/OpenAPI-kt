package io.github.nomisrev.render.test.client.wildcard.media.types

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
  public val objects: Objects = Objects(__client)

  public val texts: Texts = Texts(__client)

  public val files: Files = Files(__client)

  public val appObjects: AppObjects = AppObjects(__client)

  public val appTexts: AppTexts = AppTexts(__client)

  public val appFiles: AppFiles = AppFiles(__client)

  public val notes: Notes = Notes(__client)

  public val labels: Labels = Labels(__client)

  public val greeting: Greeting = Greeting(__client)

  public val appGreeting: AppGreeting = AppGreeting(__client)

  public val textGreeting: TextGreeting = TextGreeting(__client)

  public val animal: Animal = Animal(__client)

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
