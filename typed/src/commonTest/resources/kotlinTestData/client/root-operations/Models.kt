package root.operations.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface Models {
    suspend fun listModels(): String
}

internal class KtorModels(private val client: HttpClient) : Models {
    override suspend fun listModels(): String =
        client.get("/models").body()
}
