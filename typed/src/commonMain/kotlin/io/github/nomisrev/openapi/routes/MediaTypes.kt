package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.toModel
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
 * JSON for every other value, with the range's `charset` (text under the range of every `text` subtype with
 * `charset=iso-8859-1` is sent as `text/plain; charset=iso-8859-1`); other parameters of a range are not defined for
 * the media type chosen. The range itself is returned when it admits none of them: bytes under it are then sent as
 * the media type in the range the caller supplies (`Route.Body.SetBody.callerContentTypeRange`), and any other model
 * (an object under any `image` subtype) is reported by the renderer instead of sent.
 */
fun ContentType.requestContentType(model: Model): ContentType {
    if (!isWildcard()) return this
    val candidates = when (model) {
        is Model.ByteArray -> listOf(ContentType.Application.OctetStream, ContentType.Text.Plain)
        is Model.Primitive.String -> listOf(ContentType.Text.Plain, ContentType.Application.Json)
        else -> listOf(ContentType.Application.Json)
    }
    val range = withoutParameters()
    val chosen = candidates.firstOrNull { it.match(range) } ?: return this
    val charset = parameter("charset")
    return if (charset == null || chosen == ContentType.Application.OctetStream) chosen
    else chosen.withParameter("charset", charset)
}

/** How the content of a media type declared without `schema` is typed, by the media type alone. */
enum class MediaTypeKind {
    /** `application/json` and every `+json` media type: any JSON value. */
    Json,

    /** Every `text` media type, and the range of all of them: a string. */
    Text,

    /**
     * Everything else (`application/pdf`, `application/octet-stream`, XML, any media type range, ...): the spec says
     * nothing about the content. A response is handed to the caller as it is, and the caller builds the request body.
     */
    Other,
}

/**
 * The [MediaTypeKind] of this media type, which decides the type of its content when there is no `schema`
 * (`example` and `examples` never do). A media type range is never [MediaTypeKind.Json], even when it admits JSON, and
 * only the range of every `text` subtype is [MediaTypeKind.Text]. Shared by responses and request bodies.
 */
fun ContentType.mediaTypeKind(): MediaTypeKind = when {
    isWildcard() && !contentType.equals("text", ignoreCase = true) -> MediaTypeKind.Other
    isJsonLike() -> MediaTypeKind.Json
    contentType.equals("text", ignoreCase = true) -> MediaTypeKind.Text
    else -> MediaTypeKind.Other
}

/**
 * The model of content declared under this media type without `schema`: what `schema: {}` is for JSON, and what
 * `type: string` is for text. `null` for [MediaTypeKind.Other], which has no model: a response is
 * [Model.RawResponse], and a request body is built by the caller ([Route.Body.CallerSet]).
 */
context(ctx: Registry)
internal suspend fun ContentType.schemalessModelOrNull(name: NamingContext, context: SchemaContext): Model? =
    when (mediaTypeKind()) {
        MediaTypeKind.Json -> with(ctx) { ReferenceOr.value(Schema()).toModel(name, context) }
        MediaTypeKind.Text ->
            with(ctx) { ReferenceOr.value(Schema(type = Schema.Type.Basic.String)).toModel(name, context) }

        MediaTypeKind.Other -> null
    }
