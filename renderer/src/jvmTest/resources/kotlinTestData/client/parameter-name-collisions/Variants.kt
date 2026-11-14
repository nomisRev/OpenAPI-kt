package io.github.nomisrev.render.test.client.parameter.name.collisions

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.String
import kotlin.collections.List

public class Variants internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      body: List<String>,
      bodyRequestType: RequestType,
      requestType: String? = null,
    ) {
      val response = client.post("variants") {
        if (requestType != null) {
          this.url.encodedParameters.append("requestType", requestType.encodeURLParameter())
        }
        contentType(bodyRequestType.contentType)
        setBody(body)
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public enum class RequestType(
      public val contentType: ContentType,
    ) {
      Json(ContentType.Application.Json),
      Xml(ContentType.Application.Xml),
      ;
    }
  }
}
