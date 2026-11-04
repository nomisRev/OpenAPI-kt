package io.github.nomisrev.render.test.client.`inline`.json.body.overloads

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String
import kotlinx.serialization.Serializable

public class Patch internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: Body, requestType: RequestType) {
      val response = client.post("/patch") {
        contentType(requestType.contentType)
        setBody(body)
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    @Serializable
    public data class Body(
      public val name: String,
    )

    public enum class RequestType(
      public val contentType: ContentType,
    ) {
      Json(ContentType.Application.Json),
      MergePatchJson(ContentType("application", "merge-patch+json")),
      ;
    }
  }
}
