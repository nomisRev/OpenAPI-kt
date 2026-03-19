package io.github.nomisrev.render.test.client.operations.`inline`.shared.parameter

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Items internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  @Serializable
  public enum class Status {
    @SerialName("active")
    Active,
    @SerialName("archived")
    Archived,
    @SerialName("all")
    All,
  }

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(status: Status? = Status.All) {
      client.get("/items") {
        status?.let { parameter("status", it) }
      }
    }
  }

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(status: Status? = Status.All) {
      client.post("/items") {
        status?.let { parameter("status", it) }
      }
    }
  }
}
