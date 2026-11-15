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
  private val __client: HttpClient,
) : AutoCloseable {
  public val certificate: Certificate = Certificate(__client)

  public val report: Report = Report(__client)

  public val `data`: Data = Data(__client)

  public val note: Note = Note(__client)

  public val picture: Picture = Picture(__client)

  public val docs: Docs = Docs(__client)

  public val upload: Upload = Upload(__client)

  public val uploadOptional: UploadOptional = UploadOptional(__client)

  public val uploadPair: UploadPair = UploadPair(__client)

  public val uploadPairOptional: UploadPairOptional = UploadPairOptional(__client)

  public val uploadImage: UploadImage = UploadImage(__client)

  public val uploadAny: UploadAny = UploadAny(__client)

  public val echo: Echo = Echo(__client)

  public val echoText: EchoText = EchoText(__client)

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
