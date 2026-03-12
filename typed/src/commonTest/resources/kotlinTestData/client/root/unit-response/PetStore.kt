package root.unit.response.api

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json

interface PetStore {
    suspend fun healthCheck(): Unit
}

internal class KtorPetStore(private val client: HttpClient) : PetStore {
    override suspend fun healthCheck(): Unit =
        client.get("/health").body()
}

fun PetStoreClient(
    baseUrl: String,
    block: HttpClientConfig<*>.() -> Unit = {},
): PetStore {
    val client = HttpClient {
        install(ContentNegotiation) { json() }
        defaultRequest { url(baseUrl) }
        block()
    }
    return KtorPetStore(client)
}
