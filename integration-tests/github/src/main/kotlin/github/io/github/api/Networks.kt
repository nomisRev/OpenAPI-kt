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

public class Networks internal constructor(
  private val client: HttpClient,
) {
  public fun owner(owner: String): OwnerPath = OwnerPath(client, owner)

  public class OwnerPath internal constructor(
    private val client: HttpClient,
    private val owner: String,
  ) {
    public fun repo(repo: String): RepoPath = RepoPath(client, owner, repo)

    public class RepoPath internal constructor(
      private val client: HttpClient,
      private val owner: String,
      private val repo: String,
    ) {
      public val events: Events = Events(client, owner, repo)

      public class Events internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
            val response = client.get("/networks/$owner/$repo/events") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              301 -> Response.MovedPermanently(response.body())
              304 -> Response.NotModified
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<Event>,
            ) : Response

            public data class MovedPermanently(
              public val `value`: BasicError,
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
  }
}
