package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.Model
import io.ktor.http.ContentType

/**
 * Whether content of this media type is JSON: `application/json` (parameters such as `charset` aside), or any
 * structured-syntax `+json` suffix (RFC 6839) such as `application/merge-patch+json` or `application/problem+json`.
 * A media type range ([isWildcard]) is not JSON, even when it admits JSON ([admitsJson]).
 *
 * Ktor's `ContentNegotiation` converts these with a converter registered for `application/json`, which matches every
 * `+json` subtype as well.
 */
fun ContentType.isJsonLike(): Boolean =
    match(ContentType.Application.Json) || contentSubtype.endsWith("+json", ignoreCase = true)

/**
 * Whether this is a media type range, with `*` as its type (any media type) or as its subtype (any subtype of `text`,
 * of `application`, ...), rather than one concrete media type.
 */
fun ContentType.isWildcard(): Boolean = contentType == "*" || contentSubtype == "*"

/** Whether this media type, or media type range, admits `application/json`. */
fun ContentType.admitsJson(): Boolean = ContentType.Application.Json.match(withoutParameters())

/**
 * The media type a client sends a request body of [model] as, when the body is declared under this media type.
 *
 * A concrete media type is sent as declared. A media type range only says which media types the server accepts and
 * is never a valid `Content-Type`, so the body is sent as the first media type in that range the client produces for
 * [model]: `application/octet-stream` (else `text/plain`) for bytes, `text/plain` (else a JSON string) for text, and
 * JSON for every other value. The range itself is returned when it admits none of them (an object under any `image`
 * subtype), which the renderer reports instead of sending.
 */
fun ContentType.requestContentType(model: Model): ContentType {
    if (!isWildcard()) return this
    val candidates = when (model) {
        is Model.ByteArray -> listOf(ContentType.Application.OctetStream, ContentType.Text.Plain)
        is Model.Primitive.String -> listOf(ContentType.Text.Plain, ContentType.Application.Json)
        else -> listOf(ContentType.Application.Json)
    }
    val range = withoutParameters()
    return candidates.firstOrNull { it.match(range) } ?: this
}
