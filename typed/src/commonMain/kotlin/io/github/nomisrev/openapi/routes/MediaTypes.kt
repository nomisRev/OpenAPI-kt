package io.github.nomisrev.openapi.routes

import io.ktor.http.ContentType

/**
 * Whether content of this media type is JSON: `application/json` (parameters such as `charset` aside), or any
 * structured-syntax `+json` suffix (RFC 6839) such as `application/merge-patch+json` or `application/problem+json`.
 *
 * Ktor's `ContentNegotiation` converts these with a converter registered for `application/json`, which matches every
 * `+json` subtype as well.
 */
fun ContentType.isJsonLike(): Boolean =
    ContentType.Application.Json.match(withoutParameters()) || contentSubtype.endsWith("+json", ignoreCase = true)
