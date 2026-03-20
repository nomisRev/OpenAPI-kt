package io.github.nomisrev.render.test.`object`.read.write.variants

import io.github.nomisrev.render.test.object_.read.write.variants.UserRead
import io.github.nomisrev.render.test.object_.read.write.variants.UserWrite
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

public class Users internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: UserWrite): UserRead = client.post("/users") {
      contentType(ContentType.Application.Json)
      setBody(body)
    }.body()
  }
}
