package io.github.nomisrev.render.test.client.response.separate.methods

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlin.Int
import kotlin.String

public class Pets internal constructor(
  private val client: HttpClient,
) {
  public fun petId(petId: String): PetIdPath = PetIdPath(client, petId)

  public class PetIdPath internal constructor(
    private val client: HttpClient,
    private val petId: String,
  ) {
    public val `get`: Get = Get(client, petId)

    public class Get internal constructor(
      private val client: HttpClient,
      private val petId: String,
    ) {
      public suspend fun json(): JsonResponse {
        val response = client.get("/pets/$petId") {
          `header`(HttpHeaders.Accept, ContentType.Application.Json)
        }
        return when (response.status.value) {
          200 -> JsonResponse.Ok(response.body())
          404 -> run {
            val ct = response.contentType()
            when {
              ct?.match(ContentType.Application.Json) == true -> JsonNotFound(response.body())
              ct?.match(ContentType("application", "scim+json")) == true -> ScimJsonNotFound(response.body())
              else -> JsonNotFound(response.body())
            }
          }
          else -> throw ResponseException(response, "")
        }
      }

      public suspend fun xml(): XmlResponse {
        val response = client.get("/pets/$petId") {
          `header`(HttpHeaders.Accept, ContentType.Application.Xml)
        }
        return when (response.status.value) {
          200 -> XmlResponse.Ok(response.body())
          404 -> run {
            val ct = response.contentType()
            when {
              ct?.match(ContentType.Application.Json) == true -> JsonNotFound(response.body())
              ct?.match(ContentType("application", "scim+json")) == true -> ScimJsonNotFound(response.body())
              else -> JsonNotFound(response.body())
            }
          }
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface JsonResponse {
        public data class Ok(
          public val `value`: String,
        ) : JsonResponse
      }

      public sealed interface XmlResponse {
        public data class Ok(
          public val `value`: String,
        ) : XmlResponse
      }

      public data class JsonNotFound(
        public val `value`: Int,
      ) : JsonResponse,
          XmlResponse

      public data class ScimJsonNotFound(
        public val `value`: String,
      ) : JsonResponse,
          XmlResponse
    }
  }
}
