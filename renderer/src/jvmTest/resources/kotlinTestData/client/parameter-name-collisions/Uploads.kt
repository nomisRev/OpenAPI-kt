package io.github.nomisrev.render.test.client.parameter.name.collisions

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.encodeURLParameter
import kotlin.ByteArray
import kotlin.String

public class Uploads internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      name: String,
      bodyName: String,
      `file`: ByteArray,
    ) {
      val response = client.post("uploads") {
        this.url.encodedParameters.append("name", name.encodeURLParameter())
        setBody(MultiPartFormDataContent(formData {
          append("name", bodyName)
          append("file", file)
        }))
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
