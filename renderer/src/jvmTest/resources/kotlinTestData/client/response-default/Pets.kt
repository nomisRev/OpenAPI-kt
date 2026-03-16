package io.github.nomisrev.render.test.client.response.default

import io.ktor.http.HttpStatusCode
import kotlin.String

public interface Pets {
  public fun petId(petId: String): PetId

  public interface PetId {
    public suspend fun `get`(): GetResult

    public sealed interface GetResult {
      public data class Ok(
        public val `value`: String,
      ) : GetResult

      public data class Default(
        public val status: HttpStatusCode,
        public val `value`: String,
      ) : GetResult
    }
  }
}
