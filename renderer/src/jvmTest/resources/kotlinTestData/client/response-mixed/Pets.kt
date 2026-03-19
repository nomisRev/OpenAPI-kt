package io.github.nomisrev.render.test.client.response.mixed

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.http.HttpStatusCode
import kotlin.Int
import kotlin.String

public class Pets internal constructor(
  private val client: HttpClient,
) {
  public fun petId(petId: String): PetIdPath = PetIdPath(client, petId)

  public class PetIdPath internal constructor(
    private val client: HttpClient,
    private val petId: String,
  ) {
    public val `get`: Get = Get(client, petId)

    public class Get internal constructor(
      private val client: HttpClient,
      private val petId: String,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.get("/pets/$petId")
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          204 -> Response.NoContent
          404 -> Response.NotFound(response.body())
          else -> Response.Default(response.status, response.body())
        }
      }

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
