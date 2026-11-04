package io.github.nomisrev.render.test.client.path.separator.collision

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.post

public class SecretScanning2 internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = client.get("secret_scanning")
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = client.post("secret_scanning")
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
