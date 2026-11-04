package io.github.nomisrev.render.test.`object`.single.writable.`field`.variants

import io.github.nomisrev.render.test.object_.single.writable.`field`.variants.LocaleDescriptorRead
import io.github.nomisrev.render.test.object_.single.writable.`field`.variants.LocaleDescriptorWrite
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

public class Locales internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: LocaleDescriptorWrite): LocaleDescriptorRead {
      val response = client.post("locales") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
