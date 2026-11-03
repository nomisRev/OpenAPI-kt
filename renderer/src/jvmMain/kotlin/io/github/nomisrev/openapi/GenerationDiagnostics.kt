package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.FileSpec
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.routes.isWildcard
import io.ktor.http.ContentType

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
    val diagnostics = mutableListOf<GenerationDiagnostic>()
    val variants = bodies.variants()
    val supportedVariants = variants.filter { it.isSupportedForGeneration() }
    val unsupportedVariants = variants.filter { !it.isSupportedForGeneration() }

    diagnostics += unsupportedVariants.mapNotNull { variant ->
        val formBody = variant.body as? Route.Body.FormUrlEncoded ?: return@mapNotNull null
        if (formBody.isSupported) return@mapNotNull null
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

    diagnostics += unsupportedVariants.mapNotNull { variant ->
        val charset = variant.unsupportedFormCharsetOrNull() ?: return@mapNotNull null
        val prefix = "${method.value} $path (${variant.contentType})"
        val reason =
            "declares a form body in charset $charset, but a form body is percent-encoded as UTF-8, and sending " +
                "it labelled $charset would make the server decode every non-ASCII character wrongly"
        if (supportedVariants.isEmpty()) {
            GenerationDiagnostic(
                severity = GenerationDiagnosticSeverity.Error,
                message = "$prefix $reason, and no alternative supported request body variant exists.",
            )
        } else {
            GenerationDiagnostic(
                severity = GenerationDiagnosticSeverity.Warning,
                message = "Skipping unsupported form overload for $prefix, which $reason.",
            )
        }
    }

    diagnostics += unsupportedVariants.mapNotNull { variant ->
        val contentType = variant.body.unsendableWildcardOrNull() ?: return@mapNotNull null
        val prefix = "${method.value} $path ($contentType)"
        val reason =
            "declares its request body under a media type range that admits no media type the client produces for " +
                "this body (JSON, text/plain, application/octet-stream; only bytes are sent as a media type the caller " +
                "supplies), and a wildcard is not a valid Content-Type"
        if (supportedVariants.isEmpty()) {
            GenerationDiagnostic(
                severity = GenerationDiagnosticSeverity.Error,
                message = "$prefix $reason, and no alternative supported request body variant exists.",
            )
        } else {
            GenerationDiagnostic(
                severity = GenerationDiagnosticSeverity.Warning,
                message = "Skipping unsupported request body overload for $prefix, which $reason.",
            )
        }
    }

    if (bodies.exceedsFlattenedBodyOverloadLimit()) {
        val contentType = bodies.variants().single().contentType
        diagnostics += GenerationDiagnostic(
            severity = GenerationDiagnosticSeverity.Error,
            message =
                "${method.value} $path ($contentType) would generate more than " +
                    "$FlattenedBodyMaxOverloads flattened request-body overloads. " +
                    "Generation stops instead of silently falling back to a nested body parameter.",
        )
    }

    return diagnostics
}

internal fun ApiTree.withSupportedRequestBodiesOnly(): ApiTree = copy(
    operations = operations.mapValues { [_, route] -> route.withSupportedRequestBodiesOnly() },
    children = children.map(PathNode::withSupportedRequestBodiesOnly),
)

private fun PathNode.withSupportedRequestBodiesOnly(): PathNode = copy(
    operations = operations.mapValues { [_, route] -> route.withSupportedRequestBodiesOnly() },
    children = children.map(PathNode::withSupportedRequestBodiesOnly),
)

private fun Route.withSupportedRequestBodiesOnly(): Route =
    copy(body = body?.withSupportedRequestBodiesOnly())

private fun Route.Bodies.withSupportedRequestBodiesOnly(): Route.Bodies? {
    val supportedTypes = types.filter { entry ->
        Route.Bodies.Variant(entry.key, entry.value).isSupportedForGeneration()
    }
    return if (supportedTypes.isEmpty()) null else copy(types = supportedTypes)
}

private fun Route.Bodies.Variant.isSupportedForGeneration(): Boolean =
    body.isSupportedForGeneration() && unsupportedFormCharsetOrNull() == null

/**
 * The `charset` a form body is declared in when it is not UTF-8. Ktor's `formUrlEncode` percent-encodes the UTF-8
 * bytes of every name and value on every target, which is how `application/x-www-form-urlencoded` is serialized
 * without a `charset` (the media type defines no parameters, and a form is UTF-8) or with `charset=utf-8`. A form
 * declared in another charset would go out labelled with it but in UTF-8, so it is not generated.
 */
private fun Route.Bodies.Variant.unsupportedFormCharsetOrNull(): String? =
    if (body !is Route.Body.FormUrlEncoded) null
    else contentType.parameter("charset")?.takeUnless { it.equals("UTF-8", ignoreCase = true) }

private fun Route.Body.isSupportedForGeneration(): Boolean =
    when (this) {
        is Route.Body.FormUrlEncoded -> isSupported
        is Route.Body.Multipart.Ref,
        is Route.Body.Multipart.Value -> true
        is Route.Body.OverloadedBody,
        is Route.Body.SetBody -> unsendableWildcardOrNull() == null
    }

/**
 * The media type range a body is still declared under after `typed` picked the concrete media type to send it as
 * (`Route.Body.SetBody.contentType`): a range none of whose media types the client produces for the body, such as an
 * object under the range of every `image` subtype. Bytes under such a range are sent as the media type the caller
 * supplies (`Route.Body.SetBody.callerContentTypeRange`).
 */
private fun Route.Body.unsendableWildcardOrNull(): ContentType? {
    val contentType = when (this) {
        is Route.Body.SetBody -> contentType.takeIf { callerContentTypeRange == null }
        is Route.Body.OverloadedBody -> contentType
        is Route.Body.FormUrlEncoded,
        is Route.Body.Multipart -> null
    }
    return contentType?.takeIf { it.isWildcard() }
}
