package io.github.nomisrev.api

import io.github.nomisrev.model.Event
import io.github.nomisrev.model.BasicError
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface Networks {
    val events: Networks.Events

    interface Events {
        sealed interface ActivityListPublicEventsForRepoNetworkResult {
            data class OK(val value: List<Event>) : ActivityListPublicEventsForRepoNetworkResult

            data class MovedPermanently(val value: BasicError) : ActivityListPublicEventsForRepoNetworkResult

            data object NotModified : ActivityListPublicEventsForRepoNetworkResult

            data class Forbidden(val value: BasicError) : ActivityListPublicEventsForRepoNetworkResult

            data class NotFound(val value: BasicError) : ActivityListPublicEventsForRepoNetworkResult
        }

        suspend fun activityListPublicEventsForRepoNetwork(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): ActivityListPublicEventsForRepoNetworkResult
    }
}

internal class KtorNetworks(private val client: HttpClient) : Networks {
    override val events: Networks.Events = KtorNetworksEvents(client)
}

internal class KtorNetworksEvents(private val client: HttpClient) : Networks.Events {
    override suspend fun activityListPublicEventsForRepoNetwork(owner: String, repo: String, page: Long, perPage: Long): Networks.Events.ActivityListPublicEventsForRepoNetworkResult {
        val response = client.get("/networks/$owner/$repo/events") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Networks.Events.ActivityListPublicEventsForRepoNetworkResult.OK(response.body())
            HttpStatusCode.MovedPermanently -> Networks.Events.ActivityListPublicEventsForRepoNetworkResult.MovedPermanently(response.body())
            HttpStatusCode.NotModified -> Networks.Events.ActivityListPublicEventsForRepoNetworkResult.NotModified
            HttpStatusCode.Forbidden -> Networks.Events.ActivityListPublicEventsForRepoNetworkResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Networks.Events.ActivityListPublicEventsForRepoNetworkResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
