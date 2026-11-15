package io.github.nomisrev.render.test.client.shared.path.parameter.types

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.OptIn
import kotlin.String
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
    }
  }
}
