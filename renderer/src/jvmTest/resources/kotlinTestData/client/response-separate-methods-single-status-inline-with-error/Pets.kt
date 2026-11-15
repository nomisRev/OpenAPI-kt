package io.github.nomisrev.render.test.client.response.separate.methods.single.status.`inline`.with.error

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

public class Pets internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend fun json(): JsonResponse {
      val response = __client.get("pets") {
        `header`(HttpHeaders.Accept, ContentType.Application.Json)
      }
      return when (response.status.value) {
        200 -> response.body<JsonResponse.Ok>()
        404 -> NotFound(response.body())
        else -> throw ResponseException(response, "")
      }
    }

    public suspend fun textPlain(): TextPlainResponse {
      val response = __client.get("pets") {
        `header`(HttpHeaders.Accept, ContentType.Text.Plain)
      }
      return when (response.status.value) {
        200 -> TextPlainResponse.Ok(response.body())
        404 -> NotFound(response.body())
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface JsonResponse {
      @Serializable
      public data class Ok(
        public val id: Long,
        public val name: String,
      ) : JsonResponse
    }

    public sealed interface TextPlainResponse {
      public data class Ok(
        public val `value`: String,
      ) : TextPlainResponse
    }

    public data class NotFound(
      public val `value`: Int,
    ) : JsonResponse,
        TextPlainResponse
  }
}
