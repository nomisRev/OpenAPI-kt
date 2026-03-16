package io.github.nomisrev.render.test.client.response.unit

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import kotlin.String

public interface Pets {
  public fun petId(petId: String): PetId

  public interface PetId {
    public suspend fun delete()
  }
}

internal class KtorPets(
  private val client: HttpClient,
) : Pets {
  override fun petId(petId: String): Pets.PetId = KtorPetId(client, petId)
}

internal class KtorPetId(
  private val client: HttpClient,
  private val petId: String,
) : Pets.PetId {
  override suspend fun delete() {
    client.delete("/pets/$petId")
  }
}
