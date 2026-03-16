package io.github.nomisrev.render.test.client.root.operations

public interface Api {
  public val health: Health

  public suspend fun `get`()
}
