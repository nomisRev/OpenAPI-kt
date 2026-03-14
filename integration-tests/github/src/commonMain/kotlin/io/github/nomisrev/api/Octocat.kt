package io.github.nomisrev.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface Octocat {
    suspend fun metaGetOctocat(
        s: String? = null,
    ): String
}

internal class KtorOctocat(private val client: HttpClient) : Octocat {
    override suspend fun metaGetOctocat(s: String?): String =
        client.get("/octocat") {
            s?.let { parameter("s", it) }
        }.body()
}
