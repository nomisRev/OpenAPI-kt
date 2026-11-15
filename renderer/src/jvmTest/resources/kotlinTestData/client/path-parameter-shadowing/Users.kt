package io.github.nomisrev.render.test.client.path.parameter.shadowing

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.patch
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

public class Users internal constructor(
  private val __client: HttpClient,
) {
  public fun id(id: String): IdPath = IdPath(__client, id)

  public class IdPath internal constructor(
    private val __client: HttpClient,
    private val _id: String,
  ) {
    public val `get`: Get = Get(__client, _id)

    public val patch: Patch = Patch(__client, _id)

    public val post: Post = Post(__client, _id)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke(id: String) {
        val response = __client.get("users/${this._id.encodeURLParameter()}") {
          this.url.encodedParameters.append("id", id.encodeURLParameter())
        }
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }

    public class Patch internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke(id: String? = null, name: String? = null) {
        val response = __client.patch("users/${this._id.encodeURLParameter()}") {
          contentType(ContentType.Application.Json)
          setBody(Body(id = id, name = name))
        }
        if (response.status.value != 204) throw ResponseException(response, "")
      }

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      internal data class Body(
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val id: String? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val name: String? = null,
      )
    }

    public class Post internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke(id: String, name: String? = null) {
        val response = __client.post("users/${this._id.encodeURLParameter()}") {
          contentType(ContentType.Application.Json)
          setBody(Body(id = id, name = name))
        }
        if (response.status.value != 204) throw ResponseException(response, "")
      }

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      internal data class Body(
        public val id: String,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val name: String? = null,
      )
    }
  }
}
