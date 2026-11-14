package io.github.nomisrev.render.test.client.optional.nullable.wire

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

public class Items internal constructor(
  private val client: HttpClient,
) {
  public fun id(id: String): IdPath = IdPath(client, id)

  public class IdPath internal constructor(
    private val client: HttpClient,
    private val id: String,
  ) {
    public val patch: Patch = Patch(client, id)

    public class Patch internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke(
        name: Optional<String> = Optional.Absent,
        description: Optional<String> = Optional.Absent,
        count: Int? = null,
        settings: Settings? = null,
      ) {
        val response = client.patch("items/${this.id.encodeURLParameter()}") {
          contentType(ContentType.Application.Json)
          setBody(Body(name = name, description = description, count = count, settings = settings))
        }
        if (response.status.value != 204) throw ResponseException(response, "")
      }

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class Settings(
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val color: Optional<String> = Optional.Absent,
      )

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      internal data class Body(
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val name: Optional<String> = Optional.Absent,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val description: Optional<String> = Optional.Absent,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val count: Int? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val settings: Settings? = null,
      )
    }
  }
}
