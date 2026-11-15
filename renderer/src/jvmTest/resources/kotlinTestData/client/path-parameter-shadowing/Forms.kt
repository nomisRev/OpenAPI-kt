package io.github.nomisrev.render.test.client.path.parameter.shadowing

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
  private val __client: HttpClient,
) {
  public fun id(id: String): IdPath = IdPath(__client, id)

  public class IdPath internal constructor(
    private val __client: HttpClient,
    private val _id: String,
  ) {
    public val post: Post = Post(__client, _id)

    public class Post internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke(id: String, title: String? = null) {
        val response = __client.post("forms/${this._id.encodeURLParameter()}") {
          contentType(ContentType.Application.FormUrlEncoded)
          setBody(Parameters.build {
            append("id", id)
            if (title != null) {
              append("title", title)
            }
          }.formUrlEncode())
        }
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
