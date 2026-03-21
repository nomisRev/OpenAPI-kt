package io.github.nomisrev.render.test.client.primitive.input.`object`.guard

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

public class Tags internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(filter: Filter? = null) {
      client.get("/tags") {
        filter?.let { parameter("filter", it) }
      }
    }

    @JvmInline
    @Serializable
    public value class Filter(
      public val `value`: String,
    )
  }
}
