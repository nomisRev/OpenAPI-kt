package io.github.nomisrev.render.test.client.optional.request.bodies

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

public class Json internal constructor(
  private val __client: HttpClient,
) {
  public val post: Post = Post(__client)

  public val put: Put = Put(__client)

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = __client.post("json")
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public suspend operator fun invoke(name: String, note: String? = null) {
      val response = __client.post("json") {
        contentType(ContentType.Application.Json)
        setBody(Body(name = name, note = note))
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    internal data class Body(
      public val name: String,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val note: String? = null,
    )
  }

  public class Put internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(name: String, note: String? = null) {
      val response = __client.put("json") {
        contentType(ContentType.Application.Json)
        setBody(Body(name = name, note = note))
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    internal data class Body(
      public val name: String,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val note: String? = null,
    )
  }
}
