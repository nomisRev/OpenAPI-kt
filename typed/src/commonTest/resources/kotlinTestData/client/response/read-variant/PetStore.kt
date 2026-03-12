package io.github.nomisrev.render.golden.client.response.read_variant.api

import io.github.nomisrev.model.PetResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get

interface PetStore {
    suspend fun getPet(): PetResponse
}

internal class KtorPetStore(private val client: HttpClient) : PetStore {
    override suspend fun getPet(): PetResponse =
        client.get("/pet").body()
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
