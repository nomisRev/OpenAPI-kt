package io.github.nomisrev.render.test.client.parameter.styles

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.String

public class Codes internal constructor(
  private val client: HttpClient,
) {
  public fun code(code: String): CodePath = CodePath(client, code)

  public class CodePath internal constructor(
    private val client: HttpClient,
    private val code: String,
  ) {
    public val `get`: Get = Get(client, code)

    public class Get internal constructor(
      private val client: HttpClient,
      private val code: String,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("/codes/;code=$code")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
