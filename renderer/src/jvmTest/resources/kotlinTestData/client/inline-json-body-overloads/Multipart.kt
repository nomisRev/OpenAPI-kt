package io.github.nomisrev.render.test.client.`inline`.json.body.overloads

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.ByteArray
import kotlin.String
import kotlinx.serialization.Serializable

public class Multipart internal constructor(
  private val __client: HttpClient,
) {
  public val post: Post = Post(__client)

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(body: Body) {
      val response = __client.post("multipart") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public suspend operator fun invoke(`file`: ByteArray) {
      val response = __client.post("multipart") {
        setBody(MultiPartFormDataContent(formData {
          append("file", file)
        }))
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    @Serializable
    public data class Body(
      public val name: String,
    )
  }
}
