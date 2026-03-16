package io.github.nomisrev.render.test.client.operations.body.json

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

public interface Pets {
  public suspend fun post(body: CreatePetRequest)
}

internal class KtorPets(
  private val client: HttpClient,
) : Pets {
  override suspend fun post(body: CreatePetRequest) {
    client.post("/pets") {
      contentType(ContentType.Application.Json)
      setBody(body)
    }
  }
}
