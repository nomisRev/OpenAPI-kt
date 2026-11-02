package io.github.nomisrev.render.test.client.`enum`.wire.tostring

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Parameters
import io.ktor.http.formUrlEncode
import kotlinx.serialization.Serializable

public class Jobs internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(status: Status? = null) {
      val response = client.get("/jobs") {
        status?.let { parameter("status", it.toString()) }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
    }

    @Serializable
    public enum class Status {
      OPEN,
      CLOSED,
    }
  }

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(mode: Body.Mode) {
      val response = client.post("/jobs") {
        setBody(Parameters.build {
          append("mode", mode.toString())
        }.formUrlEncode())
      }
      if (response.status.value != 200) throw ResponseException(response, "")
    }

    public object Body {
      @Serializable
      public enum class Mode {
        FAST,
        SLOW,
      }
    }
  }
}
