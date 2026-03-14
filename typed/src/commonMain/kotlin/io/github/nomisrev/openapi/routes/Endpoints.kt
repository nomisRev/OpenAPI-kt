package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.NamingContext.Path
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.parsePathSegments
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Operation
import io.ktor.http.HttpMethod
import kotlin.collections.component1
import kotlin.collections.component2

fun OpenAPI.endpoints(): List<Endpoint> = paths.entries.flatMap { (path, p) ->
    val pathParams = p.parameters
    listOfNotNull(
        p.get?.let { Endpoint(path, HttpMethod.Get, it.copy(parameters = pathParams + it.parameters)) },
        p.put?.let { Endpoint(path, HttpMethod.Put, it.copy(parameters = pathParams + it.parameters)) },
        p.post?.let { Endpoint(path, HttpMethod.Post, it.copy(parameters = pathParams + it.parameters)) },
        p.delete?.let { Endpoint(path, HttpMethod.Delete, it.copy(parameters = pathParams + it.parameters)) },
        p.head?.let { Endpoint(path, HttpMethod.Head, it.copy(parameters = pathParams + it.parameters)) },
        p.options?.let { Endpoint(path, HttpMethod.Options, it.copy(parameters = pathParams + it.parameters)) },
        p.trace?.let { Endpoint(path, HttpMethod("Trace"), it.copy(parameters = pathParams + it.parameters)) },
        p.patch?.let { Endpoint(path, HttpMethod.Patch, it.copy(parameters = pathParams + it.parameters)) },
    )
}

class Endpoint(val path: String, val method: HttpMethod, val operation: Operation) {
    private val pathParamRegex = Regex("\\{.*?\\}")
    val pathSegments = path.replace(pathParamRegex, "").split("/").filter { it.isNotEmpty() }
    val pathParameters = pathParamRegex.findAll(path).map { it.value.removeSurrounding("{", "}") }.toList()
    val segments: List<PathSegment> = parsePathSegments(path, emptyMap())
    val operationId = operation.operationId ?: generateSyntheticOperationId(path, method)

    fun context(context: NamingContext.Nested): NamingContext =
        NamingContext(Path(pathSegments), listOf(context))

    private fun generateSyntheticOperationId(path: String, method: HttpMethod): String {
        val params = path.split("/")
            .takeLastWhile { it.startsWith("{") && it.endsWith("}") }
            .map { it.substring(1, it.length - 1) }

        return if (params.isEmpty()) method.value.lowercase()
        else params.joinToString(
            prefix = "${method.value.lowercase()}By",
            separator = "And"
        ) { it.replaceFirstChar { it.uppercase() } }
    }

    override fun toString(): String = "Endpoint(path='$path', method=$method, operationId='$operationId')"
    fun requireUnique(map: kotlin.collections.List<String>, string: String) {}
}
