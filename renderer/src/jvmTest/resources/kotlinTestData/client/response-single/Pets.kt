package io.github.nomisrev.render.test.client.response.single

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import kotlin.String
import kotlin.collections.List

public interface Pets {
  public suspend fun `get`(): List<String>
}

internal class KtorPets(
  private val client: HttpClient,
) : Pets {
  override suspend fun `get`(): List<String> = client.get("/pets").body()
}
