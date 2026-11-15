package io.github.nomisrev.render.test.client.media.type.parameters

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String

public class Labels internal constructor(
  private val __client: HttpClient,
) {
  public val post: Post = Post(__client)

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(body: String, requestType: RequestType) {
      val response = __client.post("labels") {
        contentType(requestType.contentType)
        setBody(body)
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public enum class RequestType(
      public val contentType: ContentType,
    ) {
      TextPlain(ContentType.Text.Plain.withParameter("charset", "utf-8")),
      TextCsv(ContentType("text", "csv").withParameter("charset", "iso-8859-1")),
      ;
    }
  }
}
