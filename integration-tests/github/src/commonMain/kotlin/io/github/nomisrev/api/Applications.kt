package io.github.nomisrev.api

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import io.github.nomisrev.model.ValidationError
import io.github.nomisrev.model.Authorization
import io.github.nomisrev.model.BasicError
import io.github.nomisrev.model.AppPermissions
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.http.ContentType
import io.ktor.client.request.setBody
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.delete
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.http.contentType

interface Applications {
    val grant: Applications.Grant

    val token: Applications.Token

    interface Grant {
        @Serializable
        @JvmInline
        value class AppsDeleteAuthorizationBody(@SerialName("access_token") val accessToken: String)

        sealed interface AppsDeleteAuthorizationResult {
            data object NoContent : AppsDeleteAuthorizationResult

            data class UnprocessableEntity(val value: ValidationError) : AppsDeleteAuthorizationResult
        }

        suspend fun appsDeleteAuthorization(
            clientId: String,
            body: AppsDeleteAuthorizationBody,
        ): AppsDeleteAuthorizationResult
    }

    interface Token {
        val scoped: Applications.Token.Scoped

        @Serializable
        @JvmInline
        value class AppsCheckTokenBody(@SerialName("access_token") val accessToken: String)


        @Serializable
        @JvmInline
        value class AppsDeleteTokenBody(@SerialName("access_token") val accessToken: String)


        @Serializable
        @JvmInline
        value class AppsResetTokenBody(@SerialName("access_token") val accessToken: String)

        sealed interface AppsCheckTokenResult {
            data class OK(val value: Authorization) : AppsCheckTokenResult

            data class NotFound(val value: BasicError) : AppsCheckTokenResult

            data class UnprocessableEntity(val value: ValidationError) : AppsCheckTokenResult
        }

        suspend fun appsCheckToken(
            clientId: String,
            body: AppsCheckTokenBody,
        ): AppsCheckTokenResult

        sealed interface AppsDeleteTokenResult {
            data object NoContent : AppsDeleteTokenResult

            data class UnprocessableEntity(val value: ValidationError) : AppsDeleteTokenResult
        }

        suspend fun appsDeleteToken(
            clientId: String,
            body: AppsDeleteTokenBody,
        ): AppsDeleteTokenResult

        sealed interface AppsResetTokenResult {
            data class OK(val value: Authorization) : AppsResetTokenResult

            data class UnprocessableEntity(val value: ValidationError) : AppsResetTokenResult
        }

        suspend fun appsResetToken(
            clientId: String,
            body: AppsResetTokenBody,
        ): AppsResetTokenResult

        interface Scoped {
            @Serializable
            data class AppsScopeTokenBody(
                @SerialName("access_token") val accessToken: String,
                val target: String? = null,
                @SerialName("target_id") val targetId: Long? = null,
                val repositories: List<String>? = null,
                @SerialName("repository_ids") val repositoryIds: List<Long>? = null,
                val permissions: AppPermissions? = null,
            )

            sealed interface AppsScopeTokenResult {
                data class OK(val value: Authorization) : AppsScopeTokenResult

                data class Unauthorized(val value: BasicError) : AppsScopeTokenResult

                data class Forbidden(val value: BasicError) : AppsScopeTokenResult

                data class NotFound(val value: BasicError) : AppsScopeTokenResult

                data class UnprocessableEntity(val value: ValidationError) : AppsScopeTokenResult
            }

            suspend fun appsScopeToken(
                clientId: String,
                body: AppsScopeTokenBody,
            ): AppsScopeTokenResult
        }
    }
}

internal class KtorApplications(private val client: HttpClient) : Applications {
    override val grant: Applications.Grant = KtorApplicationsGrant(client)

    override val token: Applications.Token = KtorApplicationsToken(client)
}

internal class KtorApplicationsGrant(private val client: HttpClient) : Applications.Grant {
    override suspend fun appsDeleteAuthorization(clientId: String, body: Applications.Grant.AppsDeleteAuthorizationBody): Applications.Grant.AppsDeleteAuthorizationResult {
        val response = client.delete("/applications/$clientId/grant") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Applications.Grant.AppsDeleteAuthorizationResult.NoContent
            HttpStatusCode.UnprocessableEntity -> Applications.Grant.AppsDeleteAuthorizationResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorApplicationsToken(private val client: HttpClient) : Applications.Token {
    override val scoped: Applications.Token.Scoped = KtorApplicationsTokenScoped(client)

    override suspend fun appsCheckToken(clientId: String, body: Applications.Token.AppsCheckTokenBody): Applications.Token.AppsCheckTokenResult {
        val response = client.post("/applications/$clientId/token") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Applications.Token.AppsCheckTokenResult.OK(response.body())
            HttpStatusCode.NotFound -> Applications.Token.AppsCheckTokenResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Applications.Token.AppsCheckTokenResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun appsDeleteToken(clientId: String, body: Applications.Token.AppsDeleteTokenBody): Applications.Token.AppsDeleteTokenResult {
        val response = client.delete("/applications/$clientId/token") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Applications.Token.AppsDeleteTokenResult.NoContent
            HttpStatusCode.UnprocessableEntity -> Applications.Token.AppsDeleteTokenResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun appsResetToken(clientId: String, body: Applications.Token.AppsResetTokenBody): Applications.Token.AppsResetTokenResult {
        val response = client.patch("/applications/$clientId/token") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Applications.Token.AppsResetTokenResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Applications.Token.AppsResetTokenResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorApplicationsTokenScoped(private val client: HttpClient) : Applications.Token.Scoped {
    override suspend fun appsScopeToken(clientId: String, body: Applications.Token.Scoped.AppsScopeTokenBody): Applications.Token.Scoped.AppsScopeTokenResult {
        val response = client.post("/applications/$clientId/token/scoped") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Applications.Token.Scoped.AppsScopeTokenResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Applications.Token.Scoped.AppsScopeTokenResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Applications.Token.Scoped.AppsScopeTokenResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Applications.Token.Scoped.AppsScopeTokenResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Applications.Token.Scoped.AppsScopeTokenResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
