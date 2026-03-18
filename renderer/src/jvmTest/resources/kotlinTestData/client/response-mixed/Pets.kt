package io.github.nomisrev.render.test.client.response.mixed

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.http.HttpStatusCode
import kotlin.Int
import kotlin.String

public interface Pets {
  public fun petId(petId: String): PetIdPath

  public interface PetIdPath {
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
  override fun petId(petId: String): Pets.PetIdPath = KtorPetsPetIdPath(client, petId)
}

internal class KtorPetsPetIdPath(
  private val client: HttpClient,
  private val petId: String,
) : Pets.PetIdPath {
  override val `get`: Pets.PetIdPath.Get = object : Pets.PetIdPath.Get {
    override suspend operator fun invoke(): Pets.PetIdPath.Get.Response {
      val response = client.get("/pets/$petId")
      return when (response.status.value) {
        200 -> Pets.PetIdPath.Get.Response.Ok(response.body())
        204 -> Pets.PetIdPath.Get.Response.NoContent
        404 -> Pets.PetIdPath.Get.Response.NotFound(response.body())
        else -> Pets.PetIdPath.Get.Response.Default(response.status, response.body())
      }
    }
  }
}
