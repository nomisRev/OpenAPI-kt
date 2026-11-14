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
  private val client: HttpClient,
) {
  public fun id(id: String): IdPath = IdPath(client, id)

  public class IdPath internal constructor(
    private val client: HttpClient,
    private val id: String,
  ) {
    public val post: Post = Post(client, id)

    public class Post internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke(id: String, title: String? = null) {
        val response = client.post("forms/${this.id.encodeURLParameter()}") {
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
