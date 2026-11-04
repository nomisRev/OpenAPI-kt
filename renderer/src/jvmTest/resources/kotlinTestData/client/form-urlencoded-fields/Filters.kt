package io.github.nomisrev.render.test.client.form.urlencoded.fields

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import kotlin.Int
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

public class Filters internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(filter: Body.Filter, range: Body.Range? = null) {
      val response = client.post("/filters") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(Parameters.build {
          append("status", filter.status.toString())
          if (filter.limit != null) {
            append("limit", filter.limit.toString())
          }
          if (range != null) {
            append("range[min]", range.min.toString())
            if (range.max != null) {
              append("range[max]", range.max.toString())
            }
          }
        }.formUrlEncode())
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public object Body {
      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class Filter(
        public val status: Status,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val limit: Int? = null,
      )

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class Range(
        public val min: Int,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val max: Int? = null,
      )
    }
  }
}
