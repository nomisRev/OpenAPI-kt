package io.github.api

import io.github.model.GitignoreTemplate
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.String
import kotlin.collections.List

public class Gitignore internal constructor(
  private val client: HttpClient,
) {
  public val templates: Templates = Templates(client)

  public class Templates internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public fun name(name: String): NamePath = NamePath(client, name)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.get("/gitignore/templates")
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          304 -> Response.NotModified
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<String>,
        ) : Response

        public data object NotModified : Response
      }
    }

    public class NamePath internal constructor(
      private val client: HttpClient,
      private val name: String,
    ) {
      public val `get`: Get = Get(client, name)

      public class Get internal constructor(
        private val client: HttpClient,
        private val name: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.get("/gitignore/templates/$name")
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            304 -> Response.NotModified
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: GitignoreTemplate,
          ) : Response

          public data object NotModified : Response
        }
      }
    }
  }
}
