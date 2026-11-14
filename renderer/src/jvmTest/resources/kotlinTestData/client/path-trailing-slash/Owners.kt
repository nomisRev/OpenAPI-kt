package io.github.nomisrev.render.test.client.path.trailing.slash

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
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
import kotlinx.serialization.json.JsonPrimitive

public class Owners internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  public fun id(id: String): IdPath = IdPath(client, id)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): Response {
      val response = client.get("owners/")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Response(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: String? = null,
    )
  }

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: String) {
      val response = client.post("owners/") {
        contentType(ContentType.Application.Json)
        setBody(JsonPrimitive(body))
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }

  public class IdPath internal constructor(
    private val client: HttpClient,
    private val id: String,
  ) {
    public val `get`: Get = Get(client, id)

    public class Get internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("owners/${this.id.encodeURLParameter()}/")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
