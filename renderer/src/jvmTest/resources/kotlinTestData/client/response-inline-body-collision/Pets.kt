package io.github.nomisrev.render.test.client.response.`inline`.body.collision

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.http.HttpStatusCode
import kotlin.Int
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

public class Pets internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): Response {
      val response = client.get("/pets")
      return when (response.status.value) {
        200 -> response.body<Response.Ok>()
        503 -> response.body<Response.ServiceUnavailable>()
        else -> Response.Default(response.status, response.body())
      }
    }

    public sealed interface Response {
      @Serializable
      public data class Ok(
        public val id: String,
        public val name: String,
      ) : Response

      @Serializable
      public data class ServiceUnavailable(
        public val message: String,
        public val retryAfter: Int,
      ) : Response

      @JvmInline
      @Serializable
      public value class DefaultBody(
        public val fallbackMessage: String,
      )

      public data class Default(
        public val status: HttpStatusCode,
        public val `value`: DefaultBody,
      ) : Response
    }
  }
}
