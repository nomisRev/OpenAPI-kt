package io.github.nomisrev.render.test.client.response.ranges

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlin.Int
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

public class Widgets internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend fun json(): JsonResponse {
      val response = client.get("/widgets") {
        `header`(HttpHeaders.Accept, ContentType.Application.Json)
      }
      return when (response.status.value) {
        200 -> JsonResponse.Ok(response.body())
        in 200..299 -> if (response.status.value == 204) JsonResponse.Status2XX(response.status, null) else JsonResponse.Status2XX(response.status, response.body<JsonResponse.Status2XX.Status2XXBody>())
        in 400..499 -> Status4XX(response.status)
        in 500..599 -> Status5XX(response.status, response.body<Status5XX.Status5XXBody>())
        else -> Default(response.status, response.body())
      }
    }

    public suspend fun xml(): XmlResponse {
      val response = client.get("/widgets") {
        `header`(HttpHeaders.Accept, ContentType.Application.Xml)
      }
      return when (response.status.value) {
        200 -> throw ResponseException(response, "")
        in 200..299 -> if (response.status.value == 204) XmlResponse.Status2XX(response.status, null) else XmlResponse.Status2XX(response.status, response.body())
        in 400..499 -> Status4XX(response.status)
        in 500..599 -> Status5XX(response.status, response.body<Status5XX.Status5XXBody>())
        else -> Default(response.status, response.body())
      }
    }

    public sealed interface JsonResponse {
      public data class Ok(
        public val `value`: String,
      ) : JsonResponse

      public data class Status2XX(
        public val status: HttpStatusCode,
        public val `value`: Status2XXBody?,
      ) : JsonResponse {
        @Serializable
        public data class Status2XXBody(
          public val id: String,
          public val version: Int? = null,
        )
      }
    }

    public sealed interface XmlResponse {
      public data class Status2XX(
        public val status: HttpStatusCode,
        public val `value`: String?,
      ) : XmlResponse
    }

    public data class Status4XX(
      public val status: HttpStatusCode,
    ) : JsonResponse,
        XmlResponse

    public data class Status5XX(
      public val status: HttpStatusCode,
      public val `value`: Status5XXBody?,
    ) : JsonResponse,
        XmlResponse {
      @JvmInline
      @Serializable
      public value class Status5XXBody(
        public val message: String? = null,
      )
    }

    public data class Default(
      public val status: HttpStatusCode,
      public val `value`: String,
    ) : JsonResponse,
        XmlResponse
  }
}
