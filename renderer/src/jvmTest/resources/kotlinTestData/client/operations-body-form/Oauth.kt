package io.github.nomisrev.render.test.client.operations.body.form

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Parameters
import io.ktor.http.formUrlEncode
import kotlin.String

public class Oauth internal constructor(
  private val client: HttpClient,
) {
  public val token: Token = Token(client)

  public class Token internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        grantType: String,
        code: String,
        redirectUri: String,
      ) {
        client.post("/oauth/token") {
          setBody(Parameters.build {
            append("grant_type", grantType)
            append("code", code)
            append("redirect_uri", redirectUri)
          }.formUrlEncode())
        }
      }
    }
  }
}
