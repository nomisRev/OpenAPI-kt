package io.github.nomisrev.render.test.client.`inline`.oneof.request.body.nested.path

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Long
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
      public suspend operator fun invoke(body: RepositoryIdAndRef): Response {
        val value: String = client.post("/user/codespaces") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }.body()
        return Response(value)
      }

      public suspend operator fun invoke(body: PullRequest): Response {
        val value: String = client.post("/user/codespaces") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }.body()
        return Response(value)
      }

      @Serializable
      public data class RepositoryIdAndRef(
        @SerialName("repository_id")
        public val repositoryId: Long,
        public val ref: String? = null,
      )

      @JvmInline
      @Serializable
      public value class PullRequest(
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

      public data class Response(
        public val `value`: String,
      )
    }
  }
}
