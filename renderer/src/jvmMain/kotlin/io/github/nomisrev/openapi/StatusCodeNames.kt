package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import io.github.nomisrev.openapi.routes.Route
import io.ktor.http.HttpStatusCode

/*
 * The case of a response is named after its status code.
 *
 * - A code Ktor names (`HttpStatusCode.allStatusCodes`) keeps its Ktor description in PascalCase: `Ok`, `NotFound`.
 * - A registered code Ktor lacks gets its IANA name from [iana]: `UnavailableForLegalReasons` for 451.
 * - Any other code is `Status<code>`: `Status480`. The OpenAPI Specification allows any code as a key of the
 *   Responses Object, and Ktor describes all of them as "Unknown Status Code".
 *
 * The decision is by the numeric value, never by the description.
 */

private val ktorNamedCodes: Set<Int> = HttpStatusCode.allStatusCodes.mapTo(mutableSetOf()) { it.value }

/** A registered status code that Ktor has no constant for: its IANA reason phrase and its case name. */
private data class IanaStatus(val description: String, val caseName: String)

private val iana: Map<Int, IanaStatus> = mapOf(
    103 to IanaStatus("Early Hints", "EarlyHints"),
    208 to IanaStatus("Already Reported", "AlreadyReported"),
    226 to IanaStatus("IM Used", "ImUsed"),
    421 to IanaStatus("Misdirected Request", "MisdirectedRequest"),
    428 to IanaStatus("Precondition Required", "PreconditionRequired"),
    451 to IanaStatus("Unavailable For Legal Reasons", "UnavailableForLegalReasons"),
    508 to IanaStatus("Loop Detected", "LoopDetected"),
    510 to IanaStatus("Not Extended", "NotExtended"),
    511 to IanaStatus("Network Authentication Required", "NetworkAuthenticationRequired"),
)

internal fun HttpStatusCode.toCaseName(): String = when {
    value in ktorNamedCodes -> description.pascalCaseWords()
    else -> iana[value]?.caseName ?: "Status$value"
}

/** `Request-URI Too Long` becomes `RequestUriTooLong`: any run of non-alphanumerics separates words. */
private fun String.pascalCaseWords(): String =
    split(Regex("[^A-Za-z0-9]+"))
        .filter(String::isNotEmpty)
        .joinToString("") { word -> word.lowercase().replaceFirstChar { it.uppercase() } }

private val StatusCodeType = ClassName("io.ktor.http", "HttpStatusCode")
private val StatusCodeCompanion = StatusCodeType.nestedClass("Companion")

/** The registered codes without a Ktor constant that the responses of this tree declare. */
internal fun ApiTree.collectIanaStatusCodes(into: MutableSet<Int>) {
    operations.values.forEach { it.collectIanaStatusCodes(into) }
    children.forEach { it.collectIanaStatusCodes(into) }
}

private fun PathNode.collectIanaStatusCodes(into: MutableSet<Int>) {
    operations.values.forEach { it.collectIanaStatusCodes(into) }
    children.forEach { it.collectIanaStatusCodes(into) }
}

private fun Route.collectIanaStatusCodes(into: MutableSet<Int>) {
    returns.responses.keys.mapNotNullTo(into) { code -> code.value.takeIf { it in iana && it !in ktorNamedCodes } }
}

/**
 * The support file with a named constant on `HttpStatusCode.Companion` for each of [codes], like Ktor's own
 * `HttpStatusCode.NotFound`:
 *
 * ```kotlin
 * public val HttpStatusCode.Companion.UnavailableForLegalReasons: HttpStatusCode
 *   get() = HttpStatusCode(451, "Unavailable For Legal Reasons")
 * ```
 *
 * There is one file per generated client, in the api package: status codes are a concern of the client only, and the
 * models never mention them. The properties are top-level, so a second copy in another package would be an
 * ambiguous name for a caller that imports both.
 */
internal fun generateStatusCodeSupport(config: RenderConfig, codes: Set<Int>): FileSpec {
    val builder = FileSpec.builder(config.apiPackage.toPackageName(), "StatusCodes")
    for (code in codes.sorted()) {
        val status = iana.getValue(code)
        builder.addProperty(
            PropertySpec.builder(status.caseName, StatusCodeType)
                .receiver(StatusCodeCompanion)
                .getter(
                    FunSpec.getterBuilder()
                        .addStatement("return %T(%L, %S)", StatusCodeType, code, status.description)
                        .build()
                )
                .build()
        )
    }
    return builder.build()
}
