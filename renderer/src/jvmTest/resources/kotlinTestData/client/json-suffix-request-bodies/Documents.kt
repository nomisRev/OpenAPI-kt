package io.github.nomisrev.render.test.client.json.suffix.request.bodies

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

public class Documents internal constructor(
  private val __client: HttpClient,
) {
  public val post: Post = Post(__client)

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(title: String, draft: Boolean? = null) {
      val response = __client.post("documents") {
        contentType(ContentType("application", "vnd.api+json"))
        setBody(Body(title = title, draft = draft))
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    internal data class Body(
      public val title: String,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val draft: Boolean? = null,
    )
  }
}
