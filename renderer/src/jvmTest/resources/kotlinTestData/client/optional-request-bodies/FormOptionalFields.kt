package io.github.nomisrev.render.test.client.optional.request.bodies

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

public class FormOptionalFields internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(q: String? = null) {
      val response = client.post("form-optional-fields") {
        if (q != null) {
          this.url.encodedParameters.append("q", q.encodeURLParameter())
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public suspend operator fun invoke(
      q: String? = null,
      name: String? = null,
      note: String? = null,
    ) {
      val response = client.post("form-optional-fields") {
        if (q != null) {
          this.url.encodedParameters.append("q", q.encodeURLParameter())
        }
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(Parameters.build {
          if (name != null) {
            append("name", name)
          }
          if (note != null) {
            append("note", note)
          }
        }.formUrlEncode())
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
