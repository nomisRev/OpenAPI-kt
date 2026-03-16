package io.github.nomisrev.render.test.client.operations.basic

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String

public interface Pets {
  public val `get`: Get

  public val post: Post

  public interface Get {
    public suspend operator fun invoke()
  }

  public interface Post {
    public suspend operator fun invoke(body: String)
  }
}

internal class KtorPets(
  private val client: HttpClient,
) : Pets {
  override val `get`: Pets.Get = object : Pets.Get {
    override suspend operator fun invoke() {
      client.get("/pets")
    }
  }

  override val post: Pets.Post = object : Pets.Post {
    override suspend operator fun invoke(body: String) {
      client.post("/pets") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
    }
  }
}
