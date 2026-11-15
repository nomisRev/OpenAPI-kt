package io.github.nomisrev.render.test.client.media.type.without.schema

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.utils.EmptyContent
import io.ktor.http.ContentType
import io.ktor.http.content.OutgoingContent
import io.ktor.http.contentType
import kotlin.String
import kotlin.Unit
import kotlinx.serialization.json.JsonPrimitive

public class UploadPairOptional internal constructor(
  private val __client: HttpClient,
) {
  public val post: Post = Post(__client)

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(body: String? = null) {
      val response = __client.post("upload-pair-optional") {
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(JsonPrimitive(it))
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public suspend operator fun invoke(block: HttpRequestBuilder.() -> Unit) {
      val response = __client.post("upload-pair-optional") {
        block()
        if (this.body !is EmptyContent && this.contentType() == null && (this.body as? OutgoingContent)?.contentType == null) {
          this.contentType(ContentType.Application.Pdf)
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
