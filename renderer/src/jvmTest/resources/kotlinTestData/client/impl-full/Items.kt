package io.github.nomisrev.render.test.client.`impl`.full

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public interface Items {
  public val `get`: Get

  public val post: Post

  public interface Get {
    public suspend operator fun invoke(limit: Int? = null): Response

    public data class Response(
      public val `value`: List<String>,
    )
  }

  public interface Post {
    public suspend operator fun invoke(body: String): Response

    public sealed interface Response {
      public data class Created(
        public val `value`: String,
      ) : Response

      public data class BadRequest(
        public val `value`: Int,
      ) : Response
    }
  }
}

internal class KtorItems(
  private val client: HttpClient,
) : Items {
  override val `get`: Items.Get = object : Items.Get {
    override suspend operator fun invoke(limit: Int?): Items.Get.Response {
      val value: List<String> = client.get("/items") {
        limit?.let { parameter("limit", it) }
      }.body()
      return Items.Get.Response(value)
    }
  }

  override val post: Items.Post = object : Items.Post {
    override suspend operator fun invoke(body: String): Items.Post.Response {
      val response = client.post("/items") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      return when (response.status.value) {
        201 -> Items.Post.Response.Created(response.body())
        400 -> Items.Post.Response.BadRequest(response.body())
        else -> throw ResponseException(response, "")
      }
    }
  }
}
