package io.github.nomisrev.render.test.client.operations.dropped.on.errors

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String
import kotlinx.serialization.Serializable

public class Mixed internal constructor(
  private val __client: HttpClient,
) {
  public val post: Post = Post(__client)

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(a: String) {
      val response = __client.post("mixed") {
        contentType(ContentType.Application.Json)
        setBody(Body(a = a))
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    @Serializable
    internal data class Body(
      public val a: String,
    )
  }
}
