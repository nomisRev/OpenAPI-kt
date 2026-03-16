package io.github.nomisrev.render.test.client.operations.body.json

public interface Settings {
  public suspend fun patch(body: UpdateSettingsRequest? = null)
}
