package io.github.nomisrev.render.test.client.optional.request.bodies

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlin.ByteArray
import kotlin.String

public class Multipart internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public val put: Put = Put(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = client.post("/multipart")
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public suspend operator fun invoke(`file`: ByteArray, note: String? = null) {
      val response = client.post("/multipart") {
        setBody(MultiPartFormDataContent(formData {
          append("file", file)
          if (note != null) {
            append("note", note)
          }
        }))
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }

  public class Put internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(`file`: ByteArray, note: String? = null) {
      val response = client.put("/multipart") {
        setBody(MultiPartFormDataContent(formData {
          append("file", file)
          if (note != null) {
            append("note", note)
          }
        }))
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
