package io.github.nomisrev.render.test.client.form.urlencoded.encoding.defaults

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import kotlin.Long
import kotlin.String
import kotlin.collections.List

public class Tags internal constructor(
  private val __client: HttpClient,
) {
  public val post: Post = Post(__client)

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(
      tags: List<String>,
      labels: List<String>? = null,
      ids: List<Long>? = null,
    ) {
      val response = __client.post("tags") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(Parameters.build {
          appendAll("tags", tags)
          if (labels != null) {
            appendAll("labels", labels)
          }
          if (!ids.isNullOrEmpty()) {
            append("ids", ids.joinToString(","))
          }
        }.formUrlEncode())
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
