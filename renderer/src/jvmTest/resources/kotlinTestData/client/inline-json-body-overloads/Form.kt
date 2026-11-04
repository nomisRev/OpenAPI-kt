package io.github.nomisrev.render.test.client.`inline`.json.body.overloads

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Form internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: Body) {
      val response = client.post("form") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public suspend operator fun invoke(name: String) {
      val response = client.post("form") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(Parameters.build {
          append("name", name)
        }.formUrlEncode())
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Body(
      public val name: String,
      public val kind: Kind,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val address: Address? = null,
    ) {
      @Serializable
      public data class Address(
        public val city: String,
      )

      @Serializable
      public enum class Kind(
        public val `value`: String,
      ) {
        @SerialName("small")
        Small("small"),
        @SerialName("large")
        Large("large"),
        ;
      }
    }
  }
}
