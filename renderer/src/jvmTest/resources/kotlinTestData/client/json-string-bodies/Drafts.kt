package io.github.nomisrev.render.test.client.json.string.bodies

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String
import kotlinx.serialization.json.JsonPrimitive

public class Drafts internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: String, requestType: RequestType) {
      client.post("/drafts") {
        contentType(requestType.contentType)
        when (requestType) {
          RequestType.Json -> setBody(JsonPrimitive(body))
          RequestType.TextPlain -> setBody(body)
        }
      }
    }

    public enum class RequestType(
      public val contentType: ContentType,
    ) {
      Json(ContentType.Application.Json),
      TextPlain(ContentType.Text.Plain),
      ;
    }
  }
}
