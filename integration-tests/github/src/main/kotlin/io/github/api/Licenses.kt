package io.github.api

import io.github.model.BasicError
import io.github.model.License
import io.github.model.LicenseSimple
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List

public class Licenses internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public fun license(license: String): LicensePath = LicensePath(client, license)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      featured: Boolean? = null,
      perPage: Long? = 30L,
      page: Long? = 1L,
    ): Response {
      val response = client.get("licenses") {
        if (featured != null) {
          this.url.encodedParameters.append("featured", featured.toString())
        }
        if (perPage != null) {
          this.url.encodedParameters.append("per_page", perPage.toString())
        }
        if (page != null) {
          this.url.encodedParameters.append("page", page.toString())
        }
      }
      return when (response.status.value) {
        200 -> Response.Ok(response.body())
        304 -> Response.NotModified
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface Response {
      public data class Ok(
        public val `value`: List<LicenseSimple>,
      ) : Response

      public data object NotModified : Response
    }
  }

  public class LicensePath internal constructor(
    private val client: HttpClient,
    private val license: String,
  ) {
    public val `get`: Get = Get(client, license)

    public class Get internal constructor(
      private val client: HttpClient,
      private val license: String,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.get("licenses/${this.license.encodeURLParameter()}")
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          304 -> Response.NotModified
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: License,
        ) : Response

        public data object NotModified : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }
  }
}
