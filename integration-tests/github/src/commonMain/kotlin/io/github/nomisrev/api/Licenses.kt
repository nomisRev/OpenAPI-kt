package io.github.nomisrev.api

import io.github.nomisrev.model.LicenseSimple
import io.github.nomisrev.model.License
import io.github.nomisrev.model.BasicError
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface Licenses {
    sealed interface LicensesGetAllCommonlyUsedResult {
        data class OK(val value: List<LicenseSimple>) : LicensesGetAllCommonlyUsedResult

        data object NotModified : LicensesGetAllCommonlyUsedResult
    }

    suspend fun licensesGetAllCommonlyUsed(
        page: Long = 1L,
        perPage: Long = 30L,
        featured: Boolean? = null,
    ): LicensesGetAllCommonlyUsedResult

    sealed interface LicensesGetResult {
        data class OK(val value: License) : LicensesGetResult

        data object NotModified : LicensesGetResult

        data class Forbidden(val value: BasicError) : LicensesGetResult

        data class NotFound(val value: BasicError) : LicensesGetResult
    }

    suspend fun licensesGet(
        license: String,
    ): LicensesGetResult
}

internal class KtorLicenses(private val client: HttpClient) : Licenses {
    override suspend fun licensesGetAllCommonlyUsed(page: Long, perPage: Long, featured: Boolean?): Licenses.LicensesGetAllCommonlyUsedResult {
        val response = client.get("/licenses") {
            parameter("page", page)
            parameter("per_page", perPage)
            featured?.let { parameter("featured", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Licenses.LicensesGetAllCommonlyUsedResult.OK(response.body())
            HttpStatusCode.NotModified -> Licenses.LicensesGetAllCommonlyUsedResult.NotModified
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun licensesGet(license: String): Licenses.LicensesGetResult {
        val response = client.get("/licenses/$license")
        return when (response.status) {
            HttpStatusCode.OK -> Licenses.LicensesGetResult.OK(response.body())
            HttpStatusCode.NotModified -> Licenses.LicensesGetResult.NotModified
            HttpStatusCode.Forbidden -> Licenses.LicensesGetResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Licenses.LicensesGetResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
