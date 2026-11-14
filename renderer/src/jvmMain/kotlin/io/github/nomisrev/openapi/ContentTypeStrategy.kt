@file:Suppress("TooManyFunctions")
package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.StatusCodeOrRange
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.routes.Route.Bodies
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode

internal sealed interface ContentTypeStrategy {
    data object SingleContentType : ContentTypeStrategy

    data class SeparateMethods(
        val successContentTypes: List<ContentType>,
    ) : ContentTypeStrategy
}

internal sealed interface ResponseStatus {
    val returnType: Route.ReturnType
    val caseName: String
    val isSuccessStatusCode: Boolean
    val statusCode: HttpStatusCode?
    val matchingRange: IntRange?

    data class Exact(
        override val statusCode: HttpStatusCode,
        override val returnType: Route.ReturnType,
    ) : ResponseStatus {
        override val caseName: String get() = statusCode.toCaseName()
        override val isSuccessStatusCode: Boolean get() = statusCode.isSuccessStatusCode()
        override val matchingRange: IntRange? get() = null
    }

    data class Range(
        val responseRange: StatusCodeOrRange.Range,
        override val returnType: Route.ReturnType,
    ) : ResponseStatus {
        private val hundredRange: IntRange
            get() {
                val start = responseRange.value.first().digitToInt() * 100
                return start..(start + 99)
            }

        override val caseName: String get() = "Status${responseRange.value}"
        override val isSuccessStatusCode: Boolean get() = responseRange.value.startsWith('2')
        override val statusCode: HttpStatusCode? get() = null
        override val matchingRange: IntRange get() = hundredRange
    }
}

internal sealed interface ErrorCaseStrategy {
    data object NoContent : ErrorCaseStrategy

    data class SingleContentType(
        val contentType: ContentType,
        val model: Model,
    ) : ErrorCaseStrategy

    data class MultipleContentTypes(
        val variants: List<Pair<ContentType, Model>>,
    ) : ErrorCaseStrategy
}

internal sealed interface RequestBodyStrategy {
    data class Single(
        val variant: Route.Bodies.Variant,
    ) : RequestBodyStrategy

    data class SeparateOverloads(
        val variants: List<Route.Bodies.Variant>,
    ) : RequestBodyStrategy

    data class ClashingWithEnum(
        val clashing: List<Route.Bodies.Variant>,
        val unique: List<Route.Bodies.Variant>,
    ) : RequestBodyStrategy
}

private const val APPLICATION = "application"
@Suppress("MagicNumber")
private val NO_CONTENT_STATUS_CODE = setOf(204, 304)
@Suppress("MagicNumber")
private val SUCCESS_RANGE = 200..299

internal fun HttpStatusCode.isSuccessStatusCode(): Boolean = value in SUCCESS_RANGE

internal fun HttpStatusCode.isNoContentStatusCode(): Boolean = value in NO_CONTENT_STATUS_CODE

internal fun contentTypeToIdentifier(ct: ContentType, existingNames: Set<String> = emptySet()): String =
    contentTypeIdentifierBase(ct).uniqueName(existingNames)

internal fun contentTypeToMethodName(ct: ContentType, existingNames: Set<String> = emptySet()): String =
    contentTypeMethodNameBase(ct).uniqueName(existingNames)

internal fun contentTypeResponseTypeName(ct: ContentType, existingNames: Set<String> = emptySet()): String =
    "${contentTypeToIdentifier(ct, existingNames)}Response"

internal fun bodyTypeName(
    statusCode: HttpStatusCode,
    contentType: ContentType?,
    hasMultipleStatuses: Boolean,
    hasMultipleContentTypes: Boolean,
): String {
    val statusPrefix = if (hasMultipleStatuses) statusCode.toCaseName() else ""
    val contentTypeSuffix = if (hasMultipleContentTypes && contentType != null) {
        contentTypeToIdentifier(contentType)
    } else {
        ""
    }
    return "${statusPrefix}${contentTypeSuffix}Body"
}

internal fun defaultBodyTypeName(
    contentType: ContentType?,
    hasMultipleContentTypes: Boolean,
): String =
    buildString {
        append("Default")
        if (hasMultipleContentTypes && contentType != null) {
            append(contentTypeToIdentifier(contentType))
        }
        append("Body")
    }

internal fun Route.Returns.contentTypeStrategy(): ContentTypeStrategy {
    val successContentTypes = responseStatuses()
        .asSequence()
        .filter { it.isSuccessStatusCode }
        .flatMap { status ->
            status.returnType.types.keys.asSequence()
        }
        .distinctBy(ContentType::normalizedForComparison)
        .toList()
    return if (successContentTypes.size <= 1) {
        ContentTypeStrategy.SingleContentType
    } else {
        ContentTypeStrategy.SeparateMethods(successContentTypes)
    }
}

internal fun Route.Returns.responseStatuses(): List<ResponseStatus> =
    responses.map { [statusCode, returnType] -> ResponseStatus.Exact(statusCode, returnType) } +
        responseRanges.map { [range, returnType] -> ResponseStatus.Range(range, returnType) }

internal fun Route.ReturnType.preferredModel(): Model? {
    if (types.isEmpty()) return null
    val jsonEntry = types.entries.firstOrNull { it.key.match(ContentType.Application.Json) }
    return jsonEntry?.value ?: types.values.first()
}

/** The media type of [preferredModel]. */
internal fun Route.ReturnType.preferredContentType(): ContentType? {
    if (types.isEmpty()) return null
    return types.keys.firstOrNull { it.match(ContentType.Application.Json) } ?: types.keys.first()
}

internal fun Route.ReturnType.modelForContentType(contentType: ContentType): Model? {
    val mediaType = contentType.withoutParameters()
    return types.entries.firstOrNull { [key, _] ->
        val declared = key.withoutParameters()
        declared.match(mediaType) || mediaType.match(declared)
    }?.value
}

/**
 * The model this response declares under [contentType]'s media type, parameters such as `charset` aside: one status
 * may declare `application/json; charset=utf-8` and another `application/json`, both answering the same `Accept`.
 */
internal fun Route.ReturnType.modelForMediaType(contentType: ContentType): Model? {
    val mediaType = contentType.normalizedForComparison()
    return types.entries.firstOrNull { it.key.normalizedForComparison() == mediaType }?.value
}

internal fun classifyErrorStatus(
    statusCode: HttpStatusCode,
    returnType: Route.ReturnType,
): ErrorCaseStrategy {
    if (statusCode.value in NO_CONTENT_STATUS_CODE || returnType.types.isEmpty()) {
        return ErrorCaseStrategy.NoContent
    }

    val variants = returnType.types.entries
        .asSequence()
        .map { [contentType, model] -> contentType.normalizedForComparison() to model }
        .distinctBy { it.first }
        .toList()
    return when (variants.size) {
        0 -> ErrorCaseStrategy.NoContent
        1 -> ErrorCaseStrategy.SingleContentType(
            contentType = variants.single().first,
            model = variants.single().second,
        )

        else -> ErrorCaseStrategy.MultipleContentTypes(variants)
    }
}

/**
 * How the request bodies of an operation become `invoke` overloads. Bodies with the same Kotlin signature clash and
 * are chosen through a `RequestType` argument, except bodies whose media type the caller supplies
 * ([Route.Body.SetBody.callerContentTypeRange]): those with the same signature are one overload, whose `contentType`
 * argument may be in any of their ranges ([callerContentTypeArgumentOrNull]).
 */
internal fun Bodies.detectSignatureClashes(config: RenderConfig): RequestBodyStrategy {
    val variants = variants().distinctBy { variant ->
        if (variant.body.isSuppliedByCaller()) variant.body.toKotlinSignature(config)
        else variant
    }
    return when (variants.size) {
        0 -> RequestBodyStrategy.SeparateOverloads(emptyList())
        1 -> RequestBodyStrategy.Single(variants.single())
        else -> {
            val signatures = variants.associateWith { variant -> variant.body.toKotlinSignature(config) }
            val signatureCounts = signatures.values.groupingBy { it }.eachCount()
            val clashing = variants.filter { variant -> signatureCounts.getValue(signatures.getValue(variant)) > 1 }
            if (clashing.isEmpty()) {
                RequestBodyStrategy.SeparateOverloads(variants)
            } else {
                val unique = variants.filter { variant -> signatureCounts.getValue(signatures.getValue(variant)) == 1 }
                RequestBodyStrategy.ClashingWithEnum(clashing = clashing, unique = unique)
            }
        }
    }
}

/**
 * Whether the caller supplies the media type of this body, or builds the whole body: bytes under a media type range
 * ([Route.Body.SetBody.callerContentTypeRange]), and a [Route.Body.CallerSet] body. Such bodies with the same Kotlin
 * signature are one overload.
 */
internal fun Route.Body.isSuppliedByCaller(): Boolean =
    (this as? Route.Body.SetBody)?.callerContentTypeRange != null || this is Route.Body.CallerSet

internal fun Route.Body.toKotlinSignature(config: RenderConfig): String =
    when (this) {
        is Route.Body.CallerSet -> "HttpRequestBuilder.() -> Unit"

        is Route.Body.SetBody ->
            if (callerContentTypeRange == null) type.toTypeName(config).toString()
            else "${type.toTypeName(config)}|${ContentType::class.qualifiedName}"

        is Route.Body.OverloadedBody -> type.toTypeName(config).toString()

        is Route.Body.FormUrlEncoded ->
            parameters
                .asSequence()
                .map { parameter -> parameter.toKotlinSignaturePart(config) }
                .sorted()
                .joinToString(separator = "|")

        is Route.Body.Multipart.Value ->
            parameters
                .asSequence()
                .map { parameter -> parameter.toKotlinSignaturePart(config) }
                .sorted()
                .joinToString(separator = "|")

        is Route.Body.Multipart.Ref -> value.toTypeName(config).toString()
    }

private fun contentTypeIdentifierBase(ct: ContentType): String {
    val normalized = ct.toString().substringBefore(';').trim().lowercase()
    val slashIndex = normalized.indexOf('/')
    if (slashIndex < 0) return normalized.toPascalContentTypePart()

    val type = normalized.substring(0, slashIndex)
    val subtype = normalized.substring(slashIndex + 1)
    return if (type == APPLICATION) {
        subtype.toPascalContentTypePart()
    } else {
        type.toPascalContentTypePart() + subtype.toPascalContentTypePart()
    }
}

private fun contentTypeMethodNameBase(ct: ContentType): String =
    contentTypeIdentifierBase(ct).replaceFirstChar { it.lowercase() }

private fun ContentType.normalizedForComparison(): ContentType =
    ContentType.parse(toString().substringBefore(';').trim())

private fun String.toPascalContentTypePart(): String =
    replace('+', ' ')
        .splitToWords()
        .filter(String::isNotBlank)
        .joinToString(separator = "") { word ->
            word.lowercase().replaceFirstChar { char -> char.uppercase() }
        }
        .ifBlank { "Unnamed" }
        .ensureValidLeadingCharacter()

private fun String.ensureValidLeadingCharacter(): String =
    when {
        isBlank() -> "Unnamed"
        first().isDigit() -> "_$this"
        else -> this
    }

private fun String.uniqueName(existingNames: Set<String>): String {
    if (this !in existingNames) return this
    var index = 2
    while (true) {
        val candidate = "$this$index"
        if (candidate !in existingNames) return candidate
        index++
    }
}

private fun Route.Body.Multipart.FormData.toKotlinSignaturePart(config: RenderConfig): String {
    val typeName = type.toTypeName(config).copy(nullable = false)
    val hasDefault = !isRequired || type.defaultLiteral(config) != null
    return buildString {
        append(typeName)
        if (type.isNullable) append('?')
        if (!isRequired) append('~')
        if (hasDefault) append('=')
    }
}
