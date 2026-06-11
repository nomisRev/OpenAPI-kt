package io.github.nomisrev.render.test.client.operations.body.form.ambiguous

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

public class Search internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(query: String, filters: Filters? = null) {
      client.post("/search") {
        contentType(ContentType.Application.Json)
        setBody(Body(query = query, filters = filters))
      }
    }

    @JvmInline
    @Serializable
    public value class Filters(
      public val status: String? = null,
    )

    @Serializable
    internal data class Body(
      public val query: String,
      public val filters: Filters? = null,
    )
  }
}
