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

public class Notes internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(text: String) {
      val response = client.post("/notes") {
        contentType(ContentType.Application.FormUrlEncoded.withParameter("charset", "utf-8"))
        setBody(Parameters.build {
          append("text", text)
        }.formUrlEncode())
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
