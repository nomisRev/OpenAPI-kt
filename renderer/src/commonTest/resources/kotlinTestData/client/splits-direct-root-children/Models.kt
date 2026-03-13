package io.github.nomisrev.render.golden.client.splits_direct_root_children.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface Models {
    suspend fun listModels(): String

    suspend fun retrieveModel(
        model: String,
    ): String
}

internal class KtorModels(private val client: HttpClient) : Models {
    override suspend fun listModels(): String =
        client.get("/models").body()

    override suspend fun retrieveModel(model: String): String =
        client.get("/models/$model").body()
}
