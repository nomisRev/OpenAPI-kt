package io.github.nomisrev.render.test.client.media.type.without.schema

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
  public val certificate: Certificate = Certificate(client)

  public val report: Report = Report(client)

  public val `data`: Data = Data(client)

  public val note: Note = Note(client)

  public val picture: Picture = Picture(client)

  public val docs: Docs = Docs(client)

  public val upload: Upload = Upload(client)

  public val uploadOptional: UploadOptional = UploadOptional(client)

  public val uploadPair: UploadPair = UploadPair(client)

  public val uploadPairOptional: UploadPairOptional = UploadPairOptional(client)

  public val uploadImage: UploadImage = UploadImage(client)

  public val uploadAny: UploadAny = UploadAny(client)

  public val echo: Echo = Echo(client)

  public val echoText: EchoText = EchoText(client)

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
