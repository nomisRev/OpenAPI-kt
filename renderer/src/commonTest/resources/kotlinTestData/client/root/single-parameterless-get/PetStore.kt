package io.github.nomisrev.render.golden.client.root.single_parameterless_get.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get

interface PetStore {
    suspend fun listPets(): String
}

internal class KtorPetStore(private val client: HttpClient) : PetStore {
    override suspend fun listPets(): String =
        client.get("/pets").body()
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
