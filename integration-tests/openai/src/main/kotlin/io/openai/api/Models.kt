package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.http.encodeURLParameter
import io.openai.model.DeleteModelResponse
import io.openai.model.ListModelsResponse
import io.openai.model.Model
import kotlin.String

public class Models internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public fun model(model: String): ModelPath = ModelPath(client, model)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): ListModelsResponse {
      val response = client.get("models")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class ModelPath internal constructor(
    private val client: HttpClient,
    private val model: String,
  ) {
    public val delete: Delete = Delete(client, model)

    public val `get`: Get = Get(client, model)

    public class Delete internal constructor(
      private val client: HttpClient,
      private val model: String,
    ) {
      public suspend operator fun invoke(): DeleteModelResponse {
        val response = client.delete("models/${this.model.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val model: String,
    ) {
      public suspend operator fun invoke(): Model {
        val response = client.get("models/${this.model.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }
}
