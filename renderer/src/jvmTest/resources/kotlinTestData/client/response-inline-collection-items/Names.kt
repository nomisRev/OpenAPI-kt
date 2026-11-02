package io.github.nomisrev.render.test.client.response.`inline`.collection.items

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

public class Names internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): List<Name> = client.get("/names").body()

    @Serializable
    public data class Name(
      public val name: String,
    )
  }
}
