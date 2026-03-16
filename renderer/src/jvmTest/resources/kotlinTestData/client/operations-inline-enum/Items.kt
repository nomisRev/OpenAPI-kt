package io.github.nomisrev.render.test.client.operations.`inline`.`enum`

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public interface Items {
  public suspend fun `get`(status: Status? = Status.All)

  @Serializable
  public enum class Status {
    @SerialName("active")
    Active,
    @SerialName("archived")
    Archived,
    @SerialName("all")
    All,
  }
}

internal class KtorItems(
  private val client: HttpClient,
) : Items {
  override suspend fun `get`(status: Items.Status?) {
    client.get("/items") {
      status?.let { parameter("status", it) }
    }
  }
}
