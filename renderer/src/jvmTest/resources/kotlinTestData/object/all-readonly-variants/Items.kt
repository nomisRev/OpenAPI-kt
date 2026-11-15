package io.github.nomisrev.render.test.`object`.all.readonly.variants

import io.github.nomisrev.render.test.object_.all.readonly.variants.AllReadOnlyRead
import io.github.nomisrev.render.test.object_.all.readonly.variants.AllReadOnlyWrite
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

public class Items internal constructor(
  private val __client: HttpClient,
) {
  public val post: Post = Post(__client)

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(body: AllReadOnlyWrite): AllReadOnlyRead {
      val response = __client.post("items") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
