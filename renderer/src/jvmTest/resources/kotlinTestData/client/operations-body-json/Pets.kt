package io.github.nomisrev.render.test.client.operations.body.json

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

public interface Pets {
  public val post: Post

  public interface Post {
    public suspend operator fun invoke(body: CreatePetRequest)
  }
}

internal class KtorPets(
  private val client: HttpClient,
) : Pets {
  override val post: Pets.Post = object : Pets.Post {
    override suspend operator fun invoke(body: CreatePetRequest) {
      client.post("/pets") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
    }
  }
}
