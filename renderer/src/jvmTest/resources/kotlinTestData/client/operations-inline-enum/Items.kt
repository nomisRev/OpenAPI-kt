package io.github.nomisrev.render.test.client.operations.`inline`.`enum`

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Items internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(status: Status? = Status.All) {
      val response = client.get("items") {
        if (status != null) {
          url.encodedParameters.append("status", status.value.encodeURLParameter())
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
    }

    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("active")
      Active("active"),
      @SerialName("archived")
      Archived("archived"),
      @SerialName("all")
      All("all"),
      ;
    }
  }
}
