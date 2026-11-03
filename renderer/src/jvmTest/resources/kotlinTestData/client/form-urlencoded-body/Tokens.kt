package io.github.nomisrev.render.test.client.form.urlencoded.body

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Tokens internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      grantType: Body.GrantType,
      code: String,
      redirectUri: String,
    ) {
      val response = client.post("/tokens") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(Parameters.build {
          append("grant_type", grantType.value)
          append("code", code)
          append("redirect_uri", redirectUri)
        }.formUrlEncode())
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public object Body {
      @Serializable
      public enum class GrantType(
        public val `value`: String,
      ) {
        @SerialName("authorization_code")
        AuthorizationCode("authorization_code"),
        @SerialName("refresh_token")
        RefreshToken("refresh_token"),
        ;
      }
    }
  }
}
