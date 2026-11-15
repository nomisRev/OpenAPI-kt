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
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public fun model(model: String): ModelPath = ModelPath(__client, model)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): ListModelsResponse {
      val response = __client.get("models")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class ModelPath internal constructor(
    private val __client: HttpClient,
    private val _model: String,
  ) {
    public val delete: Delete = Delete(__client, _model)

    public val `get`: Get = Get(__client, _model)

    public class Delete internal constructor(
      private val __client: HttpClient,
      private val _model: String,
    ) {
      public suspend operator fun invoke(): DeleteModelResponse {
        val response = __client.delete("models/${this._model.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _model: String,
    ) {
      public suspend operator fun invoke(): Model {
        val response = __client.get("models/${this._model.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }
}
