package io.github.nomisrev.render.test.client.path.parameter.encoding

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
  public val simple: Simple = Simple(client)

  public val label: Label = Label(client)

  public val matrix: Matrix = Matrix(client)

  public val simpleArray: SimpleArray = SimpleArray(client)

  public val labelArray: LabelArray = LabelArray(client)

  public val labelExploded: LabelExploded = LabelExploded(client)

  public val matrixArray: MatrixArray = MatrixArray(client)

  public val matrixExploded: MatrixExploded = MatrixExploded(client)

  public val numbers: Numbers = Numbers(client)

  public val kinds: Kinds = Kinds(client)

  public val refs: Refs = Refs(client)

  public val versions: Versions = Versions(client)

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
