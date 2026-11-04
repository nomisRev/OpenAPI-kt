package io.github.nomisrev.render.test.client.optional.request.bodies

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import kotlin.String

public class Form internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public val put: Put = Put(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = client.post("form")
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public suspend operator fun invoke(name: String, note: String? = null) {
      val response = client.post("form") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(Parameters.build {
          append("name", name)
          if (note != null) {
            append("note", note)
          }
        }.formUrlEncode())
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }

  public class Put internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(name: String, note: String? = null) {
      val response = client.put("form") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(Parameters.build {
          append("name", name)
          if (note != null) {
            append("note", note)
          }
        }.formUrlEncode())
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
