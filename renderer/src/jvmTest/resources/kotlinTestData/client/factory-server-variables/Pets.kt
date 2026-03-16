package io.github.nomisrev.render.test.client.factory.server.variables

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`

public interface Pets {
  public val `get`: Get

  public interface Get {
    public suspend operator fun invoke()
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
}
