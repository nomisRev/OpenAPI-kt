package io.github.nomisrev.render.test.client.path.identifier.collision

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

public class Users internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): Response {
      val response = __client.get("Users")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Response(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: String? = null,
    )
  }
}
