package io.github.nomisrev.render.test.client.response.single

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import kotlin.String
import kotlin.collections.List

public interface Pets {
  public val `get`: Get

  public interface Get {
    public suspend operator fun invoke(): Response

    public data class Response(
      public val `value`: List<String>,
    )
  }
}

internal class KtorPets(
  private val client: HttpClient,
) : Pets {
  override val `get`: Pets.Get = object : Pets.Get {
    override suspend operator fun invoke(): Pets.Get.Response {
      val value: List<String> = client.get("/pets").body()
      return Pets.Get.Response(value)
    }
  }
}
