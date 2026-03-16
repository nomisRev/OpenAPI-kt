package io.github.nomisrev.render.test.client.operations.body.form

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
