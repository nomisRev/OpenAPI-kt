package io.github.nomisrev.render.test.client.shared.path.parameter.types

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.http.encodeURLParameter
import kotlin.Long
import kotlin.String

public class Pets internal constructor(
  private val client: HttpClient,
) {
  public fun petId(petId: Long): PetIdLongPath = PetIdLongPath(client, petId)

  public fun petId(petId: String): PetIdStringPath = PetIdStringPath(client, petId)

  public class PetIdLongPath internal constructor(
    private val client: HttpClient,
    private val petId: Long,
  ) {
    public val `get`: Get = Get(client, petId)

    public class Get internal constructor(
      private val client: HttpClient,
      private val petId: Long,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("pets/${this.petId}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class PetIdStringPath internal constructor(
    private val client: HttpClient,
    private val petId: String,
  ) {
    public val delete: Delete = Delete(client, petId)

    public class Delete internal constructor(
      private val client: HttpClient,
      private val petId: String,
    ) {
      public suspend operator fun invoke() {
        val response = client.delete("pets/${this.petId.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
