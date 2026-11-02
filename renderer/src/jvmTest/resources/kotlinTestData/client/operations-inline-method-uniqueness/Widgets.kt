package io.github.nomisrev.render.test.client.operations.`inline`.method.uniqueness

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

public class Widgets internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): Response = client.get("/widgets").body()

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Response(
      public val id: String,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val version: Int? = null,
    )
  }

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(name: String): Response = client.post("/widgets") {
      contentType(ContentType.Application.Json)
      setBody(Body(name = name))
    }.body()

    @Serializable
    internal data class Body(
      public val name: String,
    )

    @Serializable
    public data class Response(
      public val id: String,
      public val name: String,
    )
  }
}
