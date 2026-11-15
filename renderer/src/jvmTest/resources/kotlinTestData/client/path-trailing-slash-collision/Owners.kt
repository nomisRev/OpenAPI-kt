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
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public val slash: Slash = Slash(__client)

  public fun id(id: String): IdPath = IdPath(__client, id)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): Response {
      val response = __client.get("owners")
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
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public val post: Post = Post(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(): Response {
        val response = __client.get("owners/")
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
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.post("owners/")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class IdPath internal constructor(
    private val __client: HttpClient,
    private val _id: String,
  ) {
    public val `get`: Get = Get(__client, _id)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("owners/${this._id.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
