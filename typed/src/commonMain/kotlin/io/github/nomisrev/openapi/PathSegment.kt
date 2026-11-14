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
     * nodes inside a composite. A tree navigates a composite one step per part ([toSteps]). [name] is the segment as written, `{id}.json`.
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

/**
 * One step of navigating a path: a node of the [ApiTree]. A segment between two `/` is one step, and a
 * [PathSegment.Composite] is one step per part ([toSteps]), so `{id}.json` is the step `id` and below it the step
 * `json`, as `{id}/json` is.
 *
 * What [joinedBy] and [trailing] hold is not part of any name, only of the identity of the step. [joinedBy] is the
 * text that joins the step to the one before it in the path (`/` between segments, `.` in `{id}.{extension}`, nothing in
 * `{a}{b}`), and [trailing] the text after the last step of a segment (`{id}.`). `{id}/json` and `{id}.json` have the
 * same names and different wire paths, so they are not one node. [fromComposite] is a parameter of a composite, which
 * is always a plain [PathSegment.Parameter] whatever its model, and so is not the same node as a parameter of another
 * shape (an overloaded union, another type) that has its name.
 */
data class PathStep(
    val segment: PathSegment,
    val joinedBy: String = "/",
    val trailing: String = "",
    val fromComposite: Boolean = false,
)

/**
 * The steps of this segment. A [PathSegment.Composite.Part.Param] part is a step; a [PathSegment.Composite.Part.Text] part is a step named after
 * its alphanumeric content (`.json` is `json`, `:cancel` is `cancel`) and the text around that content joins it to its
 * neighbours; a text without alphanumeric content (`.` in `{id}.{extension}`, `-` in `{lo}-{hi}`) is no step. A
 * fragment is read as text that starts with `#`. It is not sent, but it names a step and is part of its identity.
 */
fun PathSegment.toSteps(): List<PathStep> {
    if (this is PathSegment.Literal) return literalSteps()
    if (this !is PathSegment.Composite) return listOf(PathStep(this))
    val steps = mutableListOf<PathStep>()
    var pending = "/"
    fun textPiece(value: String) {
        val first = value.indexOfFirst(Char::isLetterOrDigit)
        if (first < 0) {
            pending += value
            return
        }
        val last = value.indexOfLast(Char::isLetterOrDigit)
        steps += PathStep(PathSegment.Literal(value.substring(first, last + 1)), pending + value.substring(0, first))
        pending = value.substring(last + 1)
    }
    // A `:` splits a text as it splits a literal segment (`{name}:cancel:now`), and is part of what joins the steps.
    fun text(value: String) {
        value.split(':').forEachIndexed { index, piece ->
            if (index > 0) pending += ":"
            textPiece(piece)
        }
    }
    for (part in parts) {
        when (part) {
            is PathSegment.Composite.Part.Text -> text(part.value)
            is PathSegment.Composite.Part.Fragment -> text("#${part.value}")
            is PathSegment.Composite.Part.Param -> {
                steps += PathStep(PathSegment.Parameter(part.name, part.model), pending, fromComposite = true)
                pending = ""
            }
        }
    }
    // Nothing to navigate (no parameter and no alphanumeric text): the segment stays one literal step, as written.
    if (steps.isEmpty()) return listOf(PathStep(PathSegment.Literal(name)))
    steps[steps.lastIndex] = steps.last().copy(trailing = pending)
    return steps
}

/**
 * A literal segment is one step, except that a `:` splits it as it splits the text of a composite (`entities:search`,
 * Google's custom verbs): one step per non-empty piece, the pieces joined on the wire by the `:`. Each piece is a
 * step as written, so `$batch:run` shares its first step with `$batch`. A `:` at the start of the segment is part of
 * what joins the first step to its parent (`:verb`), one at its end trails the last step (`entities:`), and a segment
 * of colons only has nothing to navigate: it stays one step, as written. No other character splits a literal.
 */
private fun PathSegment.Literal.literalSteps(): List<PathStep> {
    if (':' !in name) return listOf(PathStep(this))
    val steps = mutableListOf<PathStep>()
    var pending = "/"
    name.split(':').forEachIndexed { index, piece ->
        if (index > 0) pending += ":"
        if (piece.isEmpty()) return@forEachIndexed
        steps += PathStep(PathSegment.Literal(piece), pending)
        pending = ""
    }
    if (steps.isEmpty()) return listOf(PathStep(this))
    steps[steps.lastIndex] = steps.last().copy(trailing = pending)
    return steps
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
    is Model.RawResponse,
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
