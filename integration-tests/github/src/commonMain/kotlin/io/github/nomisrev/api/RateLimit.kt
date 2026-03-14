package io.github.nomisrev.api

import io.github.nomisrev.model.RateLimitOverview
import io.github.nomisrev.model.BasicError
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get

interface RateLimit {
    sealed interface RateLimitGetResult {
        data class OK(val value: RateLimitOverview) : RateLimitGetResult

        data object NotModified : RateLimitGetResult

        data class NotFound(val value: BasicError) : RateLimitGetResult
    }

    suspend fun rateLimitGet(): RateLimitGetResult
}

internal class KtorRateLimit(private val client: HttpClient) : RateLimit {
    override suspend fun rateLimitGet(): RateLimit.RateLimitGetResult {
        val response = client.get("/rate_limit")
        return when (response.status) {
            HttpStatusCode.OK -> RateLimit.RateLimitGetResult.OK(response.body())
            HttpStatusCode.NotModified -> RateLimit.RateLimitGetResult.NotModified
            HttpStatusCode.NotFound -> RateLimit.RateLimitGetResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
