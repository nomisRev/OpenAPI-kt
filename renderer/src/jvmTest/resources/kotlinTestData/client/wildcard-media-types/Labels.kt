package io.github.nomisrev.render.test.client.wildcard.media.types

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String
import kotlinx.serialization.json.JsonPrimitive

public class Labels internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: String, requestType: RequestType) {
      val response = client.post("/labels") {
        contentType(requestType.contentType)
        when (requestType) {
          RequestType.UnnamedUnnamed -> setBody(body)
          RequestType.Unnamed -> setBody(JsonPrimitive(body))
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public enum class RequestType(
      public val contentType: ContentType,
    ) {
      UnnamedUnnamed(ContentType.Text.Plain),
      Unnamed(ContentType.Application.Json),
      ;
    }
  }
}
