package io.github.nomisrev.render.test.client.`inline`.json.body.overloads

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import kotlin.String
import kotlinx.serialization.Serializable

public class Optional internal constructor(
  private val __client: HttpClient,
) {
  public val post: Post = Post(__client)

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = __client.post("optional")
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public suspend operator fun invoke(body: Body? = null) {
      val response = __client.post("optional") {
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public suspend operator fun invoke(name: String) {
      val response = __client.post("optional") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(Parameters.build {
          append("name", name)
        }.formUrlEncode())
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    @Serializable
    public data class Body(
      public val name: String,
    )
  }
}
