package io.github.nomisrev.render.test.client.status.code.case.names

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

public class Negotiated internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend fun json(): JsonResponse {
      val response = __client.get("negotiated") {
        `header`(HttpHeaders.Accept, ContentType.Application.Json)
      }
      return when (response.status.value) {
        200 -> response.body<JsonResponse.Ok>()
        482 -> run {
          val ct = response.contentType()
          when {
            ct?.match(ContentType.Application.Json) == true -> response.body<JsonStatus482>()
            ct?.match(ContentType.Text.Plain) == true -> TextPlainStatus482(response.body())
            else -> response.body<JsonStatus482>()
          }
        }
        483 -> run {
          val ct = response.contentType()
          when {
            ct?.match(ContentType.Application.Json) == true -> response.body<JsonStatus483>()
            ct?.match(ContentType.Text.Plain) == true -> TextPlainStatus483(response.body())
            else -> response.body<JsonStatus483>()
          }
        }
        else -> throw ResponseException(response, "")
      }
    }

    public suspend fun textPlain(): TextPlainResponse {
      val response = __client.get("negotiated") {
        `header`(HttpHeaders.Accept, ContentType.Text.Plain)
      }
      return when (response.status.value) {
        200 -> TextPlainResponse.Ok(response.body())
        482 -> run {
          val ct = response.contentType()
          when {
            ct?.match(ContentType.Application.Json) == true -> response.body<JsonStatus482>()
            ct?.match(ContentType.Text.Plain) == true -> TextPlainStatus482(response.body())
            else -> response.body<JsonStatus482>()
          }
        }
        483 -> run {
          val ct = response.contentType()
          when {
            ct?.match(ContentType.Application.Json) == true -> response.body<JsonStatus483>()
            ct?.match(ContentType.Text.Plain) == true -> TextPlainStatus483(response.body())
            else -> response.body<JsonStatus483>()
          }
        }
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface JsonResponse {
      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class Ok(
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val id: String? = null,
      ) : JsonResponse
    }

    public sealed interface TextPlainResponse {
      public data class Ok(
        public val `value`: String,
      ) : TextPlainResponse
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class JsonStatus482(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val code: String? = null,
    ) : JsonResponse,
        TextPlainResponse

    public data class TextPlainStatus482(
      public val `value`: String,
    ) : JsonResponse,
        TextPlainResponse

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class JsonStatus483(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val detail: String? = null,
    ) : JsonResponse,
        TextPlainResponse

    public data class TextPlainStatus483(
      public val `value`: String,
    ) : JsonResponse,
        TextPlainResponse
  }
}
