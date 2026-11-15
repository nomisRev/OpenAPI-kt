package io.github.nomisrev.render.test.client.state.name.collision

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post

public class Client internal constructor(
  private val __client: HttpClient,
) {
  public val register: Register = Register(__client)

  public class Register internal constructor(
    private val __client: HttpClient,
  ) {
    public val post: Post = Post(__client)

    public class Post internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.post("client/register")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
