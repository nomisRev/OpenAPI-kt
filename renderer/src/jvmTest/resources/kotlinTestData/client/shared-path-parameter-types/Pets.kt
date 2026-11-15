package io.github.nomisrev.render.test.client.shared.path.parameter.types

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.http.encodeURLParameter
import kotlin.Long
import kotlin.String

public class Pets internal constructor(
  private val __client: HttpClient,
) {
  public fun petId(petId: Long): PetIdLongPath = PetIdLongPath(__client, petId)

  public fun petId(petId: String): PetIdStringPath = PetIdStringPath(__client, petId)

  public class PetIdLongPath internal constructor(
    private val __client: HttpClient,
    private val _petId: Long,
  ) {
    public val `get`: Get = Get(__client, _petId)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _petId: Long,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("pets/${this._petId}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class PetIdStringPath internal constructor(
    private val __client: HttpClient,
    private val _petId: String,
  ) {
    public val delete: Delete = Delete(__client, _petId)

    public class Delete internal constructor(
      private val __client: HttpClient,
      private val _petId: String,
    ) {
      public suspend operator fun invoke() {
        val response = __client.delete("pets/${this._petId.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
