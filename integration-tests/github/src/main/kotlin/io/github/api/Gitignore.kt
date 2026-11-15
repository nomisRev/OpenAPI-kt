package io.github.api

import io.github.model.GitignoreTemplate
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String
import kotlin.collections.List

public class Gitignore internal constructor(
  private val __client: HttpClient,
) {
  public val templates: Templates = Templates(__client)

  public class Templates internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public fun name(name: String): NamePath = NamePath(__client, name)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(): Response {
        val response = __client.get("gitignore/templates")
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
      private val __client: HttpClient,
      private val _name: String,
    ) {
      public val `get`: Get = Get(__client, _name)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _name: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = __client.get("gitignore/templates/${this._name.encodeURLParameter()}")
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
