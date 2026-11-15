package io.github.nomisrev.render.test.client.response.default

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.http.HttpStatusCode
import io.ktor.http.encodeURLParameter
import io.ktor.serialization.JsonConvertException
import kotlin.String
import kotlinx.serialization.json.JsonPrimitive

public class Pets internal constructor(
  private val __client: HttpClient,
) {
  public fun petId(petId: String): PetIdPath = PetIdPath(__client, petId)

  public class PetIdPath internal constructor(
    private val __client: HttpClient,
    private val _petId: String,
  ) {
    public val `get`: Get = Get(__client, _petId)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _petId: String,
    ) {
      public suspend operator fun invoke(): Response {
        val response = __client.get("pets/${this._petId.encodeURLParameter()}")
        return when (response.status.value) {
          200 -> Response.Ok(response.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") })
          else -> Response.Default(response.status, response.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") })
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: String,
        ) : Response

        public data class Default(
          public val status: HttpStatusCode,
          public val `value`: String,
        ) : Response
      }
    }
  }
}
