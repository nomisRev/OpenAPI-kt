package io.github.nomisrev.render.test.client.optional.name.collision

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

public class Things internal constructor(
  private val client: HttpClient,
) {
  public val patch: Patch = Patch(client)

  public class Patch internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): Optional {
      val response = client.patch("things")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    public suspend operator fun invoke(note: OptionalProperty<String> = OptionalProperty.Absent): Optional {
      val response = client.patch("things") {
        contentType(ContentType.Application.Json)
        setBody(Body(note = note))
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    internal data class Body(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val note: OptionalProperty<String> = OptionalProperty.Absent,
    )
  }
}
