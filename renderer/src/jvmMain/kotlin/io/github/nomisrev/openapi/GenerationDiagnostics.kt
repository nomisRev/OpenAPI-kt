package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.FileSpec
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.routes.isWildcard
import io.ktor.http.ContentType
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive

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

internal fun ApiTree.generationDiagnostics(config: RenderConfig): List<GenerationDiagnostic> =
    requestBodyDiagnostics() + ignoredParameterDiagnostics() + parameterDiagnostics() +
        renamedArgumentDiagnostics(config.withOptionalTypeName(this).withNamedModels(models)) +
        children.renamedPathDiagnostics(parentPath = "") + renamedServerDiagnostics() + propertyOverrideDiagnostics() + typedWarningDiagnostics()

/** A warning for each server whose name is a last resort: the same name as an earlier server, or `Custom` ([serverNames]). */
private fun ApiTree.renamedServerDiagnostics(): List<GenerationDiagnostic> =
    if (servers.size < 2) emptyList()
    else servers.serverNames().warnings.map { GenerationDiagnostic(GenerationDiagnosticSeverity.Warning, it) }

/** The warnings `typed` recorded for schemas it generates differently from how they are declared. */
private fun ApiTree.typedWarningDiagnostics(): List<GenerationDiagnostic> =
    warnings.map { GenerationDiagnostic(GenerationDiagnosticSeverity.Warning, it) }

/**
 * A warning for each property a discriminated subtype redeclares so that no value satisfies its declaration and the
 * inherited one: `typed` generates the subtype with its own declaration ([Model.DiscriminatedObject.PropertyOverride]),
 * as a subclass overrides a property, where `allOf` taken literally admits no value with the property. A schema that
 * is generated for both the read and the write context is reported once.
 */
private fun ApiTree.propertyOverrideDiagnostics(): List<GenerationDiagnostic> =
    models.filterIsInstance<Model.DiscriminatedObject>()
        .flatMap { it.overriddenProperties }
        .distinct()
        .map { override ->
            GenerationDiagnostic(
                severity = GenerationDiagnosticSeverity.Warning,
                message = "Discriminated subtype ${override.schema} overrides property '${override.property}' it " +
                    "inherits from ${override.parent}: it declares ${override.declared}, which contradicts the " +
                    "inherited ${override.inherited} (${override.reason}). ${override.schema}'s declaration is used.",
            )
        }

private fun ApiTree.renamedArgumentDiagnostics(config: RenderConfig): List<GenerationDiagnostic> =
    operations.values.flatMap { it.renamedArgumentDiagnostics(config) } +
        children.flatMap { it.renamedArgumentDiagnostics(config) }

private fun PathNode.renamedArgumentDiagnostics(config: RenderConfig): List<GenerationDiagnostic> =
    operations.values.flatMap { it.renamedArgumentDiagnostics(config) } +
        children.flatMap { it.renamedArgumentDiagnostics(config) }

/**
 * A warning for each argument of an operation whose Kotlin name is not the one its wire name gives it, because another
 * argument has that name ([operationArguments]): the parameter, property or field keeps its wire name, whatever the
 * argument is called. The operation without a body, which an optional body also gets, is not looked at apart: it has
 * the arguments of the others.
 */
private fun Route.renamedArgumentDiagnostics(config: RenderConfig): List<GenerationDiagnostic> =
    withSupportedRequestBodiesOnly().overloadArguments(config)
        .flatMap { it.renames }
        .distinct()
        .map { rename ->
            GenerationDiagnostic(
                severity = GenerationDiagnosticSeverity.Warning,
                message = "${method.value} $path: ${rename.subject} is generated as argument " +
                    "'${rename.name.unescapeBackticks()}': ${rename.reason.explanation}",
            )
        }

/** A warning for each node whose Kotlin name is taken by an earlier sibling, and is suffixed ([uniqueNames]). */
private fun List<PathNode>.renamedPathDiagnostics(parentPath: String): List<GenerationDiagnostic> =
    zip(uniqueNames()).flatMap { [node, name] ->
        val path = "$parentPath${node.joinedBy}${node.segment.templateText()}${node.trailing}"
        val generatedAs = "Path '$path' is generated as '${name.memberName.unescapeBackticks()}' (class ${name.simpleName})"
        val reason = when {
            name.suffix != null -> "an earlier path segment has the same Kotlin name."
            name.separator != null ->
                "another path segment has the same Kotlin name, so this one is named with the text that joins it " +
                    "('${name.separator}')."
            else -> null
        }
        val renamed = listOfNotNull(reason).map { GenerationDiagnostic(GenerationDiagnosticSeverity.Warning, "$generatedAs: $it") }
        renamed + node.children.renamedPathDiagnostics(path)
    }

private fun PathSegment.templateText(): String = when (this) {
    is PathSegment.FixedValue -> wireValue
    is PathSegment.Literal -> name
    is PathSegment.Parameter -> "{$name}"
    is PathSegment.OverloadedParameter -> "{$name}"
    is PathSegment.Composite -> name
}

private fun ApiTree.requestBodyDiagnostics(): List<GenerationDiagnostic> =
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
                    "$prefix contains form fields [$fieldDescription] that have no supported form serialization " +
                        "for their type and encoding (`style`, `explode`), and no alternative supported request body " +
                        "variant exists.",
            )
        } else {
            GenerationDiagnostic(
                severity = GenerationDiagnosticSeverity.Warning,
                message =
                    "Skipping unsupported form overload for $prefix because fields [$fieldDescription] have no " +
                        "supported form serialization for their type and encoding (`style`, `explode`).",
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
        is Route.Body.Multipart.Value,
        is Route.Body.CallerSet -> true
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
        is Route.Body.CallerSet,
        is Route.Body.Multipart -> null
    }
    return contentType?.takeIf { it.isWildcard() }
}

private fun ApiTree.ignoredParameterDiagnostics(): List<GenerationDiagnostic> =
    operations.values.flatMapTo(mutableListOf(), Route::ignoredParameterDiagnostics) +
        children.flatMapTo(mutableListOf(), PathNode::ignoredParameterDiagnostics)

private fun PathNode.ignoredParameterDiagnostics(): List<GenerationDiagnostic> =
    operations.values.flatMapTo(mutableListOf(), Route::ignoredParameterDiagnostics) +
        children.flatMapTo(mutableListOf(), PathNode::ignoredParameterDiagnostics)

/**
 * OpenAPI says a header parameter named `Accept`, `Content-Type` or `Authorization` SHALL be ignored, so it is not an
 * argument of the generated operation. Its definition can still carry information the client does not act on (the
 * media types an upload accepts, that a request must be authorized), so the generator reports each one as a warning
 * rather than dropping it silently.
 */
private fun Route.ignoredParameterDiagnostics(): List<GenerationDiagnostic> =
    ignoredParameters.map { parameter ->
        val allowed = parameter.schema?.valueOrNull()?.enum
            ?.filterNot { it is JsonNull }
            ?.map { (it as? JsonPrimitive)?.content ?: it.toString() }
            ?.takeIf { it.isNotEmpty() }
            ?.let { values -> " (declared values: ${values.joinToString(", ")})" }
            .orEmpty()
        GenerationDiagnostic(
            severity = GenerationDiagnosticSeverity.Warning,
            message =
                "Ignoring header parameter ${parameter.name}$allowed of ${method.value} $path: OpenAPI ignores " +
                    "a header parameter named Accept, Content-Type or Authorization; ${parameter.ignoredBecause()}.",
        )
    }

private fun Parameter.ignoredBecause(): String = when {
    name.equals("Content-Type", ignoreCase = true) -> "the Content-Type header is sent from the request body"
    name.equals("Accept", ignoreCase = true) -> "the client chooses the Accept header from the declared responses"
    else -> "authorization is described by security schemes and configured on the HttpClient"
}

private fun ApiTree.parameterDiagnostics(): List<GenerationDiagnostic> =
    operations.values.flatMapTo(mutableListOf(), Route::parameterDiagnostics) +
        children.flatMapTo(mutableListOf(), PathNode::parameterDiagnostics)

private fun PathNode.parameterDiagnostics(): List<GenerationDiagnostic> =
    operations.values.flatMapTo(mutableListOf(), Route::parameterDiagnostics) +
        children.flatMapTo(mutableListOf(), PathNode::parameterDiagnostics)

/**
 * A parameter is sent only when the client writes the serialization its type, location, `style` and `explode`
 * define (`isSerializableParameter` in `typed`). Any other parameter fails generation, rather than sending the
 * `toString()` of its Kotlin value or a layout the server reads as something else.
 */
private fun Route.parameterDiagnostics(): List<GenerationDiagnostic> =
    parameters.filterNot(Route.Input::isSupported).map { parameter ->
        val serialization =
            if (parameter.contentType != null) "content ${parameter.contentType}"
            else "style ${parameter.style}, explode ${parameter.explode}"
        GenerationDiagnostic(
            severity = GenerationDiagnosticSeverity.Error,
            message =
                "${method.value} $path parameter ${parameter.name} (in ${parameter.input.value}, $serialization) has " +
                    "no supported serialization for its type, location, `style` and `explode`.",
        )
    }
