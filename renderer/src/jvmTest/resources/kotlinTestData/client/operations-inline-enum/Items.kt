package io.github.nomisrev.render.test.client.operations.`inline`.`enum`

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
