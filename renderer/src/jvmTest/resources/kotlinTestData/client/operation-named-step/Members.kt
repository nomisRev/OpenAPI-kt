package io.github.nomisrev.render.test.client.operation.named.step

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.post

public class Members internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public val post: Post = Post(__client)

  public val delete: Delete = Delete(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public val post: Post = Post(__client)

    public suspend operator fun invoke() {
      val response = __client.get("members")
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public class Post internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.post("members/get")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = __client.post("members")
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }

  public class Delete internal constructor(
    private val __client: HttpClient,
  ) {
    public val post: Post = Post(__client)

    public class Post internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.post("members/delete")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
