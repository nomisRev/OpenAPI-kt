package io.github.nomisrev.render.test.client.operations.body.form.ambiguous

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

public class Search internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(query: String, filters: Filters? = null) {
      val response = client.post("search") {
        contentType(ContentType.Application.Json)
        setBody(Body(query = query, filters = filters))
      }
      if (response.status.value != 200) throw ResponseException(response, "")
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Filters(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val status: String? = null,
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    internal data class Body(
      public val query: String,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val filters: Filters? = null,
    )
  }
}
