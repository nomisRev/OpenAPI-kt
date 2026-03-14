package io.github.nomisrev.api

import kotlinx.datetime.LocalDate
import io.github.nomisrev.model.BasicError
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get

interface Versions {
    sealed interface MetaGetAllVersionsResult {
        data class OK(val value: List<LocalDate>) : MetaGetAllVersionsResult

        data class NotFound(val value: BasicError) : MetaGetAllVersionsResult
    }

    suspend fun metaGetAllVersions(): MetaGetAllVersionsResult
}

internal class KtorVersions(private val client: HttpClient) : Versions {
    override suspend fun metaGetAllVersions(): Versions.MetaGetAllVersionsResult {
        val response = client.get("/versions")
        return when (response.status) {
            HttpStatusCode.OK -> Versions.MetaGetAllVersionsResult.OK(response.body())
            HttpStatusCode.NotFound -> Versions.MetaGetAllVersionsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
