package io.github.nomisrev.render.test.client.media.type.without.schema

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String
import kotlin.Unit
import kotlinx.serialization.json.JsonPrimitive

public class UploadPairOptional internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: String? = null) {
      val response = client.post("upload-pair-optional") {
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(JsonPrimitive(it))
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public suspend operator fun invoke(block: HttpRequestBuilder.() -> Unit) {
      val response = client.post("upload-pair-optional") {
        contentType(ContentType.Application.Pdf)
        block()
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
