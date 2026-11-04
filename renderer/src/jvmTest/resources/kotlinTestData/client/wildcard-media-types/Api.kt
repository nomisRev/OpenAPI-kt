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
  private val client: HttpClient,
) : AutoCloseable {
  public val objects: Objects = Objects(client)

  public val texts: Texts = Texts(client)

  public val files: Files = Files(client)

  public val appObjects: AppObjects = AppObjects(client)

  public val appTexts: AppTexts = AppTexts(client)

  public val appFiles: AppFiles = AppFiles(client)

  public val notes: Notes = Notes(client)

  public val labels: Labels = Labels(client)

  public val greeting: Greeting = Greeting(client)

  public val appGreeting: AppGreeting = AppGreeting(client)

  public val textGreeting: TextGreeting = TextGreeting(client)

  public val animal: Animal = Animal(client)

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
