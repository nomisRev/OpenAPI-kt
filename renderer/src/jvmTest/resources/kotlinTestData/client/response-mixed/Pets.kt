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
    public val `get`: Get

    public interface Get {
      public suspend operator fun invoke(): Response

      public sealed interface Response {
        public data class Ok(
          public val `value`: String,
        ) : Response

        public data object NoContent : Response

        public data class NotFound(
          public val `value`: Int,
        ) : Response

        public data class Default(
          public val status: HttpStatusCode,
          public val `value`: String,
        ) : Response
      }
    }
  }
}

internal class KtorPets(
  private val client: HttpClient,
) : Pets {
  override fun petId(petId: String): Pets.PetId = KtorPetsPetId(client, petId)
}

internal class KtorPetsPetId(
  private val client: HttpClient,
  private val petId: String,
) : Pets.PetId {
  override val `get`: Pets.PetId.Get = object : Pets.PetId.Get {
    override suspend operator fun invoke(): Pets.PetId.Get.Response {
      val response = client.get("/pets/$petId")
      return when (response.status.value) {
        200 -> Pets.PetId.Get.Response.Ok(response.body())
        204 -> Pets.PetId.Get.Response.NoContent
        404 -> Pets.PetId.Get.Response.NotFound(response.body())
        else -> Pets.PetId.Get.Response.Default(response.status, response.body())
      }
    }
  }
}
