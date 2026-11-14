package io.github.nomisrev.render.test.client.parameter.name.collisions

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import io.ktor.http.formUrlEncode
import kotlin.String

public class Forms internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      title: String,
      bodyTitle: String,
      note: String? = null,
    ) {
      val response = client.post("forms") {
        this.url.encodedParameters.append("title", title.encodeURLParameter())
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(Parameters.build {
          append("title", bodyTitle)
          if (note != null) {
            append("note", note)
          }
        }.formUrlEncode())
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
