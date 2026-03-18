package io.github.nomisrev.render.test.client.response.unit

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import kotlin.String

public interface Pets {
  public fun petId(petId: String): PetIdPath

  public interface PetIdPath {
    public val delete: Delete

    public interface Delete {
      public suspend operator fun invoke()
    }
  }
}

internal class KtorPets(
  private val client: HttpClient,
) : Pets {
  override fun petId(petId: String): Pets.PetIdPath = KtorPetsPetIdPath(client, petId)
}

internal class KtorPetsPetIdPath(
  private val client: HttpClient,
  private val petId: String,
) : Pets.PetIdPath {
  override val delete: Pets.PetIdPath.Delete = object : Pets.PetIdPath.Delete {
    override suspend operator fun invoke() {
      client.delete("/pets/$petId")
    }
  }
}
