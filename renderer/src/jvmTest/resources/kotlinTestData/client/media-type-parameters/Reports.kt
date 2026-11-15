package io.github.nomisrev.render.test.client.media.type.parameters

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.String

public class Reports internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend fun json(): JsonResponse {
      val response = __client.get("reports") {
        `header`(HttpHeaders.Accept, ContentType.Application.Json.withParameter("charset", "utf-8"))
      }
      return when (response.status.value) {
        200 -> JsonResponse.Ok(response.body())
        400 -> run {
          val ct = response.contentType()
          when {
            ct?.match(ContentType("application", "problem+json")) == true -> ProblemJsonBadRequest(response.body())
            ct?.match(ContentType.Text.Plain) == true -> TextPlainBadRequest(response.body())
            else -> ProblemJsonBadRequest(response.body())
          }
        }
        in 400..499 -> run {
          val ct = response.contentType()
          when {
            ct?.match(ContentType.Application.Json) == true -> JsonStatus4XX(response.status, response.body())
            ct?.match(ContentType.Text.Plain) == true -> TextPlainStatus4XX(response.status, response.body())
            else -> JsonStatus4XX(response.status, response.body())
          }
        }
        else -> throw ResponseException(response, "")
      }
    }

    public suspend fun textCsv(): TextCsvResponse {
      val response = __client.get("reports") {
        `header`(HttpHeaders.Accept, ContentType("text", "csv").withParameter("charset", "utf-8"))
      }
      return when (response.status.value) {
        200 -> TextCsvResponse.Ok(response.body())
        400 -> run {
          val ct = response.contentType()
          when {
            ct?.match(ContentType("application", "problem+json")) == true -> ProblemJsonBadRequest(response.body())
            ct?.match(ContentType.Text.Plain) == true -> TextPlainBadRequest(response.body())
            else -> ProblemJsonBadRequest(response.body())
          }
        }
        in 400..499 -> run {
          val ct = response.contentType()
          when {
            ct?.match(ContentType.Application.Json) == true -> JsonStatus4XX(response.status, response.body())
            ct?.match(ContentType.Text.Plain) == true -> TextPlainStatus4XX(response.status, response.body())
            else -> JsonStatus4XX(response.status, response.body())
          }
        }
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface JsonResponse {
      public data class Ok(
        public val `value`: Pet,
      ) : JsonResponse
    }

    public sealed interface TextCsvResponse {
      public data class Ok(
        public val `value`: String,
      ) : TextCsvResponse
    }

    public data class ProblemJsonBadRequest(
      public val `value`: Problem,
    ) : JsonResponse,
        TextCsvResponse

    public data class TextPlainBadRequest(
      public val `value`: String,
    ) : JsonResponse,
        TextCsvResponse

    public data class JsonStatus4XX(
      public val status: HttpStatusCode,
      public val `value`: Problem?,
    ) : JsonResponse,
        TextCsvResponse

    public data class TextPlainStatus4XX(
      public val status: HttpStatusCode,
      public val `value`: String?,
    ) : JsonResponse,
        TextCsvResponse
  }
}
