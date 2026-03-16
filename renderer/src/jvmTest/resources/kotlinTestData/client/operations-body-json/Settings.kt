package io.github.nomisrev.render.test.client.operations.body.json

import io.ktor.client.HttpClient
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

public interface Settings {
  public suspend fun patch(body: UpdateSettingsRequest? = null)
}

internal class KtorSettings(
  private val client: HttpClient,
) : Settings {
  override suspend fun patch(body: UpdateSettingsRequest?) {
    client.patch("/settings") {
      body?.let {
        contentType(ContentType.Application.Json)
        setBody(it)
      }
    }
  }
}
