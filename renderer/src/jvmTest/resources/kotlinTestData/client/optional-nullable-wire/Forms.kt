package io.github.nomisrev.render.test.client.optional.nullable.wire

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import kotlin.String

public class Forms internal constructor(
  private val __client: HttpClient,
) {
  public val post: Post = Post(__client)

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(nickname: String? = null, filter: Filter? = null) {
      val response = __client.post("forms") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(Parameters.build {
          if (nickname != null) {
            append("nickname", nickname)
          }
          if (filter != null) {
            filter.q.valueOrNull?.let { filterQValue ->
              append("filter[q]", filterQValue)
            }
            if (filter.limit != null) {
              append("filter[limit]", filter.limit.toString())
            }
          }
        }.formUrlEncode())
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
