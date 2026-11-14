package io.github.nomisrev.openapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface PathSegment {
    val name: String

    @Serializable
    @SerialName("Literal")
    data class Literal(override val name: String) : PathSegment

    @Serializable
    @SerialName("Parameter")
    data class Parameter(override val name: String, val model: Model) : PathSegment

    @Serializable
    @SerialName("FixedValue")
    data class FixedValue(
        val wireValue: String,
        val sourceName: String,
    ) : PathSegment {
        override val name: String
            get() = wireValue
    }

    /**
     * A segment of literal text and template expressions: `{id}.json`, `lists.{format}`, `{id}.{extension}`,
     * `{name}:cancel`, `v{version}` (OpenAPI 3.1.1, 4.8.2: `path-segment = 1*( path-literal / template-expression )`).
     * A segment that is exactly `{name}`, or has no braces, is a [Parameter] or a [Literal] instead.
     *
     * A [Part.Param] is a plain parameter whatever its model: a fixed enum or a union is not expanded into separate
     * nodes inside a composite. [name] is the segment as written, `{id}.json`.
     */
    @Serializable
    @SerialName("Composite")
    data class Composite(val parts: List<Part>) : PathSegment {
        override val name: String
            get() = parts.joinToString("") { it.template }

        val parameters: List<Part.Param>
            get() = parts.filterIsInstance<Part.Param>()

        @Serializable
        sealed interface Part {
            /** The part as it is written in the path template. */
            val template: String

            @Serializable
            @SerialName("Text")
            data class Text(val value: String) : Part {
                override val template: String
                    get() = value
            }

            @Serializable
            @SerialName("Param")
            data class Param(val name: String, val model: Model) : Part {
                override val template: String
                    get() = "{$name}"
            }

            /**
             * The `#fragment` of a path key (AWS uses `/tags/{arn}#tagKeys` to keep two operations on one path apart).
             * A fragment is never sent in a request, but it is part of the segment's identity and name.
             */
            @Serializable
            @SerialName("Fragment")
            data class Fragment(val value: String) : Part {
                override val template: String
                    get() = "#$value"
            }
        }
    }

    @Serializable
    @SerialName("OverloadedParameter")
    data class OverloadedParameter(
        override val name: String,
        val type: Model.Union,
    ) : PathSegment {
        val cases: List<Model.Union.Case>
            get() = type.cases
    }
}

private val DefaultPathParameterType = Model.Primitive.String(
    default = null,
    description = null,
    constraint = null,
    isNullable = false,
    title = null
)

/**
 * Parses an OpenAPI path template into ordered literal and parameter segments.
 *
 * The path is appended to the server URL as written (OpenAPI 3.1.1, 4.8.1), so `/owners/` and `/owners` are two
 * paths: a trailing slash (`/owners/`) or an empty segment (`/a//b`) is an empty [PathSegment.Literal], and the
 * request path is the segments joined by `/` (`owners/`, `a//b`). `/` has no segments. A leading empty segment
 * (`//owners`) is dropped: the request path is relative to the server URL, and a relative path cannot start with `/`.
 */
fun parsePathSegments(
    path: String,
    pathParamTypes: Map<String, Model>,
): List<PathSegment> {
    val relative = path.trimStart('/')
    if (relative.isEmpty()) return emptyList()
    // A fragment is never part of the request. It ends the key, and is kept on the last segment ([Composite.Part.Fragment]).
    val fragment = if ('#' in relative) relative.substringAfter('#') else null
    val segments = relative.substringBefore('#').split("/")
    return segments.mapIndexed { index, segment ->
        parseSegment(segment, fragment.takeIf { index == segments.lastIndex }, pathParamTypes)
    }
}

/**
 * A segment that is exactly `{name}` is a parameter and one without an expression a literal, as before composite
 * segments; anything else (`{id}.json`, `{a}{b}`, `v{version}`, or any segment with a fragment) is a [PathSegment.Composite].
 */
private fun parseSegment(segment: String, fragment: String?, pathParamTypes: Map<String, Model>): PathSegment {
    fun typeOf(name: String) = pathParamTypes[name] ?: DefaultPathParameterType
    if (fragment == null && segment.startsWith("{") && segment.endsWith("}") &&
        segment.count { it == '{' } == 1 && segment.count { it == '}' } == 1
    ) {
        val paramName = segment.removeSurrounding("{", "}")
        return typeOf(paramName).toPathSegment(paramName)
    }
    val tokens = tokenizeSegment(segment)
    if (fragment == null && tokens.none { it is SegmentToken.Expression }) return PathSegment.Literal(segment)
    val parts = tokens.map { token ->
        when (token) {
            is SegmentToken.Text -> PathSegment.Composite.Part.Text(token.value)
            is SegmentToken.Expression -> PathSegment.Composite.Part.Param(token.name, typeOf(token.name))
        }
    } + listOfNotNull(fragment?.let { PathSegment.Composite.Part.Fragment(it) })
    return PathSegment.Composite(parts)
}

private sealed interface SegmentToken {
    data class Text(val value: String) : SegmentToken
    data class Expression(val name: String) : SegmentToken
}

/**
 * Splits a segment into literal text and template expressions. A `{` with no closing `}` after it, a `}` with no
 * opening `{`, and a `{` inside an expression are literal text, and are reported by [pathTemplateProblems].
 */
private fun tokenizeSegment(segment: String): List<SegmentToken> {
    val tokens = mutableListOf<SegmentToken>()
    val text = StringBuilder()
    fun flush() {
        if (text.isNotEmpty()) tokens += SegmentToken.Text(text.toString())
        text.clear()
    }
    var index = 0
    while (index < segment.length) {
        val close = if (segment[index] == '{') segment.indexOf('}', index + 1) else -1
        val inner = if (close > 0) segment.substring(index + 1, close) else null
        if (inner != null && '{' !in inner) {
            flush()
            tokens += SegmentToken.Expression(inner)
            index = close + 1
        } else {
            text.append(segment[index])
            index++
        }
    }
    flush()
    return tokens
}

/** The names of the template expressions of [path], in order. */
fun pathTemplateParameterNames(path: String): List<String> =
    path.trimStart('/').substringBefore('#').split("/").flatMap { segment ->
        tokenizeSegment(segment).filterIsInstance<SegmentToken.Expression>().map { it.name }
    }

private val PlainExpressionName = Regex("[A-Za-z0-9_$][A-Za-z0-9_.\\-$\\[\\]@]*")

/**
 * What is wrong with the path template [path] of a path key that is still generated, for a warning: a `#fragment`
 * (not part of a path template; it is not sent), a `?` (not part of a path template; it is sent as written), an
 * unbalanced brace, an expression whose name is not a plain name (`{.format}`, `{insight_id:}`, `{}`), and an
 * expression without a declared path parameter in [declared], which is a required string.
 */
fun pathTemplateProblems(path: String, declared: Set<String>): List<String> {
    val problems = mutableListOf<String>()
    val relative = path.trimStart('/')
    if ('#' in relative) {
        problems += "Path '$path' has a fragment ('#${relative.substringAfter('#')}'), which a path template does not " +
            "have. The fragment is not sent: the request is for '${relative.substringBefore('#')}'. It stays in the " +
            "generated names, so paths that differ only by their fragment stay separate."
    }
    val template = relative.substringBefore('#')
    if ('?' in template) {
        problems += "Path '$path' contains '?', which a path template does not have. It is sent as written."
    }
    template.split("/")
        .filter { segment ->
            tokenizeSegment(segment).any { it is SegmentToken.Text && ('{' in it.value || '}' in it.value) }
        }
        .forEach { segment ->
            problems += "Path '$path' has an unbalanced brace in the segment '$segment'. It is sent as written."
        }
    val names = pathTemplateParameterNames(path).distinct()
    names.filterNot { PlainExpressionName.matches(it) }.forEach { name ->
        problems += "Path '$path' has the template expression '{$name}', which is not a plain parameter name. " +
            "It is generated as a path parameter named '$name'."
    }
    names.filterNot { it in declared }.forEach { name ->
        problems += "Path '$path' has the template expression '{$name}' without a declared path parameter. " +
            "It is generated as a required string parameter."
    }
    declared.filterNot { it in names }.forEach { name ->
        problems += "Path '$path' declares the path parameter '$name', which matches no template expression in the " +
            "path. It is not sent."
    }
    return problems
}

internal fun Model.toPathSegment(paramName: String): PathSegment = when (this) {
    is Model.Union -> toPathSegmentOrOverload(paramName)
    is Model.ByteArray,
    is Model.Collection,
    is Model.Dictionary,
    is Model.Date,
    is Model.DateTime,
    is Model.DiscriminatedObject,
    is Model.Enum,
    is Model.FreeFormJson,
    is Model.Object,
    is Model.Primitive,
    is Model.Reference,
    is Model.Uuid -> PathSegment.Parameter(paramName, this)
}

internal fun Model.Union.toPathSegmentOrOverload(paramName: String): PathSegment {
    if (!isFlattenablePathUnion()) return PathSegment.Parameter(paramName, this)
    return PathSegment.OverloadedParameter(paramName, this)
}

internal fun Model.Union.isFlattenablePathUnion(): Boolean =
    cases.all { case ->
        case.model is Model.Primitive ||
            case.model is Model.Enum ||
            case.model is Model.Uuid ||
            case.model is Model.Date ||
            case.model is Model.DateTime
    }
