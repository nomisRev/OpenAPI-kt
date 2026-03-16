package io.github.nomisrev.render.test.client.operations.body.json

import io.ktor.client.HttpClient
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

public interface Settings {
  public val patch: Patch

  public interface Patch {
    public suspend operator fun invoke(body: UpdateSettingsRequest? = null)
  }
}

internal class KtorSettings(
  private val client: HttpClient,
) : Settings {
  override val patch: Settings.Patch = object : Settings.Patch {
    override suspend operator fun invoke(body: UpdateSettingsRequest?) {
      client.patch("/settings") {
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }
    }
  }
}
