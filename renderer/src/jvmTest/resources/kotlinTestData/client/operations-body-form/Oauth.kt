package io.github.nomisrev.render.test.client.operations.body.form

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Parameters
import io.ktor.http.formUrlEncode
import kotlin.String

public interface Oauth {
  public val token: Token

  public interface Token {
    public suspend fun post(
      grantType: String,
      code: String,
      redirectUri: String,
    )
  }
}

internal class KtorOauth(
  private val client: HttpClient,
) : Oauth {
  override val token: Oauth.Token = KtorToken(client)
}

internal class KtorToken(
  private val client: HttpClient,
) : Oauth.Token {
  override suspend fun post(
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
