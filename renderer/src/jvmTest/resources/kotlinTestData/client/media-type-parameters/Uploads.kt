package io.github.nomisrev.render.test.client.media.type.parameters

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlin.String

public class Uploads internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(title: String) {
      val response = client.post("/uploads") {
        setBody(MultiPartFormDataContent(formData {
          append("title", title)
        }))
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
