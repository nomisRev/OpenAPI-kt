package io.github.nomisrev.render.test.client.response.single

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import kotlin.String
import kotlin.collections.List

public class Pets internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): List<String> = client.get("/pets").body()
  }
}
