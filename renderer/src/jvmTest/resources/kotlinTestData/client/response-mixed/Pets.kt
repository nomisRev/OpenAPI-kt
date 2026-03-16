package io.github.nomisrev.render.test.client.response.mixed

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.http.HttpStatusCode
import kotlin.Int
import kotlin.String

public interface Pets {
  public fun petId(petId: String): PetId

  public interface PetId {
    public suspend fun `get`(): GetResult

    public sealed interface GetResult {
      public data class Ok(
        public val `value`: String,
      ) : GetResult

      public data object NoContent : GetResult

      public data class NotFound(
        public val `value`: Int,
      ) : GetResult

      public data class Default(
        public val status: HttpStatusCode,
        public val `value`: String,
      ) : GetResult
    }
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
  override suspend fun `get`(): Pets.PetId.GetResult {
    val response = client.get("/pets/$petId")
    return when (response.status.value) {
      200 -> Pets.PetId.GetResult.Ok(response.body())
      204 -> Pets.PetId.GetResult.NoContent
      404 -> Pets.PetId.GetResult.NotFound(response.body())
      else -> Pets.PetId.GetResult.Default(response.status, response.body())
    }
  }
}
