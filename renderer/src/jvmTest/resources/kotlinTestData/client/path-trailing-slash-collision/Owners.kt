package io.github.nomisrev.render.test.client.path.trailing.slash.collision

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.post
import io.ktor.http.encodeURLParameter
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

public class Owners internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val slash: Slash = Slash(client)

  public fun id(id: String): IdPath = IdPath(client, id)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): Response {
      val response = client.get("owners")
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

  public class Slash internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

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
        public val id: String? = null,
      )
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = client.post("owners/")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
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
        val response = client.get("owners/${id.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
