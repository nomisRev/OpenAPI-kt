package io.github.nomisrev.api

import io.github.nomisrev.model.Integration
import io.github.nomisrev.model.WebhookConfigUrl
import io.github.nomisrev.model.WebhookConfigContentType
import io.github.nomisrev.model.WebhookConfigSecret
import io.github.nomisrev.model.WebhookConfigInsecureSsl
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import io.github.nomisrev.model.WebhookConfig
import io.github.nomisrev.model.HookDeliveryItem
import io.github.nomisrev.model.BasicError
import io.github.nomisrev.model.ValidationError
import io.github.nomisrev.model.HookDelivery
import kotlinx.serialization.json.JsonElement
import io.github.nomisrev.model.IntegrationInstallationRequest
import io.github.nomisrev.model.Installation
import kotlinx.datetime.LocalDateTime
import io.github.nomisrev.model.AppPermissions
import io.github.nomisrev.model.InstallationToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.http.ContentType
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.http.contentType

interface App {
    val hook: App.Hook

    val installationRequests: App.InstallationRequests

    val installations: App.Installations

    suspend fun appsGetAuthenticated(): Integration

    interface Hook {
        val config: App.Hook.Config

        val deliveries: App.Hook.Deliveries

        interface Config {
            @Serializable
            data class AppsUpdateWebhookConfigForAppBody(
                val url: WebhookConfigUrl? = null,
                @SerialName("content_type") val contentType: WebhookConfigContentType? = null,
                val secret: WebhookConfigSecret? = null,
                @SerialName("insecure_ssl") val insecureSsl: WebhookConfigInsecureSsl? = null,
            )

            suspend fun appsGetWebhookConfigForApp(): WebhookConfig

            suspend fun appsUpdateWebhookConfigForApp(
                body: AppsUpdateWebhookConfigForAppBody,
            ): WebhookConfig
        }

        interface Deliveries {
            val attempts: App.Hook.Deliveries.Attempts

            sealed interface AppsListWebhookDeliveriesResult {
                data class OK(val value: List<HookDeliveryItem>) : AppsListWebhookDeliveriesResult

                data class BadRequest(val value: BasicError) : AppsListWebhookDeliveriesResult

                data class UnprocessableEntity(val value: ValidationError) : AppsListWebhookDeliveriesResult
            }

            suspend fun appsListWebhookDeliveries(
                perPage: Long = 30L,
                cursor: String? = null,
            ): AppsListWebhookDeliveriesResult

            sealed interface AppsGetWebhookDeliveryResult {
                data class OK(val value: HookDelivery) : AppsGetWebhookDeliveryResult

                data class BadRequest(val value: BasicError) : AppsGetWebhookDeliveryResult

                data class UnprocessableEntity(val value: ValidationError) : AppsGetWebhookDeliveryResult
            }

            suspend fun appsGetWebhookDelivery(
                deliveryId: Long,
            ): AppsGetWebhookDeliveryResult

            interface Attempts {
                sealed interface AppsRedeliverWebhookDeliveryResult {
                    data class Accepted(val value: JsonElement) : AppsRedeliverWebhookDeliveryResult

                    data class BadRequest(val value: BasicError) : AppsRedeliverWebhookDeliveryResult

                    data class UnprocessableEntity(val value: ValidationError) : AppsRedeliverWebhookDeliveryResult
                }

                suspend fun appsRedeliverWebhookDelivery(
                    deliveryId: Long,
                ): AppsRedeliverWebhookDeliveryResult
            }
        }
    }

    interface InstallationRequests {
        sealed interface AppsListInstallationRequestsForAuthenticatedAppResult {
            data class OK(val value: List<IntegrationInstallationRequest>) : AppsListInstallationRequestsForAuthenticatedAppResult

            data object NotModified : AppsListInstallationRequestsForAuthenticatedAppResult

            data class Unauthorized(val value: BasicError) : AppsListInstallationRequestsForAuthenticatedAppResult
        }

        suspend fun appsListInstallationRequestsForAuthenticatedApp(
            page: Long = 1L,
            perPage: Long = 30L,
        ): AppsListInstallationRequestsForAuthenticatedAppResult
    }

    interface Installations {
        val accessTokens: App.Installations.AccessTokens

        val suspended: App.Installations.Suspended

        suspend fun appsListInstallations(
            page: Long = 1L,
            perPage: Long = 30L,
            outdated: String? = null,
            since: LocalDateTime? = null,
        ): List<Installation>

        sealed interface AppsGetInstallationResult {
            data class OK(val value: Installation) : AppsGetInstallationResult

            data class NotFound(val value: BasicError) : AppsGetInstallationResult
        }

        suspend fun appsGetInstallation(
            installationId: Long,
        ): AppsGetInstallationResult

        sealed interface AppsDeleteInstallationResult {
            data object NoContent : AppsDeleteInstallationResult

            data class NotFound(val value: BasicError) : AppsDeleteInstallationResult
        }

        suspend fun appsDeleteInstallation(
            installationId: Long,
        ): AppsDeleteInstallationResult

        interface AccessTokens {
            @Serializable
            data class AppsCreateInstallationAccessTokenBody(
                val repositories: List<String>? = null,
                @SerialName("repository_ids") val repositoryIds: List<Long>? = null,
                val permissions: AppPermissions? = null,
            )

            sealed interface AppsCreateInstallationAccessTokenResult {
                data class Created(val value: InstallationToken) : AppsCreateInstallationAccessTokenResult

                data class Unauthorized(val value: BasicError) : AppsCreateInstallationAccessTokenResult

                data class Forbidden(val value: BasicError) : AppsCreateInstallationAccessTokenResult

                data class NotFound(val value: BasicError) : AppsCreateInstallationAccessTokenResult

                data class UnprocessableEntity(val value: ValidationError) : AppsCreateInstallationAccessTokenResult
            }

            suspend fun appsCreateInstallationAccessToken(
                installationId: Long,
                body: AppsCreateInstallationAccessTokenBody? = null,
            ): AppsCreateInstallationAccessTokenResult
        }

        interface Suspended {
            sealed interface AppsSuspendInstallationResult {
                data object NoContent : AppsSuspendInstallationResult

                data class NotFound(val value: BasicError) : AppsSuspendInstallationResult
            }

            suspend fun appsSuspendInstallation(
                installationId: Long,
            ): AppsSuspendInstallationResult

            sealed interface AppsUnsuspendInstallationResult {
                data object NoContent : AppsUnsuspendInstallationResult

                data class NotFound(val value: BasicError) : AppsUnsuspendInstallationResult
            }

            suspend fun appsUnsuspendInstallation(
                installationId: Long,
            ): AppsUnsuspendInstallationResult
        }
    }
}

internal class KtorApp(private val client: HttpClient) : App {
    override val hook: App.Hook = KtorAppHook(client)

    override val installationRequests: App.InstallationRequests = KtorAppInstallationRequests(client)

    override val installations: App.Installations = KtorAppInstallations(client)

    override suspend fun appsGetAuthenticated(): Integration =
        client.get("/app").body()
}

internal class KtorAppHook(private val client: HttpClient) : App.Hook {
    override val config: App.Hook.Config = KtorAppHookConfig(client)

    override val deliveries: App.Hook.Deliveries = KtorAppHookDeliveries(client)
}

internal class KtorAppHookConfig(private val client: HttpClient) : App.Hook.Config {
    override suspend fun appsGetWebhookConfigForApp(): WebhookConfig =
        client.get("/app/hook/config").body()

    override suspend fun appsUpdateWebhookConfigForApp(body: App.Hook.Config.AppsUpdateWebhookConfigForAppBody): WebhookConfig =
        client.patch("/app/hook/config") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorAppHookDeliveries(private val client: HttpClient) : App.Hook.Deliveries {
    override val attempts: App.Hook.Deliveries.Attempts = KtorAppHookDeliveriesAttempts(client)

    override suspend fun appsListWebhookDeliveries(perPage: Long, cursor: String?): App.Hook.Deliveries.AppsListWebhookDeliveriesResult {
        val response = client.get("/app/hook/deliveries") {
            parameter("per_page", perPage)
            cursor?.let { parameter("cursor", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> App.Hook.Deliveries.AppsListWebhookDeliveriesResult.OK(response.body())
            HttpStatusCode.BadRequest -> App.Hook.Deliveries.AppsListWebhookDeliveriesResult.BadRequest(response.body())
            HttpStatusCode.UnprocessableEntity -> App.Hook.Deliveries.AppsListWebhookDeliveriesResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun appsGetWebhookDelivery(deliveryId: Long): App.Hook.Deliveries.AppsGetWebhookDeliveryResult {
        val response = client.get("/app/hook/deliveries/$deliveryId")
        return when (response.status) {
            HttpStatusCode.OK -> App.Hook.Deliveries.AppsGetWebhookDeliveryResult.OK(response.body())
            HttpStatusCode.BadRequest -> App.Hook.Deliveries.AppsGetWebhookDeliveryResult.BadRequest(response.body())
            HttpStatusCode.UnprocessableEntity -> App.Hook.Deliveries.AppsGetWebhookDeliveryResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorAppHookDeliveriesAttempts(private val client: HttpClient) : App.Hook.Deliveries.Attempts {
    override suspend fun appsRedeliverWebhookDelivery(deliveryId: Long): App.Hook.Deliveries.Attempts.AppsRedeliverWebhookDeliveryResult {
        val response = client.post("/app/hook/deliveries/$deliveryId/attempts")
        return when (response.status) {
            HttpStatusCode.Accepted -> App.Hook.Deliveries.Attempts.AppsRedeliverWebhookDeliveryResult.Accepted(response.body())
            HttpStatusCode.BadRequest -> App.Hook.Deliveries.Attempts.AppsRedeliverWebhookDeliveryResult.BadRequest(response.body())
            HttpStatusCode.UnprocessableEntity -> App.Hook.Deliveries.Attempts.AppsRedeliverWebhookDeliveryResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorAppInstallationRequests(private val client: HttpClient) : App.InstallationRequests {
    override suspend fun appsListInstallationRequestsForAuthenticatedApp(page: Long, perPage: Long): App.InstallationRequests.AppsListInstallationRequestsForAuthenticatedAppResult {
        val response = client.get("/app/installation-requests") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> App.InstallationRequests.AppsListInstallationRequestsForAuthenticatedAppResult.OK(response.body())
            HttpStatusCode.NotModified -> App.InstallationRequests.AppsListInstallationRequestsForAuthenticatedAppResult.NotModified
            HttpStatusCode.Unauthorized -> App.InstallationRequests.AppsListInstallationRequestsForAuthenticatedAppResult.Unauthorized(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorAppInstallations(private val client: HttpClient) : App.Installations {
    override val accessTokens: App.Installations.AccessTokens = KtorAppInstallationsAccessTokens(client)

    override val suspended: App.Installations.Suspended = KtorAppInstallationsSuspended(client)

    override suspend fun appsListInstallations(page: Long, perPage: Long, outdated: String?, since: LocalDateTime?): List<Installation> =
        client.get("/app/installations") {
            parameter("page", page)
            parameter("per_page", perPage)
            outdated?.let { parameter("outdated", it) }
            since?.let { parameter("since", it) }
        }.body()

    override suspend fun appsGetInstallation(installationId: Long): App.Installations.AppsGetInstallationResult {
        val response = client.get("/app/installations/$installationId")
        return when (response.status) {
            HttpStatusCode.OK -> App.Installations.AppsGetInstallationResult.OK(response.body())
            HttpStatusCode.NotFound -> App.Installations.AppsGetInstallationResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun appsDeleteInstallation(installationId: Long): App.Installations.AppsDeleteInstallationResult {
        val response = client.delete("/app/installations/$installationId")
        return when (response.status) {
            HttpStatusCode.NoContent -> App.Installations.AppsDeleteInstallationResult.NoContent
            HttpStatusCode.NotFound -> App.Installations.AppsDeleteInstallationResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorAppInstallationsAccessTokens(private val client: HttpClient) : App.Installations.AccessTokens {
    override suspend fun appsCreateInstallationAccessToken(installationId: Long, body: App.Installations.AccessTokens.AppsCreateInstallationAccessTokenBody?): App.Installations.AccessTokens.AppsCreateInstallationAccessTokenResult {
        val response = client.post("/app/installations/$installationId/access_tokens") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.Created -> App.Installations.AccessTokens.AppsCreateInstallationAccessTokenResult.Created(response.body())
            HttpStatusCode.Unauthorized -> App.Installations.AccessTokens.AppsCreateInstallationAccessTokenResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> App.Installations.AccessTokens.AppsCreateInstallationAccessTokenResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> App.Installations.AccessTokens.AppsCreateInstallationAccessTokenResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> App.Installations.AccessTokens.AppsCreateInstallationAccessTokenResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorAppInstallationsSuspended(private val client: HttpClient) : App.Installations.Suspended {
    override suspend fun appsSuspendInstallation(installationId: Long): App.Installations.Suspended.AppsSuspendInstallationResult {
        val response = client.put("/app/installations/$installationId/suspended")
        return when (response.status) {
            HttpStatusCode.NoContent -> App.Installations.Suspended.AppsSuspendInstallationResult.NoContent
            HttpStatusCode.NotFound -> App.Installations.Suspended.AppsSuspendInstallationResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun appsUnsuspendInstallation(installationId: Long): App.Installations.Suspended.AppsUnsuspendInstallationResult {
        val response = client.delete("/app/installations/$installationId/suspended")
        return when (response.status) {
            HttpStatusCode.NoContent -> App.Installations.Suspended.AppsUnsuspendInstallationResult.NoContent
            HttpStatusCode.NotFound -> App.Installations.Suspended.AppsUnsuspendInstallationResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
