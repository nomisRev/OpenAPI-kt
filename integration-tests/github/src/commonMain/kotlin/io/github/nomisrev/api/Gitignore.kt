package io.github.nomisrev.api

import io.github.nomisrev.model.GitignoreTemplate
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get

interface Gitignore {
    val templates: Gitignore.Templates

    interface Templates {
        sealed interface GitignoreGetAllTemplatesResult {
            data class OK(val value: List<String>) : GitignoreGetAllTemplatesResult

            data object NotModified : GitignoreGetAllTemplatesResult
        }

        suspend fun gitignoreGetAllTemplates(): GitignoreGetAllTemplatesResult

        sealed interface GitignoreGetTemplateResult {
            data class OK(val value: GitignoreTemplate) : GitignoreGetTemplateResult

            data object NotModified : GitignoreGetTemplateResult
        }

        suspend fun gitignoreGetTemplate(
            name: String,
        ): GitignoreGetTemplateResult
    }
}

internal class KtorGitignore(private val client: HttpClient) : Gitignore {
    override val templates: Gitignore.Templates = KtorGitignoreTemplates(client)
}

internal class KtorGitignoreTemplates(private val client: HttpClient) : Gitignore.Templates {
    override suspend fun gitignoreGetAllTemplates(): Gitignore.Templates.GitignoreGetAllTemplatesResult {
        val response = client.get("/gitignore/templates")
        return when (response.status) {
            HttpStatusCode.OK -> Gitignore.Templates.GitignoreGetAllTemplatesResult.OK(response.body())
            HttpStatusCode.NotModified -> Gitignore.Templates.GitignoreGetAllTemplatesResult.NotModified
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gitignoreGetTemplate(name: String): Gitignore.Templates.GitignoreGetTemplateResult {
        val response = client.get("/gitignore/templates/$name")
        return when (response.status) {
            HttpStatusCode.OK -> Gitignore.Templates.GitignoreGetTemplateResult.OK(response.body())
            HttpStatusCode.NotModified -> Gitignore.Templates.GitignoreGetTemplateResult.NotModified
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
