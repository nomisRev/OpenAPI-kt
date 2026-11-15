package io.github.nomisrev.render.test.client.operation.named.step

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

public class Standards internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public val post: Post = Post(__client)

    public suspend operator fun invoke(limit: Int? = null): Response {
      val response = __client.get("standards") {
        if (limit != null) {
          this.url.encodedParameters.append("limit", limit.toString())
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    public class Post internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(ids: List<String>): Response {
        val response = __client.post("standards/get") {
          contentType(ContentType.Application.Json)
          setBody(Body(ids = ids))
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }

      @Serializable
      internal data class Body(
        public val ids: List<String>,
      )

      @Serializable
      public data class Response(
        public val names: List<String>,
      )
    }

    @Serializable
    public data class Response(
      public val count: Int,
    )
  }
}
