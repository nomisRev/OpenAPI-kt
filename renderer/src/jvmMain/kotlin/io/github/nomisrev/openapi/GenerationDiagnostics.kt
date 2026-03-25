package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.FileSpec
import io.github.nomisrev.openapi.routes.Route

data class GenerationResult(
    val files: List<FileSpec>,
    val diagnostics: List<GenerationDiagnostic>,
)

fun GenerationResult.throwOnErrors() {
    diagnostics.throwOnErrors()
}

data class GenerationDiagnostic(
    val severity: GenerationDiagnosticSeverity,
    val message: String,
)

enum class GenerationDiagnosticSeverity {
    Warning,
    Error,
}

fun List<GenerationDiagnostic>.throwOnErrors() {
    val errors = filter { it.severity == GenerationDiagnosticSeverity.Error }
    if (errors.isEmpty()) return
    throw IllegalArgumentException(
        buildString {
            appendLine("OpenAPI generation failed:")
            errors.forEach { diagnostic ->
                append("- ")
                appendLine(diagnostic.message)
            }
        }.trimEnd()
    )
}

internal fun ApiTree.requestBodyDiagnostics(): List<GenerationDiagnostic> =
    operations.values.flatMapTo(mutableListOf(), Route::requestBodyDiagnostics) +
        children.flatMapTo(mutableListOf(), PathNode::requestBodyDiagnostics)

private fun PathNode.requestBodyDiagnostics(): List<GenerationDiagnostic> =
    operations.values.flatMapTo(mutableListOf(), Route::requestBodyDiagnostics) +
        children.flatMapTo(mutableListOf(), PathNode::requestBodyDiagnostics)

private fun Route.requestBodyDiagnostics(): List<GenerationDiagnostic> {
    val bodies = body ?: return emptyList()
    val variants = bodies.variants()
    val supportedVariants = variants.filter { it.body.isSupportedForGeneration() }
    val unsupportedVariants = variants.filter { !it.body.isSupportedForGeneration() }
    if (unsupportedVariants.isEmpty()) return emptyList()

    return unsupportedVariants.mapNotNull { variant ->
        val formBody = variant.body as? Route.Body.FormUrlEncoded ?: return@mapNotNull null
        val fieldDescription = formBody.unsupportedFields.sorted().joinToString(", ")
        val prefix = "${method.value} $path (${variant.contentType})"
        if (supportedVariants.isEmpty()) {
            GenerationDiagnostic(
                severity = GenerationDiagnosticSeverity.Error,
                message =
                    "$prefix contains non-scalar form fields without explicit encoding rules " +
                        "[$fieldDescription], and no alternative supported request body variant exists.",
            )
        } else {
            GenerationDiagnostic(
                severity = GenerationDiagnosticSeverity.Warning,
                message =
                    "Skipping unsupported form overload for $prefix because fields " +
                        "[$fieldDescription] are non-scalar and have no explicit encoding rules.",
            )
        }
    }
}

internal fun ApiTree.withSupportedRequestBodiesOnly(): ApiTree = copy(
    operations = operations.mapValues { (_, route) -> route.withSupportedRequestBodiesOnly() },
    children = children.map(PathNode::withSupportedRequestBodiesOnly),
)

private fun PathNode.withSupportedRequestBodiesOnly(): PathNode = copy(
    operations = operations.mapValues { (_, route) -> route.withSupportedRequestBodiesOnly() },
    children = children.map(PathNode::withSupportedRequestBodiesOnly),
)

private fun Route.withSupportedRequestBodiesOnly(): Route =
    copy(body = body?.withSupportedRequestBodiesOnly())

private fun Route.Bodies.withSupportedRequestBodiesOnly(): Route.Bodies? {
    val supportedTypes = types.filterValues(Route.Body::isSupportedForGeneration)
    return if (supportedTypes.isEmpty()) null else copy(types = supportedTypes)
}

private fun Route.Body.isSupportedForGeneration(): Boolean =
    when (this) {
        is Route.Body.FormUrlEncoded -> isSupported
        is Route.Body.Multipart.Ref,
        is Route.Body.Multipart.Value,
        is Route.Body.OverloadedBody,
        is Route.Body.SetBody -> true
    }
