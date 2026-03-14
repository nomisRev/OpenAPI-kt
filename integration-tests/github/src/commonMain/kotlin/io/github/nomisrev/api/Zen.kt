package io.github.nomisrev.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface Zen {
    suspend fun metaGetZen(): String
}

internal class KtorZen(private val client: HttpClient) : Zen {
    override suspend fun metaGetZen(): String =
        client.get("/zen").body()
}
