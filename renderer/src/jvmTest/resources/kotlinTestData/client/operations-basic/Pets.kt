package io.github.nomisrev.render.test.client.operations.basic

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String

public interface Pets {
  public suspend fun `get`()

  public suspend fun post(body: String)
}

internal class KtorPets(
  private val client: HttpClient,
) : Pets {
  override suspend fun `get`() {
    client.get("/pets")
  }

  override suspend fun post(body: String) {
    client.post("/pets") {
      contentType(ContentType.Application.Json)
      setBody(body)
    }
  }
}
