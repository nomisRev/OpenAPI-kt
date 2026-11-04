package io.github.nomisrev.render.test.client.response.unit

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.delete
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Pets internal constructor(
  private val client: HttpClient,
) {
  public fun petId(petId: String): PetIdPath = PetIdPath(client, petId)

  public class PetIdPath internal constructor(
    private val client: HttpClient,
    private val petId: String,
  ) {
    public val delete: Delete = Delete(client, petId)

    public class Delete internal constructor(
      private val client: HttpClient,
      private val petId: String,
    ) {
      public suspend operator fun invoke() {
        val response = client.delete("pets/${petId.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
