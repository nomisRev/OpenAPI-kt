package io.github.nomisrev.render.test.client.shared.path.parameter.types

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.OptIn
import kotlin.String
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Vaults internal constructor(
  private val __client: HttpClient,
) {
  public fun id(id: String): IdStringPath = IdStringPath(__client, id)

  @OptIn(ExperimentalUuidApi::class)
  public fun id(id: Uuid): IdUuidPath = IdUuidPath(__client, id)

  public class IdStringPath internal constructor(
    private val __client: HttpClient,
    private val _id: String,
  ) {
    public val `get`: Get = Get(__client, _id)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("vaults/${this._id.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  @OptIn(ExperimentalUuidApi::class)
  public class IdUuidPath internal constructor(
    private val __client: HttpClient,
    private val _id: Uuid,
  ) {
    public val items: Items = Items(__client, _id)

    public val files: Files = Files(__client, _id)

    @OptIn(ExperimentalUuidApi::class)
    public class Items internal constructor(
      private val __client: HttpClient,
      private val _id: Uuid,
    ) {
      public val `get`: Get = Get(__client, _id)

      @OptIn(ExperimentalUuidApi::class)
      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: Uuid,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("vaults/${this._id}/items")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }

    @OptIn(ExperimentalUuidApi::class)
    public class Files internal constructor(
      private val __client: HttpClient,
      private val _id: Uuid,
    ) {
      public val `get`: Get = Get(__client, _id)

      public val put: Put = Put(__client, _id)

      @OptIn(ExperimentalUuidApi::class)
      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: Uuid,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("vaults/${this._id}/files")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }

      @OptIn(ExperimentalUuidApi::class)
      public class Put internal constructor(
        private val __client: HttpClient,
        private val _id: Uuid,
      ) {
        public suspend operator fun invoke(
          name: String,
          kind: Kind? = null,
          owner: Owner? = null,
        ): Response {
          val response = __client.put("vaults/${this._id}/files") {
            contentType(ContentType.Application.Json)
            setBody(Body(name = name, kind = kind, owner = owner))
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        @Serializable
        public enum class Kind(
          public val `value`: String,
        ) {
          @SerialName("text")
          Text("text"),
          @SerialName("binary")
          Binary("binary"),
          ;
        }

        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        public data class Owner(
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val name: String? = null,
        )

        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        internal data class Body(
          public val name: String,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val kind: Kind? = null,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val owner: Owner? = null,
        )

        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        public data class Response(
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val state: State? = null,
        ) {
          @Serializable
          public enum class State(
            public val `value`: String,
          ) {
            @SerialName("stored")
            Stored("stored"),
            @SerialName("queued")
            Queued("queued"),
            ;
          }
        }
      }
    }
  }
}
