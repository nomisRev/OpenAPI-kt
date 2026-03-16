package io.github.nomisrev.render.test.client.root.operations

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`

public interface Health {
  public val `get`: Get

  public interface Get {
    public suspend operator fun invoke()
  }
}

internal class KtorHealth(
  private val client: HttpClient,
) : Health {
  override val `get`: Health.Get = object : Health.Get {
    override suspend operator fun invoke() {
      client.get("/health")
    }
  }
}
