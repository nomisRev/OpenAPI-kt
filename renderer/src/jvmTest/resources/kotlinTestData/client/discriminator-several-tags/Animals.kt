package io.github.nomisrev.render.test.client.discriminator.several.tags

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.collections.List

public class Animals internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): List<Animal> {
      val response = __client.get("animals")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
