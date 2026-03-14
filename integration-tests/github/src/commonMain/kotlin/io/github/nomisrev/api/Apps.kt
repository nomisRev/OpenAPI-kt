package io.github.nomisrev.api

import io.github.nomisrev.model.Integration
import io.github.nomisrev.model.BasicError
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get

interface Apps {
    sealed interface AppsGetBySlugResult {
        data class OK(val value: Integration) : AppsGetBySlugResult

        data class Forbidden(val value: BasicError) : AppsGetBySlugResult

        data class NotFound(val value: BasicError) : AppsGetBySlugResult
    }

    suspend fun appsGetBySlug(
        appSlug: String,
    ): AppsGetBySlugResult
}

internal class KtorApps(private val client: HttpClient) : Apps {
    override suspend fun appsGetBySlug(appSlug: String): Apps.AppsGetBySlugResult {
        val response = client.get("/apps/$appSlug")
        return when (response.status) {
            HttpStatusCode.OK -> Apps.AppsGetBySlugResult.OK(response.body())
            HttpStatusCode.Forbidden -> Apps.AppsGetBySlugResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Apps.AppsGetBySlugResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
