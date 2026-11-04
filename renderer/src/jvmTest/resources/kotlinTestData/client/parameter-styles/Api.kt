package io.github.nomisrev.render.test.client.parameter.styles

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
  public val items: Items = Items(client)

  public val simple: Simple = Simple(client)

  public val label: Label = Label(client)

  public val labelExploded: LabelExploded = LabelExploded(client)

  public val matrix: Matrix = Matrix(client)

  public val matrixExploded: MatrixExploded = MatrixExploded(client)

  public val codes: Codes = Codes(client)

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
