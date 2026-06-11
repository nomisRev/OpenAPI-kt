package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.openai.model.CreateModerationRequest
import io.openai.model.CreateModerationResponse

public class Moderations internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: CreateModerationRequest): CreateModerationResponse = client.post("/moderations") {
      contentType(ContentType.Application.Json)
      setBody(body)
    }.body()
  }
}
