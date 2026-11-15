package io.github.nomisrev.render.test.client.path.parameter.encoding

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
  public val simple: Simple = Simple(__client)

  public val label: Label = Label(__client)

  public val matrix: Matrix = Matrix(__client)

  public val simpleArray: SimpleArray = SimpleArray(__client)

  public val labelArray: LabelArray = LabelArray(__client)

  public val labelExploded: LabelExploded = LabelExploded(__client)

  public val matrixArray: MatrixArray = MatrixArray(__client)

  public val matrixExploded: MatrixExploded = MatrixExploded(__client)

  public val numbers: Numbers = Numbers(__client)

  public val kinds: Kinds = Kinds(__client)

  public val refs: Refs = Refs(__client)

  public val versions: Versions = Versions(__client)

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
