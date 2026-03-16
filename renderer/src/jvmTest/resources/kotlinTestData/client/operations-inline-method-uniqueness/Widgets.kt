package io.github.nomisrev.render.test.client.operations.`inline`.method.uniqueness

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Int
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

public interface Widgets {
  public val `get`: Get

  public val post: Post

  public interface Get {
    public suspend operator fun invoke(): Response

    @Serializable
    public data class Response(
      public val id: String,
      public val version: Int? = null,
    )
  }

  public interface Post {
    public suspend operator fun invoke(body: Body): Response

    @JvmInline
    @Serializable
    public value class Body(
      public val name: String,
    )

    @Serializable
    public data class Response(
      public val id: String,
      public val name: String,
    )
  }
}

internal class KtorWidgets(
  private val client: HttpClient,
) : Widgets {
  override val `get`: Widgets.Get = object : Widgets.Get {
    override suspend operator fun invoke(): Widgets.Get.Response = client.get("/widgets").body()
  }

  override val post: Widgets.Post = object : Widgets.Post {
    override suspend operator fun invoke(body: Widgets.Post.Body): Widgets.Post.Response = client.post("/widgets") {
      contentType(ContentType.Application.Json)
      setBody(body)
    }.body()
  }
}
