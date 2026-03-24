package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
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
    public suspend operator fun invoke(): ListModelsResponse = client.get("/models").body()
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
      public suspend operator fun invoke(): DeleteModelResponse = client.delete("/models/$model").body()
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val model: String,
    ) {
      public suspend operator fun invoke(): Model = client.get("/models/$model").body()
    }
  }
}
