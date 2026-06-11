package io.github.nomisrev.render.test.`object`.plain.model.no.read.suffix

import io.github.nomisrev.render.test.object_.plain.model.no.read.suffix.IssueFolderRead
import io.github.nomisrev.render.test.object_.plain.model.no.read.suffix.IssueFolderWrite
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String

public class Folders internal constructor(
  private val client: HttpClient,
) {
  public fun id(id: String): IdPath = IdPath(client, id)

  public class IdPath internal constructor(
    private val client: HttpClient,
    private val id: String,
  ) {
    public val post: Post = Post(client, id)

    public class Post internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke(body: IssueFolderWrite): IssueFolderRead = client.post("/folders/$id") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()
    }
  }
}
