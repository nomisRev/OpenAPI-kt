package io.github.nomisrev.render.test.`object`.plain.model.no.read.suffix

import io.github.nomisrev.render.test.object_.plain.model.no.read.suffix.IssueFolderRead
import io.github.nomisrev.render.test.object_.plain.model.no.read.suffix.IssueFolderWrite
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Folders internal constructor(
  private val __client: HttpClient,
) {
  public fun id(id: String): IdPath = IdPath(__client, id)

  public class IdPath internal constructor(
    private val __client: HttpClient,
    private val _id: String,
  ) {
    public val post: Post = Post(__client, _id)

    public class Post internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke(body: IssueFolderWrite): IssueFolderRead {
        val response = __client.post("folders/${this._id.encodeURLParameter()}") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }
}
