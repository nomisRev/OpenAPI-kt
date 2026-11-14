package io.github.nomisrev.render.test.client.status.code.case.names

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

public class Failing internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): Response {
      val response = client.get("failing")
      return when (response.status.value) {
        204 -> Response.NoContent
        404 -> response.body<Response.NotFound>()
        484 -> response.body<Response.Status484>()
        485 -> Response.Status485
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface Response {
      public data object NoContent : Response

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class NotFound(
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val what: String? = null,
      ) : Response

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class Status484(
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val x: String? = null,
      ) : Response

      public data object Status485 : Response
    }
  }
}
