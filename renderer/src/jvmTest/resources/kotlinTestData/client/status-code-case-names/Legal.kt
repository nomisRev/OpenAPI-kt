package io.github.nomisrev.render.test.client.status.code.case.names

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.serialization.JsonConvertException
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive

public class Legal internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): Response {
      val response = __client.get("legal")
      return when (response.status.value) {
        200 -> Response.Ok(response.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") })
        207 -> response.body<Response.MultiStatus>()
        451 -> response.body<Response.UnavailableForLegalReasons>()
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface Response {
      public data class Ok(
        public val `value`: String,
      ) : Response

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class MultiStatus(
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val items: String? = null,
      ) : Response

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class UnavailableForLegalReasons(
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val reason: String? = null,
      ) : Response
    }
  }
}
