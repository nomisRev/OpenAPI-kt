package io.github.nomisrev.render.test.client.request.builder.shadowing

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
  private val __client: HttpClient,
) {
  public val post: Post = Post(__client)

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(
      url: String,
      `file`: ByteArray,
      append: String? = null,
      formData: String? = null,
    ) {
      val response = __client.post("uploads") {
        this.url.encodedParameters.append("url", url.encodeURLParameter())
        if (formData != null) {
          this.url.encodedParameters.append("formData", formData.encodeURLParameter())
        }
        setBody(MultiPartFormDataContent(formData {
          append("file", file)
          if (append != null) {
            append("append", append)
          }
        }))
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
