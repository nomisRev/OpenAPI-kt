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

public interface User {
  public val codespaces: Codespaces

  public interface Codespaces {
    public val post: Post

    public interface Post {
      public suspend operator fun invoke(body: RepositoryIdAndRef): Response

      public suspend operator fun invoke(body: PullRequest): Response

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

internal class KtorUser(
  private val client: HttpClient,
) : User {
  override val codespaces: User.Codespaces = KtorUserCodespaces(client)
}

internal class KtorUserCodespaces(
  private val client: HttpClient,
) : User.Codespaces {
  override val post: User.Codespaces.Post = object : User.Codespaces.Post {
    override suspend operator fun invoke(body: User.Codespaces.Post.RepositoryIdAndRef): User.Codespaces.Post.Response {
      val value: String = client.post("/user/codespaces") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()
      return User.Codespaces.Post.Response(value)
    }

    override suspend operator fun invoke(body: User.Codespaces.Post.PullRequest): User.Codespaces.Post.Response {
      val value: String = client.post("/user/codespaces") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()
      return User.Codespaces.Post.Response(value)
    }
  }
}
