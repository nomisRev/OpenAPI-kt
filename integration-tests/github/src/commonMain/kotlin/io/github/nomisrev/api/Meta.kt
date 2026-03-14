package io.github.nomisrev.api

import io.github.nomisrev.model.ApiOverview
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get

interface Meta {
    sealed interface MetaGetResult {
        data class OK(val value: ApiOverview) : MetaGetResult

        data object NotModified : MetaGetResult
    }

    suspend fun metaGet(): MetaGetResult
}

internal class KtorMeta(private val client: HttpClient) : Meta {
    override suspend fun metaGet(): Meta.MetaGetResult {
        val response = client.get("/meta")
        return when (response.status) {
            HttpStatusCode.OK -> Meta.MetaGetResult.OK(response.body())
            HttpStatusCode.NotModified -> Meta.MetaGetResult.NotModified
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
