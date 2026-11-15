package io.github.nomisrev.render.test.client.operations.body.request.type

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`header`
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.JsonConvertException
import kotlin.String
import kotlinx.serialization.json.JsonPrimitive

public class Markdown internal constructor(
  private val __client: HttpClient,
) {
  public val post: Post = Post(__client)

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend fun json(body: String, requestType: RequestType): String {
      val response = __client.post("markdown") {
        `header`(HttpHeaders.Accept, ContentType.Application.Json)
        contentType(requestType.contentType)
        setBody(body)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") }
    }

    public suspend fun xml(body: String, requestType: RequestType): String {
      val response = __client.post("markdown") {
        `header`(HttpHeaders.Accept, ContentType.Application.Xml)
        contentType(requestType.contentType)
        setBody(body)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    public enum class RequestType(
      public val contentType: ContentType,
    ) {
      TextPlain(ContentType.Text.Plain),
      TextXMarkdown(ContentType("text", "x-markdown")),
      ;
    }
  }
}
