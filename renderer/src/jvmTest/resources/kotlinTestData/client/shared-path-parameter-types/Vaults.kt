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
  private val client: HttpClient,
) {
  public fun id(id: String): IdStringPath = IdStringPath(client, id)

  @OptIn(ExperimentalUuidApi::class)
  public fun id(id: Uuid): IdUuidPath = IdUuidPath(client, id)

  public class IdStringPath internal constructor(
    private val client: HttpClient,
    private val id: String,
  ) {
    public val `get`: Get = Get(client, id)

    public class Get internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("vaults/${this.id.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  @OptIn(ExperimentalUuidApi::class)
  public class IdUuidPath internal constructor(
    private val client: HttpClient,
    private val id: Uuid,
  ) {
    public val items: Items = Items(client, id)

    public val files: Files = Files(client, id)

    @OptIn(ExperimentalUuidApi::class)
    public class Items internal constructor(
      private val client: HttpClient,
      private val id: Uuid,
    ) {
      public val `get`: Get = Get(client, id)

      @OptIn(ExperimentalUuidApi::class)
      public class Get internal constructor(
        private val client: HttpClient,
        private val id: Uuid,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("vaults/${this.id}/items")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }

    @OptIn(ExperimentalUuidApi::class)
    public class Files internal constructor(
      private val client: HttpClient,
      private val id: Uuid,
    ) {
      public val `get`: Get = Get(client, id)

      public val put: Put = Put(client, id)

      @OptIn(ExperimentalUuidApi::class)
      public class Get internal constructor(
        private val client: HttpClient,
        private val id: Uuid,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("vaults/${this.id}/files")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }

      @OptIn(ExperimentalUuidApi::class)
      public class Put internal constructor(
        private val client: HttpClient,
        private val id: Uuid,
      ) {
        public suspend operator fun invoke(
          name: String,
          kind: Kind? = null,
          owner: Owner? = null,
        ): Response {
          val response = client.put("vaults/${this.id}/files") {
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
