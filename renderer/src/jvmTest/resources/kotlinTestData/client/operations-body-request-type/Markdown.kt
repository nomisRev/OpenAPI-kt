package io.github.nomisrev.render.test.client.operations.body.request.type

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
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
      contentType(requestType.contentType)
      setBody(body)
    }.body()

    public suspend fun xml(body: String, requestType: RequestType): String = client.post("/markdown") {
      contentType(requestType.contentType)
      setBody(body)
    }.body()

    public sealed interface JsonResponse {
      public data class Ok(
        public val `value`: String,
      ) : JsonResponse
    }

    public sealed interface XmlResponse {
      public data class Ok(
        public val `value`: String,
      ) : XmlResponse
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
