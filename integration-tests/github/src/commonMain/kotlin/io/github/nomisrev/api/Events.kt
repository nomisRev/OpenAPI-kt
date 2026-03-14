package io.github.nomisrev.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import io.github.nomisrev.model.Event
import io.github.nomisrev.model.BasicError
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface Events {
    @Serializable
    data class ActivityListPublicEventsResponse(
        val code: String? = null,
        val message: String? = null,
        @SerialName("documentation_url") val documentationUrl: String? = null,
    )

    sealed interface ActivityListPublicEventsResult {
        data class OK(val value: List<Event>) : ActivityListPublicEventsResult

        data object NotModified : ActivityListPublicEventsResult

        data class Forbidden(val value: BasicError) : ActivityListPublicEventsResult

        data class ServiceUnavailable(val value: ActivityListPublicEventsResponse) : ActivityListPublicEventsResult
    }

    suspend fun activityListPublicEvents(
        page: Long = 1L,
        perPage: Long = 15L,
    ): ActivityListPublicEventsResult
}

internal class KtorEvents(private val client: HttpClient) : Events {
    override suspend fun activityListPublicEvents(page: Long, perPage: Long): Events.ActivityListPublicEventsResult {
        val response = client.get("/events") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Events.ActivityListPublicEventsResult.OK(response.body())
            HttpStatusCode.NotModified -> Events.ActivityListPublicEventsResult.NotModified
            HttpStatusCode.Forbidden -> Events.ActivityListPublicEventsResult.Forbidden(response.body())
            HttpStatusCode.ServiceUnavailable -> Events.ActivityListPublicEventsResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
