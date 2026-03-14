package io.github.nomisrev.api

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.json.JsonElement
import io.github.nomisrev.model.ValidationErrorSimple
import io.github.nomisrev.model.BasicError
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.http.ContentType
import io.ktor.client.request.setBody
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.http.contentType

interface Credentials {
    val revoke: Credentials.Revoke

    interface Revoke {
        @Serializable
        @JvmInline
        value class CredentialsRevokeBody(val credentials: List<String>)

        sealed interface CredentialsRevokeResult {
            data class Accepted(val value: JsonElement) : CredentialsRevokeResult

            data class UnprocessableEntity(val value: ValidationErrorSimple) : CredentialsRevokeResult

            data class InternalServerError(val value: BasicError) : CredentialsRevokeResult
        }

        suspend fun credentialsRevoke(
            body: CredentialsRevokeBody,
        ): CredentialsRevokeResult
    }
}

internal class KtorCredentials(private val client: HttpClient) : Credentials {
    override val revoke: Credentials.Revoke = KtorCredentialsRevoke(client)
}

internal class KtorCredentialsRevoke(private val client: HttpClient) : Credentials.Revoke {
    override suspend fun credentialsRevoke(body: Credentials.Revoke.CredentialsRevokeBody): Credentials.Revoke.CredentialsRevokeResult {
        val response = client.post("/credentials/revoke") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Accepted -> Credentials.Revoke.CredentialsRevokeResult.Accepted(response.body())
            HttpStatusCode.UnprocessableEntity -> Credentials.Revoke.CredentialsRevokeResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Credentials.Revoke.CredentialsRevokeResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
