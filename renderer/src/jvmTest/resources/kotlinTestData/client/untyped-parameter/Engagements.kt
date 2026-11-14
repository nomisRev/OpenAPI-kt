package io.github.nomisrev.render.test.client.untyped.parameter

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.json.JsonPrimitive

public class Engagements internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = client.post("engagements")
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public suspend operator fun invoke(
      to: String,
      parameters: JsonPrimitive? = null,
      tags: List<JsonPrimitive>? = null,
    ) {
      val response = client.post("engagements") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(Parameters.build {
          append("To", to)
          if (parameters != null) {
            append("Parameters", parameters.content)
          }
          if (tags != null) {
            appendAll("Tags", tags.map { it.content })
          }
        }.formUrlEncode())
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
