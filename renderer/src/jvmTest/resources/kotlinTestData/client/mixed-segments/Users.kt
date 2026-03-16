package io.github.nomisrev.render.test.client.mixed.segments

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`

public interface Users {
  public val `get`: Get

  public interface Get {
    public suspend operator fun invoke()
  }
}

internal class KtorUsers(
  private val client: HttpClient,
) : Users {
  override val `get`: Users.Get = object : Users.Get {
    override suspend operator fun invoke() {
      client.get("/users")
    }
  }
}
