package io.github.api

import io.github.model.BasicError
import io.github.model.Event
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Events internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(perPage: Long? = 15L, page: Long? = 1L): Response {
      val response = client.get("/events") {
        perPage?.let { parameter("per_page", it) }
        page?.let { parameter("page", it) }
      }
      return when (response.status.value) {
        200 -> Response.Ok(response.body())
        304 -> Response.NotModified
        403 -> Response.Forbidden(response.body())
        503 -> response.body<Response.ServiceUnavailable>()
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface Response {
      public data class Ok(
        public val `value`: List<Event>,
      ) : Response

      public data object NotModified : Response

      public data class Forbidden(
        public val `value`: BasicError,
      ) : Response

      @Serializable
      public data class ServiceUnavailable(
        public val code: String? = null,
        public val message: String? = null,
        @SerialName("documentation_url")
        public val documentationUrl: String? = null,
      ) : Response
    }
  }
}
