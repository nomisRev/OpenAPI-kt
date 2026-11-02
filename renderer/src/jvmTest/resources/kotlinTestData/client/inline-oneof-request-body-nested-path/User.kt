package io.github.nomisrev.render.test.client.`inline`.oneof.request.body.nested.path

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.JsonConvertException
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive

public class User internal constructor(
  private val client: HttpClient,
) {
  public val codespaces: Codespaces = Codespaces(client)

  public class Codespaces internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: RepositoryIdAndRef): String = client.post("/user/codespaces") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") }

      public suspend operator fun invoke(body: PullRequest): String = client.post("/user/codespaces") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") }

      @Serializable
      public data class RepositoryIdAndRef(
        @SerialName("repository_id")
        public val repositoryId: Long,
        public val ref: String? = null,
      )

      @Serializable
      public data class PullRequest(
        @SerialName("pull_request")
        public val pullRequest: PullRequest,
      ) {
        @Serializable
        public data class PullRequest(
          @SerialName("pull_request_number")
          public val pullRequestNumber: Long,
          @SerialName("repository_id")
          public val repositoryId: Long,
        )
      }
    }
  }
}
