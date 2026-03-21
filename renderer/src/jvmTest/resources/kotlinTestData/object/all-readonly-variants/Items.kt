package io.github.nomisrev.render.test.`object`.all.readonly.variants

import io.github.nomisrev.render.test.object_.all.readonly.variants.AllReadOnlyRead
import io.github.nomisrev.render.test.object_.all.readonly.variants.AllReadOnlyWrite
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

public class Items internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: AllReadOnlyWrite): AllReadOnlyRead = client.post("/items") {
      contentType(ContentType.Application.Json)
      setBody(body)
    }.body()
  }
}
