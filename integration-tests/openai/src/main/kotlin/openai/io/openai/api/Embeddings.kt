package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.openai.model.CreateEmbeddingRequest
import io.openai.model.CreateEmbeddingResponse

public class Embeddings internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: CreateEmbeddingRequest): CreateEmbeddingResponse = client.post("/embeddings") {
      contentType(ContentType.Application.Json)
      setBody(body)
    }.body()
  }
}
