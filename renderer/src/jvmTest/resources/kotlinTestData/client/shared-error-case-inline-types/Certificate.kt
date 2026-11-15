package io.github.nomisrev.render.test.client.shared.error.case.`inline`.types

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`header`
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Certificate internal constructor(
  private val __client: HttpClient,
) {
  public val post: Post = Post(__client)

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend fun json(): JsonResponse {
      val response = __client.post("certificate") {
        `header`(HttpHeaders.Accept, ContentType.Application.Json)
      }
      return when (response.status.value) {
        200 -> response.body<JsonResponse.Ok>()
        400 -> response.body<BadRequest>()
        404 -> response.body<NotFound>()
        else -> throw ResponseException(response, "")
      }
    }

    public suspend fun pdf(): PdfResponse {
      val response = __client.post("certificate") {
        `header`(HttpHeaders.Accept, ContentType.Application.Pdf)
      }
      return when (response.status.value) {
        200 -> PdfResponse.Ok(response)
        400 -> response.body<BadRequest>()
        404 -> response.body<NotFound>()
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface JsonResponse {
      @Serializable
      public data class Ok(
        public val id: String,
      ) : JsonResponse
    }

    public sealed interface PdfResponse {
      public data class Ok(
        public val response: HttpResponse,
      ) : PdfResponse
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class BadRequest(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val error: Error? = null,
      @SerialName("error_description")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val errorDescription: ErrorDescription? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val details: Details? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val causes: List<Causes>? = null,
    ) : JsonResponse,
        PdfResponse {
      @Serializable
      public data class Causes(
        public val code: Int,
      )

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class Details(
        public val `field`: String,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val reason: Reason? = null,
      ) {
        @Serializable
        public enum class Reason(
          public val `value`: String,
        ) {
          @SerialName("missing")
          Missing("missing"),
          @SerialName("malformed")
          Malformed("malformed"),
          ;
        }
      }

      @Serializable
      public enum class Error(
        public val `value`: String,
      ) {
        @SerialName("missing_parameter")
        MissingParameter("missing_parameter"),
        @SerialName("invalid_parameter")
        InvalidParameter("invalid_parameter"),
        ;
      }

      @Serializable
      public enum class ErrorDescription(
        public val `value`: String,
      ) {
        @SerialName("Bad request")
        BadRequest("Bad request"),
        @SerialName("Invalid format")
        InvalidFormat("Invalid format"),
        ;
      }
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class NotFound(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val error: Error? = null,
    ) : JsonResponse,
        PdfResponse {
      @Serializable
      public enum class Error(
        public val `value`: String,
      ) {
        @SerialName("record_not_found")
        RecordNotFound("record_not_found"),
        @SerialName("url_not_found")
        UrlNotFound("url_not_found"),
        ;
      }
    }
  }
}
