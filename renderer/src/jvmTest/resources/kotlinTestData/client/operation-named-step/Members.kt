package io.github.nomisrev.render.test.client.operation.named.step

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.post

public class Members internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  public val delete: Delete = Delete(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public suspend operator fun invoke() {
      val response = client.get("members")
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = client.post("members/get")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = client.post("members")
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }

  public class Delete internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = client.post("members/delete")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
