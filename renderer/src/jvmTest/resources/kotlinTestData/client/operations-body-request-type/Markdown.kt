package io.github.nomisrev.render.test.client.operations.body.request.type

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`header`
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlin.String

public class Markdown internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend fun json(body: String, requestType: RequestType): String = client.post("/markdown") {
      `header`(HttpHeaders.Accept, ContentType.Application.Json)
      contentType(requestType.contentType)
      setBody(body)
    }.body()

    public suspend fun xml(body: String, requestType: RequestType): String = client.post("/markdown") {
      `header`(HttpHeaders.Accept, ContentType.Application.Xml)
      contentType(requestType.contentType)
      setBody(body)
    }.body()

    public enum class RequestType(
      public val contentType: ContentType,
    ) {
      TextPlain(ContentType.Text.Plain),
      TextXMarkdown(ContentType("text", "x-markdown")),
      ;
    }
  }
}
