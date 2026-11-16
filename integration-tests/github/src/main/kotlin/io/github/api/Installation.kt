package io.github.api

import io.github.model.BasicError
import io.github.model.Repository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Installation internal constructor(
  private val __client: HttpClient,
) {
  public val repositories: Repositories = Repositories(__client)

  public val token: Token = Token(__client)

  public class Repositories internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = __client.get("installation/repositories") {
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
        }
        return when (response.status.value) {
          200 -> response.body<Response.Ok>()
          304 -> Response.NotModified
          401 -> Response.Unauthorized(response.body())
          403 -> Response.Forbidden(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        public data class Ok(
          @SerialName("total_count")
          public val totalCount: Long,
          public val repositories: List<Repository>,
          @SerialName("repository_selection")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val repositorySelection: String? = null,
        ) : Response

        public data object NotModified : Response

        public data class Unauthorized(
          public val `value`: BasicError,
        ) : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response
      }
    }
  }

  public class Token internal constructor(
    private val __client: HttpClient,
  ) {
    public val delete: Delete = Delete(__client)

    public class Delete internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.delete("installation/token")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
