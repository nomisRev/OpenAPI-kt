package io.github.nomisrev.api

import io.github.nomisrev.model.CodeOfConduct
import io.github.nomisrev.model.BasicError
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get

interface CodesOfConduct {
    sealed interface CodesOfConductGetAllCodesOfConductResult {
        data class OK(val value: List<CodeOfConduct>) : CodesOfConductGetAllCodesOfConductResult

        data object NotModified : CodesOfConductGetAllCodesOfConductResult
    }

    suspend fun codesOfConductGetAllCodesOfConduct(): CodesOfConductGetAllCodesOfConductResult

    sealed interface CodesOfConductGetConductCodeResult {
        data class OK(val value: CodeOfConduct) : CodesOfConductGetConductCodeResult

        data object NotModified : CodesOfConductGetConductCodeResult

        data class NotFound(val value: BasicError) : CodesOfConductGetConductCodeResult
    }

    suspend fun codesOfConductGetConductCode(
        key: String,
    ): CodesOfConductGetConductCodeResult
}

internal class KtorCodesOfConduct(private val client: HttpClient) : CodesOfConduct {
    override suspend fun codesOfConductGetAllCodesOfConduct(): CodesOfConduct.CodesOfConductGetAllCodesOfConductResult {
        val response = client.get("/codes_of_conduct")
        return when (response.status) {
            HttpStatusCode.OK -> CodesOfConduct.CodesOfConductGetAllCodesOfConductResult.OK(response.body())
            HttpStatusCode.NotModified -> CodesOfConduct.CodesOfConductGetAllCodesOfConductResult.NotModified
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codesOfConductGetConductCode(key: String): CodesOfConduct.CodesOfConductGetConductCodeResult {
        val response = client.get("/codes_of_conduct/$key")
        return when (response.status) {
            HttpStatusCode.OK -> CodesOfConduct.CodesOfConductGetConductCodeResult.OK(response.body())
            HttpStatusCode.NotModified -> CodesOfConduct.CodesOfConductGetConductCodeResult.NotModified
            HttpStatusCode.NotFound -> CodesOfConduct.CodesOfConductGetConductCodeResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
