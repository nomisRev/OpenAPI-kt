package io.github.nomisrev.render.test.client.operations.deprecated

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import kotlin.Deprecated

public interface Legacy {
  @Deprecated("Deprecated by the API provider")
  public val `get`: Get

  @Deprecated("Deprecated by the API provider")
  public interface Get {
    @Deprecated("Deprecated by the API provider")
    public suspend operator fun invoke()
  }
}

internal class KtorLegacy(
  private val client: HttpClient,
) : Legacy {
  @Deprecated("Deprecated by the API provider")
  override val `get`: Legacy.Get = object : Legacy.Get {
    @Deprecated("Deprecated by the API provider")
    override suspend operator fun invoke() {
      client.get("/legacy")
    }
  }
}
