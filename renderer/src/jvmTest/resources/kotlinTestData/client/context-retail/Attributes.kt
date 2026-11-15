package io.github.nomisrev.render.test.client.context.retail

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

public class Attributes internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public val post: Post = Post(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): Response {
      val response = __client.get("attributes")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Response(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val attribute: AttributeRead? = null,
    )
  }

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(body: AttributeWrite) {
      val response = __client.post("attributes") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
