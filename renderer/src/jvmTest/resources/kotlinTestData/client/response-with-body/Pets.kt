package io.github.nomisrev.render.test.client.response.with.body

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Int
import kotlin.String

public interface Pets {
  public val post: Post

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

internal class KtorPets(
  private val client: HttpClient,
) : Pets {
  override val post: Pets.Post = object : Pets.Post {
    override suspend operator fun invoke(body: String): Pets.Post.Response {
      val response = client.post("/pets") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      return when (response.status.value) {
        201 -> Pets.Post.Response.Created(response.body())
        400 -> Pets.Post.Response.BadRequest(response.body())
        else -> throw ResponseException(response, "")
      }
    }
  }
}
