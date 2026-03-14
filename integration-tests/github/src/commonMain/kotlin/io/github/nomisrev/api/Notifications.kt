package io.github.nomisrev.api

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline
import io.github.nomisrev.model.Thread
import io.github.nomisrev.model.BasicError
import io.github.nomisrev.model.ValidationError
import io.github.nomisrev.model.ThreadSubscription
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.http.ContentType
import io.ktor.client.request.setBody
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.put
import io.ktor.http.contentType

interface Notifications {
    val threads: Notifications.Threads

    @Serializable
    data class ActivityMarkNotificationsAsReadBody(
        @SerialName("last_read_at") val lastReadAt: LocalDateTime? = null,
        val read: Boolean? = null,
    )


    @Serializable
    @JvmInline
    value class ActivityMarkNotificationsAsReadResponse(val message: String? = null)

    sealed interface ActivityListNotificationsForAuthenticatedUserResult {
        data class OK(val value: List<Thread>) : ActivityListNotificationsForAuthenticatedUserResult

        data object NotModified : ActivityListNotificationsForAuthenticatedUserResult

        data class Unauthorized(val value: BasicError) : ActivityListNotificationsForAuthenticatedUserResult

        data class Forbidden(val value: BasicError) : ActivityListNotificationsForAuthenticatedUserResult

        data class UnprocessableEntity(val value: ValidationError) : ActivityListNotificationsForAuthenticatedUserResult
    }

    suspend fun activityListNotificationsForAuthenticatedUser(
        all: Boolean = false,
        page: Long = 1L,
        participating: Boolean = false,
        perPage: Long = 50L,
        before: LocalDateTime? = null,
        since: LocalDateTime? = null,
    ): ActivityListNotificationsForAuthenticatedUserResult

    sealed interface ActivityMarkNotificationsAsReadResult {
        data class Accepted(val value: ActivityMarkNotificationsAsReadResponse) : ActivityMarkNotificationsAsReadResult

        data object ResetContent : ActivityMarkNotificationsAsReadResult

        data object NotModified : ActivityMarkNotificationsAsReadResult

        data class Unauthorized(val value: BasicError) : ActivityMarkNotificationsAsReadResult

        data class Forbidden(val value: BasicError) : ActivityMarkNotificationsAsReadResult
    }

    suspend fun activityMarkNotificationsAsRead(
        body: ActivityMarkNotificationsAsReadBody? = null,
    ): ActivityMarkNotificationsAsReadResult

    interface Threads {
        val subscription: Notifications.Threads.Subscription

        sealed interface ActivityGetThreadResult {
            data class OK(val value: Thread) : ActivityGetThreadResult

            data object NotModified : ActivityGetThreadResult

            data class Unauthorized(val value: BasicError) : ActivityGetThreadResult

            data class Forbidden(val value: BasicError) : ActivityGetThreadResult
        }

        suspend fun activityGetThread(
            threadId: Long,
        ): ActivityGetThreadResult

        suspend fun activityMarkThreadAsDone(
            threadId: Long,
        ): Unit

        sealed interface ActivityMarkThreadAsReadResult {
            data object ResetContent : ActivityMarkThreadAsReadResult

            data object NotModified : ActivityMarkThreadAsReadResult

            data class Forbidden(val value: BasicError) : ActivityMarkThreadAsReadResult
        }

        suspend fun activityMarkThreadAsRead(
            threadId: Long,
        ): ActivityMarkThreadAsReadResult

        interface Subscription {
            @Serializable
            @JvmInline
            value class ActivitySetThreadSubscriptionBody(val ignored: Boolean? = null)

            sealed interface ActivityGetThreadSubscriptionForAuthenticatedUserResult {
                data class OK(val value: ThreadSubscription) : ActivityGetThreadSubscriptionForAuthenticatedUserResult

                data object NotModified : ActivityGetThreadSubscriptionForAuthenticatedUserResult

                data class Unauthorized(val value: BasicError) : ActivityGetThreadSubscriptionForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : ActivityGetThreadSubscriptionForAuthenticatedUserResult
            }

            suspend fun activityGetThreadSubscriptionForAuthenticatedUser(
                threadId: Long,
            ): ActivityGetThreadSubscriptionForAuthenticatedUserResult

            sealed interface ActivitySetThreadSubscriptionResult {
                data class OK(val value: ThreadSubscription) : ActivitySetThreadSubscriptionResult

                data object NotModified : ActivitySetThreadSubscriptionResult

                data class Unauthorized(val value: BasicError) : ActivitySetThreadSubscriptionResult

                data class Forbidden(val value: BasicError) : ActivitySetThreadSubscriptionResult
            }

            suspend fun activitySetThreadSubscription(
                threadId: Long,
                body: ActivitySetThreadSubscriptionBody? = null,
            ): ActivitySetThreadSubscriptionResult

            sealed interface ActivityDeleteThreadSubscriptionResult {
                data object NoContent : ActivityDeleteThreadSubscriptionResult

                data object NotModified : ActivityDeleteThreadSubscriptionResult

                data class Unauthorized(val value: BasicError) : ActivityDeleteThreadSubscriptionResult

                data class Forbidden(val value: BasicError) : ActivityDeleteThreadSubscriptionResult
            }

            suspend fun activityDeleteThreadSubscription(
                threadId: Long,
            ): ActivityDeleteThreadSubscriptionResult
        }
    }
}

internal class KtorNotifications(private val client: HttpClient) : Notifications {
    override val threads: Notifications.Threads = KtorNotificationsThreads(client)

    override suspend fun activityListNotificationsForAuthenticatedUser(all: Boolean, page: Long, participating: Boolean, perPage: Long, before: LocalDateTime?, since: LocalDateTime?): Notifications.ActivityListNotificationsForAuthenticatedUserResult {
        val response = client.get("/notifications") {
            parameter("all", all)
            parameter("page", page)
            parameter("participating", participating)
            parameter("per_page", perPage)
            before?.let { parameter("before", it) }
            since?.let { parameter("since", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Notifications.ActivityListNotificationsForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> Notifications.ActivityListNotificationsForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> Notifications.ActivityListNotificationsForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Notifications.ActivityListNotificationsForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Notifications.ActivityListNotificationsForAuthenticatedUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun activityMarkNotificationsAsRead(body: Notifications.ActivityMarkNotificationsAsReadBody?): Notifications.ActivityMarkNotificationsAsReadResult {
        val response = client.put("/notifications") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.Accepted -> Notifications.ActivityMarkNotificationsAsReadResult.Accepted(response.body())
            HttpStatusCode.ResetContent -> Notifications.ActivityMarkNotificationsAsReadResult.ResetContent
            HttpStatusCode.NotModified -> Notifications.ActivityMarkNotificationsAsReadResult.NotModified
            HttpStatusCode.Unauthorized -> Notifications.ActivityMarkNotificationsAsReadResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Notifications.ActivityMarkNotificationsAsReadResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorNotificationsThreads(private val client: HttpClient) : Notifications.Threads {
    override val subscription: Notifications.Threads.Subscription = KtorNotificationsThreadsSubscription(client)

    override suspend fun activityGetThread(threadId: Long): Notifications.Threads.ActivityGetThreadResult {
        val response = client.get("/notifications/threads/$threadId")
        return when (response.status) {
            HttpStatusCode.OK -> Notifications.Threads.ActivityGetThreadResult.OK(response.body())
            HttpStatusCode.NotModified -> Notifications.Threads.ActivityGetThreadResult.NotModified
            HttpStatusCode.Unauthorized -> Notifications.Threads.ActivityGetThreadResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Notifications.Threads.ActivityGetThreadResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun activityMarkThreadAsDone(threadId: Long): Unit =
        client.delete("/notifications/threads/$threadId").body()

    override suspend fun activityMarkThreadAsRead(threadId: Long): Notifications.Threads.ActivityMarkThreadAsReadResult {
        val response = client.patch("/notifications/threads/$threadId")
        return when (response.status) {
            HttpStatusCode.ResetContent -> Notifications.Threads.ActivityMarkThreadAsReadResult.ResetContent
            HttpStatusCode.NotModified -> Notifications.Threads.ActivityMarkThreadAsReadResult.NotModified
            HttpStatusCode.Forbidden -> Notifications.Threads.ActivityMarkThreadAsReadResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorNotificationsThreadsSubscription(private val client: HttpClient) : Notifications.Threads.Subscription {
    override suspend fun activityGetThreadSubscriptionForAuthenticatedUser(threadId: Long): Notifications.Threads.Subscription.ActivityGetThreadSubscriptionForAuthenticatedUserResult {
        val response = client.get("/notifications/threads/$threadId/subscription")
        return when (response.status) {
            HttpStatusCode.OK -> Notifications.Threads.Subscription.ActivityGetThreadSubscriptionForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> Notifications.Threads.Subscription.ActivityGetThreadSubscriptionForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> Notifications.Threads.Subscription.ActivityGetThreadSubscriptionForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Notifications.Threads.Subscription.ActivityGetThreadSubscriptionForAuthenticatedUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun activitySetThreadSubscription(threadId: Long, body: Notifications.Threads.Subscription.ActivitySetThreadSubscriptionBody?): Notifications.Threads.Subscription.ActivitySetThreadSubscriptionResult {
        val response = client.put("/notifications/threads/$threadId/subscription") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Notifications.Threads.Subscription.ActivitySetThreadSubscriptionResult.OK(response.body())
            HttpStatusCode.NotModified -> Notifications.Threads.Subscription.ActivitySetThreadSubscriptionResult.NotModified
            HttpStatusCode.Unauthorized -> Notifications.Threads.Subscription.ActivitySetThreadSubscriptionResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Notifications.Threads.Subscription.ActivitySetThreadSubscriptionResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun activityDeleteThreadSubscription(threadId: Long): Notifications.Threads.Subscription.ActivityDeleteThreadSubscriptionResult {
        val response = client.delete("/notifications/threads/$threadId/subscription")
        return when (response.status) {
            HttpStatusCode.NoContent -> Notifications.Threads.Subscription.ActivityDeleteThreadSubscriptionResult.NoContent
            HttpStatusCode.NotModified -> Notifications.Threads.Subscription.ActivityDeleteThreadSubscriptionResult.NotModified
            HttpStatusCode.Unauthorized -> Notifications.Threads.Subscription.ActivityDeleteThreadSubscriptionResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Notifications.Threads.Subscription.ActivityDeleteThreadSubscriptionResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
