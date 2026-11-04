package io.github.nomisrev.render.test.client.wildcard.media.types

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.ByteArray

public class Files internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: ByteArray) {
      val response = client.post("files") {
        contentType(ContentType.Application.OctetStream)
        setBody(body)
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
