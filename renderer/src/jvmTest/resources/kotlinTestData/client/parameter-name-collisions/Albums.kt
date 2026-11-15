package io.github.nomisrev.render.test.client.parameter.name.collisions

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

public class Albums internal constructor(
  private val __client: HttpClient,
) {
  public val put: Put = Put(__client)

  public class Put internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(ids: String) {
      val response = __client.put("albums") {
        this.url.encodedParameters.append("ids", ids.encodeURLParameter())
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public suspend operator fun invoke(
      ids: String,
      bodyIds: List<String>? = null,
      position: Int? = null,
    ) {
      val response = __client.put("albums") {
        this.url.encodedParameters.append("ids", ids.encodeURLParameter())
        contentType(ContentType.Application.Json)
        setBody(Body(ids = bodyIds, position = position))
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    internal data class Body(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val ids: List<String>? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val position: Int? = null,
    )
  }
}
