package io.github.nomisrev.render.test.client.optional.request.bodies

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

public class JsonOptionalFields internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(q: String? = null) {
      val response = client.post("json-optional-fields") {
        if (q != null) {
          this.url.encodedParameters.append("q", q.encodeURLParameter())
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public suspend operator fun invoke(
      q: String? = null,
      name: String? = null,
      note: String? = null,
    ) {
      val response = client.post("json-optional-fields") {
        if (q != null) {
          this.url.encodedParameters.append("q", q.encodeURLParameter())
        }
        contentType(ContentType.Application.Json)
        setBody(Body(name = name, note = note))
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    internal data class Body(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val note: String? = null,
    )
  }
}
