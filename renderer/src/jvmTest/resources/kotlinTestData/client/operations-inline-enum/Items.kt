package io.github.nomisrev.render.test.client.operations.`inline`.`enum`

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public interface Items {
  public val `get`: Get

  @Serializable
  public enum class Status {
    @SerialName("active")
    Active,
    @SerialName("archived")
    Archived,
    @SerialName("all")
    All,
  }

  public interface Get {
    public suspend operator fun invoke(status: Status? = Status.All)
  }
}

internal class KtorItems(
  private val client: HttpClient,
) : Items {
  override val `get`: Items.Get = object : Items.Get {
    override suspend operator fun invoke(status: Items.Status?) {
      client.get("/items") {
        status?.let { parameter("status", it) }
      }
    }
  }
}
