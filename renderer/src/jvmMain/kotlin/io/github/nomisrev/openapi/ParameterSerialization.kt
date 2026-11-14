package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.joinToCode
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.parser.Style
import io.github.nomisrev.openapi.routes.Route
import io.ktor.http.encodeURLParameter

private val CookieMember = MemberName("io.ktor.client.request", "cookie")
private val EncodeURLParameterMember = MemberName("io.ktor.http", "encodeURLParameter")

/**
 * Where a form-style value (OpenAPI 3.1.1, 4.8.12.4 and 4.8.15; RFC 6570 `{?name}`) puts its name/value pairs.
 *
 * - [FormBody]: a `ParametersBuilder` that `formUrlEncode` percent-encodes as a whole, delimiters included.
 * - [Query]: the request's `url.encodedParameters`. Every name and value is percent-encoded as RFC 6570 does (all but
 *   `A-Z a-z 0-9 - . _ ~`; a space is `%20`, not the `+` only a form decoder reads as a space), while the `,` that
 *   joins the items of a `form` value stays literal and the `spaceDelimited` and `pipeDelimited` delimiters are `%20`
 *   and `%7C`, as the Style Examples show: a server splits a value by its delimiter before decoding the items, so a
 *   `,` inside an item (`%2C`) is told apart from the delimiter. `allowReserved: true` only permits leaving reserved
 *   characters as is; encoding them decodes to the same value, so it is encoded alike.
 * - [Cookie]: one cookie per pair. Ktor percent-encodes a cookie value, which cannot hold `,`, `;` or a space
 *   (RFC 6265).
 */
internal enum class PairTarget {
    FormBody,
    Query,
    Cookie,
}

/**
 * Appends the pairs of [model], held by [expr], under [name] to [target], laid out by [style], with the support rules
 * of the typed layer (`isFormUrlEncodable`, `isSerializableParameter`). A `null` value is left out, as an undefined
 * RFC 6570 variable is, and so is an empty array, which RFC 6570 treats as undefined.
 *
 * - A scalar is its string form ([formValueExpr]).
 * - An array is one `name=item` per item ([Route.Body.FormStyle.FormExploded]), or one `name` of its items joined by
 *   `,`, a space or `|` ([PairTarget] says how the delimiter is encoded).
 * - An object is one `key=value` per property ([Route.Body.FormStyle.FormExploded]), one `name[key]=value`
 *   ([Route.Body.FormStyle.DeepObject], `[` and `]` percent-encoded as the specification requires), or one `name` of
 *   its keys and values joined by the delimiter of its style (`color=R,100,G,200`). A map is laid out alike, by its
 *   entries.
 * - A union is the case it holds, each case the value class around its `value`; the value class the typed layer gives
 *   a named array or map schema is the array or map it wraps.
 */
@Suppress("LongParameterList", "CyclomaticComplexMethod")
internal fun CodeBlock.Builder.addPairsCode(
    config: RenderConfig,
    target: PairTarget,
    name: String,
    expr: String,
    model: Model,
    mayBeNull: Boolean,
    style: Route.Body.FormStyle,
) {
    when (val resolved = model.formModel(config)) {
        is Model.Union -> addUnionCode(config, name, expr, model, resolved, mayBeNull) { caseExpr, caseModel ->
            addPairsCode(config, target, name, caseExpr, caseModel, mayBeNull = false, style)
        }

        is Model.Collection -> {
            val item = resolved.inner
            if (style == Route.Body.FormStyle.FormExploded) {
                when (target) {
                    PairTarget.FormBody -> {
                        val itemExpr = item.formValueExpr(config, "it")
                        if (mayBeNull) beginControlFlow("if (%L != null)", expr)
                        if (itemExpr == "it") addStatement("appendAll(%S, %L)", name, expr)
                        else addStatement("appendAll(%S, %L.map·{ %L })", name, expr, itemExpr)
                        if (mayBeNull) endControlFlow()
                    }

                    PairTarget.Query, PairTarget.Cookie -> addStatement(
                        "%L%L.forEach·{ %L }",
                        expr,
                        if (mayBeNull) "?" else "",
                        target.pairCode(name, item.pairValueCode(config, target, "it")),
                    )
                }
            } else {
                val delimiter = target.delimiterOf(style)
                beginControlFlow(if (mayBeNull) "if (!%L.isNullOrEmpty())" else "if (%L.isNotEmpty())", expr)
                addStatement("%L", target.pairCode(name, joinCode(config, target, expr, item, delimiter)))
                endControlFlow()
            }
        }

        is Model.Object if resolved.isWrapperOfNonScalar -> {
            val [propertyName, property] = resolved.properties.entries.single()
            if (mayBeNull) beginControlFlow("if (%L != null)", expr)
            addPairsCode(config, target, name, "$expr.${propertyName.toParamName()}", property.model, false, style)
            if (mayBeNull) endControlFlow()
        }

        is Model.Dictionary -> {
            val entry = resolved.inner.pairValueCode(config, target, "it.value")
            val key = if (target == PairTarget.Query) CodeBlock.of("it.key.%M()", EncodeURLParameterMember)
            else CodeBlock.of("it.key")
            when (style) {
                Route.Body.FormStyle.FormExploded, Route.Body.FormStyle.DeepObject -> {
                    val entryName =
                        if (style == Route.Body.FormStyle.FormExploded) key
                        else CodeBlock.of(
                            "\"%L%L%L\"",
                            templateText(target.nameOf("$name[")),
                            key.interpolated(),
                            templateText(target.nameOf("]")),
                        )
                    val append = target.pairCode(entryName, entry)
                    addStatement("%L%L.forEach·{ %L }", expr, if (mayBeNull) "?" else "", append)
                }

                Route.Body.FormStyle.Form, Route.Body.FormStyle.SpaceDelimited, Route.Body.FormStyle.PipeDelimited -> {
                    val delimiter = target.delimiterOf(style)
                    beginControlFlow(if (mayBeNull) "if (!%L.isNullOrEmpty())" else "if (%L.isNotEmpty())", expr)
                    val pair =
                        CodeBlock.of("\"%L%L%L\"", key.interpolated(), templateText(delimiter), entry.interpolated())
                    val value = CodeBlock.of("%L.entries.joinToString(%S)·{ %L }", expr, delimiter, pair)
                    addStatement("%L", target.pairCode(name, value))
                    endControlFlow()
                }
            }
        }

        is Model.Object if !resolved.isScalarWrapper -> {
            if (mayBeNull) beginControlFlow("if (%L != null)", expr)
            when (style) {
                Route.Body.FormStyle.FormExploded, Route.Body.FormStyle.DeepObject ->
                    for ([propertyName, property] in resolved.properties) {
                        val pairName =
                            if (style == Route.Body.FormStyle.DeepObject) "$name[$propertyName]" else propertyName
                        val propertyExpr = "$expr.${propertyName.toParamName()}"
                        if (property.isOptionalNullable) {
                            // `Optional<T>`: `Absent` and `Null` have no form, `Value` is `T`.
                            val valueName = optionalValueName(propertyExpr)
                            beginControlFlow("%L?.let·{ %L ->", optionalValueOrNull(propertyExpr), valueName)
                            addPairsCode(
                                config = config,
                                target = target,
                                name = pairName,
                                expr = valueName,
                                model = property.model,
                                mayBeNull = false,
                                style = Route.Body.FormStyle.FormExploded,
                            )
                            endControlFlow()
                        } else {
                            addPairsCode(
                                config = config,
                                target = target,
                                name = pairName,
                                expr = propertyExpr,
                                model = property.model,
                                mayBeNull = property.model.isNullable || !property.isRequired,
                                style = Route.Body.FormStyle.FormExploded,
                            )
                        }
                    }

                Route.Body.FormStyle.Form, Route.Body.FormStyle.SpaceDelimited, Route.Body.FormStyle.PipeDelimited -> {
                    val delimiter = target.delimiterOf(style)
                    val value = objectCode(
                        config = config,
                        model = resolved,
                        expr = expr,
                        keySeparator = delimiter,
                        separator = delimiter,
                        key = { if (target == PairTarget.Query) it.encodeURLParameter() else it },
                        value = { it.encodedFor(target) },
                    ).expression()
                    addStatement("%L", target.pairCode(name, value))
                }
            }
            if (mayBeNull) endControlFlow()
        }

        else -> {
            if (mayBeNull) beginControlFlow("if (%L != null)", expr)
            addStatement("%L", target.pairCode(name, model.pairValueCode(config, target, expr)))
            if (mayBeNull) endControlFlow()
        }
    }
}

/** The statement that appends the pair [name]=[value] to this target. */
private fun PairTarget.pairCode(name: String, value: CodeBlock): CodeBlock =
    pairCode(CodeBlock.of("%S", nameOf(name)), value)

/** The statement that appends the pair of the `String` expression [name] (as [nameOf] writes it) and [value]. */
private fun PairTarget.pairCode(name: CodeBlock, value: CodeBlock): CodeBlock = when (this) {
    PairTarget.FormBody -> CodeBlock.of("append(%L, %L)", name, value)
    // `this.url`: a parameter named `url` is closer than the receiver of the request lambda and would shadow `url`.
    PairTarget.Query -> CodeBlock.of("this.url.encodedParameters.append(%L, %L)", name, value)
    PairTarget.Cookie -> CodeBlock.of("%M(%L, %L)", CookieMember, name, value)
}

/** The literal [name] as this target writes it: percent-encoded for [PairTarget.Query]. */
private fun PairTarget.nameOf(name: String): String = if (this == PairTarget.Query) name.encodeURLParameter() else name

/** A wrapper the typed layer gives a named array or map schema: a value class around the array or map. */
private val Model.Object.isWrapperOfNonScalar: Boolean
    get() = isTransparentWrapper && !isScalarWrapper && properties.size == 1

/** The delimiter that joins the items of an array or object of [style] in this target, encoded as it is written. */
private fun PairTarget.delimiterOf(style: Route.Body.FormStyle): String = when (style) {
    Route.Body.FormStyle.Form -> ","
    Route.Body.FormStyle.SpaceDelimited -> if (this == PairTarget.Query) "%20" else " "
    Route.Body.FormStyle.PipeDelimited -> if (this == PairTarget.Query) "%7C" else "|"
    Route.Body.FormStyle.FormExploded, Route.Body.FormStyle.DeepObject -> error("A $style value is not joined.")
}

/** The items of the array [expr] joined by [delimiter], each its value in [target]. */
private fun joinCode(
    config: RenderConfig,
    target: PairTarget,
    expr: String,
    item: Model,
    delimiter: String,
): CodeBlock {
    val itemValue = item.pairValueCode(config, target, "it")
    // `joinToString` writes every item with `toString()` already.
    return if (itemValue.toString() == "it" || itemValue.toString() == "it.toString()") {
        CodeBlock.of("%L.joinToString(%S)", expr, delimiter)
    } else {
        CodeBlock.of("%L.joinToString(%S)·{ %L }", expr, delimiter, itemValue)
    }
}

/** The value of the scalar [expr] of this model as [target] writes it: percent-encoded for [PairTarget.Query]. */
private fun Model.pairValueCode(config: RenderConfig, target: PairTarget, expr: String): CodeBlock =
    ScalarValue(formValueExpr(config, expr), needsEncoding(config)).encodedFor(target)

/** A scalar's `String` form [expr], and whether it can hold a character a query must percent-encode. */
private data class ScalarValue(val expr: String, val needsEncoding: Boolean) {
    fun encodedFor(target: PairTarget): CodeBlock = code(encode = target == PairTarget.Query)

    /** This value, percent-encoded with `encodeURLParameter` if [encode] and it can hold a character to encode. */
    fun code(encode: Boolean): CodeBlock =
        if (encode && needsEncoding) CodeBlock.of("%L.%M()", expr, EncodeURLParameterMember)
        else CodeBlock.of("%L", expr)
}

/**
 * Whether the string form of a scalar of this model can hold a character outside `A-Z a-z 0-9 - . _ ~`: a number,
 * boolean or UUID cannot.
 */
private fun Model.needsEncoding(config: RenderConfig): Boolean = when (val model = formModel(config)) {
    is Model.Primitive.Boolean,
    is Model.Primitive.Double,
    is Model.Primitive.Float,
    is Model.Primitive.Int,
    is Model.Primitive.Long,
    is Model.Uuid -> false

    is Model.Object -> model.properties.getValue("value").model.needsEncoding(config)
    else -> true
}

/**
 * A `String` value of generated code: the [Template] text of a string template (`;ids=$ids`) or an [Expression]
 * (`ids.joinToString(",")`). A path is one template that embeds either; elsewhere a template is quoted.
 */
private sealed interface StringCode {
    data class Template(val text: CodeBlock) : StringCode
    data class Expression(val code: CodeBlock) : StringCode

    /** This value as a `String` expression. */
    fun expression(): CodeBlock = when (this) {
        is Template -> CodeBlock.of("\"%L\"", text)
        is Expression -> code
    }

    /** This value as the text of a string template. */
    fun templateText(): CodeBlock = when (this) {
        is Template -> text
        is Expression -> code.interpolated()
    }
}

/**
 * The object [expr] as every property's [key], [keySeparator] and [value], joined by [separator], after [prefix]. An
 * optional property that is `null` is left out.
 */
@Suppress("LongParameterList")
private fun objectCode(
    config: RenderConfig,
    model: Model.Object,
    expr: String,
    keySeparator: String,
    separator: String,
    prefix: String = "",
    key: (String) -> String = { it },
    value: (ScalarValue) -> CodeBlock = { CodeBlock.of("%L", it.expr) },
): StringCode {
    fun piece(name: String, property: Model.Object.Property, valueExpr: String): CodeBlock {
        val v = ScalarValue(property.model.formValueExpr(config, valueExpr), property.model.needsEncoding(config))
        return CodeBlock.of("%L%L%L", templateText(key(name)), templateText(keySeparator), value(v).interpolated())
    }
    val properties = model.properties.entries.toList()
    return if (properties.all { [_, property] -> !property.model.isNullable && property.isRequired }) {
        val pieces = properties.map { [name, property] -> piece(name, property, "$expr.${name.toParamName()}") }
        StringCode.Template(CodeBlock.of("%L%L", templateText(prefix), pieces.joinToCode(templateText(separator))))
    } else {
        val pieces = properties.map { [name, property] ->
            val propertyExpr = "$expr.${name.toParamName()}"
            if (!property.model.isNullable && property.isRequired) {
                CodeBlock.of("\"%L\"", piece(name, property, propertyExpr))
            } else if (property.isOptionalNullable) {
                CodeBlock.of("%L?.let·{ \"%L\" }", optionalValueOrNull(propertyExpr), piece(name, property, "it"))
            } else {
                CodeBlock.of("%L?.let·{ \"%L\" }", propertyExpr, piece(name, property, "it"))
            }
        }
        val joined = CodeBlock.of("listOfNotNull(%L).joinToString(%S)", pieces.joinToCode(", "), separator)
        StringCode.Expression(if (prefix.isEmpty()) joined else CodeBlock.of("%S + %L", prefix, joined))
    }
}

/**
 * Appends the header [name] of [model], held by [expr], as `style: simple` (OpenAPI 3.1.1, 4.8.12.4; RFC 6570
 * `{name}`): a scalar is its string form, an array its items joined by `,`, an object `key,value` pairs joined by `,`
 * or, with `explode`, `key=value` pairs joined by `,`. A header value is not percent-encoded. A `null` value and an
 * empty array, undefined in RFC 6570, leave the header out.
 */
@Suppress("LongParameterList")
internal fun CodeBlock.Builder.addHeaderCode(
    config: RenderConfig,
    headerMember: MemberName,
    name: String,
    expr: String,
    model: Model,
    mayBeNull: Boolean,
    explode: Boolean,
) {
    val value = { valueExpr: String, valueModel: Model ->
        expansionCode(config, Style.simple, explode, name, valueExpr, valueModel, encode = false).expression()
    }
    when (val resolved = model.formModel(config)) {
        is Model.Union -> addUnionCode(config, name, expr, model, resolved, mayBeNull) { caseExpr, caseModel ->
            addHeaderCode(config, headerMember, name, caseExpr, caseModel, mayBeNull = false, explode)
        }

        is Model.Object if resolved.isWrapperOfNonScalar -> {
            val [propertyName, property] = resolved.properties.entries.single()
            if (mayBeNull) beginControlFlow("if (%L != null)", expr)
            val valueExpr = "$expr.${propertyName.toParamName()}"
            addHeaderCode(config, headerMember, name, valueExpr, property.model, mayBeNull = false, explode)
            if (mayBeNull) endControlFlow()
        }

        is Model.Collection, is Model.Dictionary -> {
            beginControlFlow(if (mayBeNull) "if (!%L.isNullOrEmpty())" else "if (%L.isNotEmpty())", expr)
            addStatement("%M(%S, %L)", headerMember, name, value(expr, model))
            endControlFlow()
        }

        else -> {
            if (mayBeNull) beginControlFlow("if (%L != null)", expr)
            addStatement("%M(%S, %L)", headerMember, name, value(expr, model))
            if (mayBeNull) endControlFlow()
        }
    }
}

/**
 * The expansion of the non-null [expr] of [model] as a `simple`, `label` or `matrix` value [name] (OpenAPI 3.1.1,
 * 4.8.12.4; RFC 6570 `{name}`, `{.name}`, `{;name}`), as a path segment or header writes it:
 *
 * - `simple`: `blue`, `blue,black`, `R,100,G,200` (exploded `R=100,G=200`).
 * - `label`: `.blue`, `.blue,black` (exploded `.blue.black`), `.R,100,G,200` (exploded `.R=100.G=200`).
 * - `matrix`: `;color=blue`, `;color=blue,black` (exploded `;color=blue;color=black`), `;color=R,100,G,200`
 *   (exploded `;R=100;G=200`).
 *
 * An empty array, undefined in RFC 6570, expands to nothing, so a path segment of one is empty.
 *
 * With [encode] (a path segment), every scalar and array item is percent-encoded as RFC 6570 does, all but
 * `A-Z a-z 0-9 - . _ ~`, so a `/`, `?`, `#` or `%` in a value stays inside its segment, while the `,`, `.`, `;` and
 * `=` written by the style stay literal. The client does not generate path objects or maps, so those are not encoded.
 * Its spaces are `·`: a path is one string template, which KotlinPoet must not wrap.
 */
@Suppress("LongParameterList")
private fun expansionCode(
    config: RenderConfig,
    style: Style,
    explode: Boolean,
    name: String,
    expr: String,
    model: Model,
    encode: Boolean,
): StringCode {
    val [prefix, separator] = when (style) {
        Style.label -> "." to "."
        Style.matrix -> ";" to ";"
        else -> "" to ","
    }
    val named = if (style == Style.matrix) "${if (encode) name.encodeURLParameter() else name}=" else ""
    val valueCode = { valueModel: Model, valueExpr: String ->
        ScalarValue(valueModel.formValueExpr(config, valueExpr), valueModel.needsEncoding(config)).code(encode)
    }
    return when (val resolved = model.formModel(config)) {
        is Model.Collection -> {
            val itemValue = valueCode(resolved.inner, "it")
            if (explode && style != Style.simple) {
                val item = CodeBlock.of("%L%L", templateText(prefix + named), itemValue.interpolated())
                StringCode.Expression(CodeBlock.of("%L.joinToString(%S)·{·\"%L\"·}", expr, "", item))
            } else {
                val itemCode = itemValue.toString()
                val joined =
                    if (itemCode == "it" || itemCode == "it.toString()") CodeBlock.of("%L.joinToString(%S)", expr, ",")
                    else CodeBlock.of("%L.joinToString(%S)·{·%L·}", expr, ",", itemValue)
                if (prefix.isEmpty()) StringCode.Expression(joined)
                else StringCode.Expression(
                    CodeBlock.of(
                        "if·(%L.isEmpty())·\"\"·else·\"%L%L%L\"",
                        expr,
                        templateText(prefix),
                        templateText(named),
                        joined.interpolated(),
                    )
                )
            }
        }

        is Model.Dictionary -> {
            val entry = CodeBlock.of("%L", resolved.inner.formValueExpr(config, "it.value")).interpolated()
            val pair = CodeBlock.of("\"\${it.key}%L%L\"", if (explode) "=" else ",", entry)
            val joined =
                CodeBlock.of("%L.entries.joinToString(%S)·{ %L }", expr, if (explode) separator else ",", pair)
            if (prefix.isEmpty()) StringCode.Expression(joined)
            else StringCode.Expression(
                CodeBlock.of("if (%L.isEmpty()) \"\" else \"%L%L\"", expr, templateText(prefix + named), joined.interpolated())
            )
        }

        is Model.Object if !resolved.isScalarWrapper ->
            if (explode) objectCode(config, resolved, expr, keySeparator = "=", separator = separator, prefix = prefix)
            else objectCode(config, resolved, expr, keySeparator = ",", separator = ",", prefix = prefix + named)

        else -> {
            val value = valueCode(model, expr)
            if (prefix.isEmpty()) StringCode.Expression(value)
            else StringCode.Template(CodeBlock.of("%L%L", templateText(prefix + named), value.interpolated()))
        }
    }
}

/**
 * The path parameter [this] as the operation reads it, `this.id` (a keyword as ``this.`object` ``). A path parameter is
 * a property of the operation class, and the `invoke` parameters (a query parameter, the property of a flattened body
 * or form) are nearer declarations of the same name, so a bare `id` could be one of those.
 */
private fun String.pathProperty(): String {
    val name = toCamelCase()
    return "this.${if (name.isValidParamName()) name else "`$name`"}"
}

/**
 * The path of this route as a `String` template: every parameter segment is the expansion of its `style` and
 * `explode` ([expansionCode]), percent-encoded as a path segment (`${this.id.encodeURLParameter()}`, a number `${this.id}`). A
 * fixed enum segment is its encoded wire value and an overloaded parameter, already its string, is encoded alike. The
 * literal segments of the path template are written as they are, so the empty one of a trailing slash or a double
 * slash keeps its `/` (`owners/`, `a//b`).
 *
 * The path is relative, `owners/$id` and `""` for `/`, so Ktor's `defaultRequest` appends it to the base URL path
 * (OpenAPI 3.1.1, 4.8.1) rather than replacing that path, as an absolute `/owners/$id` would.
 */
internal fun Route.pathLiteral(config: RenderConfig): CodeBlock {
    fun parameter(name: String, model: Model): CodeBlock {
        val variable = name.pathProperty()
        val input = parameters.firstOrNull { it.input == Parameter.Input.Path && it.name == name }
        return if (input == null || !input.isSupported) CodeBlock.of("\${%L}", variable)
        else expansionCode(
            config = config,
            style = if (input.contentType != null) Style.simple else input.style,
            explode = input.explode,
            name = name,
            expr = variable,
            model = model.publicInputModelOrSelf(),
            encode = true,
        ).templateText()
    }

    val inner = segments.map { segment ->
        when (segment) {
            is PathSegment.FixedValue -> CodeBlock.of("%L", templateText(segment.wireValue.encodeURLParameter()))
            // `·` keeps KotlinPoet from wrapping a line inside the template.
            is PathSegment.Literal -> CodeBlock.of("%L", templateText(segment.name).replace(' ', '·'))
            is PathSegment.OverloadedParameter ->
                CodeBlock.of("%L.%M()", segment.name.pathProperty(), EncodeURLParameterMember).interpolated()
            is PathSegment.Parameter -> parameter(segment.name, segment.model)
            // Each parameter is expanded and encoded on its own, the text around it is written as it is. A fragment is not sent.
            is PathSegment.Composite -> segment.parts.map { part ->
                when (part) {
                    is PathSegment.Composite.Part.Text ->
                        CodeBlock.of("%L", templateText(part.value).replace(' ', '·'))
                    is PathSegment.Composite.Part.Param -> parameter(part.name, part.model)
                    is PathSegment.Composite.Part.Fragment -> CodeBlock.of("")
                }
            }.joinToCode("")
        }
    }
    return CodeBlock.of("\"%L\"", inner.joinToCode("/"))
}

/**
 * Writes `when ([expr]) { is Case -> … }` for the union [resolved] that [model] is, calling [caseCode] with each
 * case's `value` and model: every case the typed layer supports is a value class around its value. An inline union is the
 * class `Name` the operation nests for the parameter [name], a component its own class.
 */
@Suppress("LongParameterList")
private fun CodeBlock.Builder.addUnionCode(
    config: RenderConfig,
    name: String,
    expr: String,
    model: Model,
    resolved: Model.Union,
    mayBeNull: Boolean,
    caseCode: CodeBlock.Builder.(caseExpr: String, caseModel: Model) -> Unit,
) {
    val isInline = (model as? Model.ContextHolder)?.context?.head is NamingContext.Path
    val unionType =
        if (isInline) CodeBlock.of("%L", name.toPascalCase())
        else CodeBlock.of("%T", model.toTypeName(config).copy(nullable = false))
    val colliding = resolved.cases.collidingSingleDiscriminatorTags()
    if (mayBeNull) beginControlFlow("if (%L != null)", expr)
    beginControlFlow("when (%L)", expr)
    for (case in resolved.cases) {
        beginControlFlow("is %L.%L ->", unionType, case.caseSimpleName(config, colliding))
        caseCode("$expr.value", case.model)
        endControlFlow()
    }
    endControlFlow()
    if (mayBeNull) endControlFlow()
}

/** The model a form or parameter value of this model is: a [Model.Reference] is the model it names. */
internal fun Model.formModel(config: RenderConfig): Model =
    if (this is Model.Reference) {
        requireNotNull(config.resolve(this)) {
            "A form field or parameter references ${context}, which is not rendered."
        }
            .formModel(config)
    } else this

/**
 * The `String` form value of the scalar [expr] of this model: a string as is, a `JsonPrimitive` as its `content`, an enum as its wire value (see
 * `enum-wire-tostring`), a number, boolean, date, date-time or UUID as its `toString()` (`1`, `1.5`, `true`,
 * `2024-01-31`, `2024-01-31T10:00:00Z`), and a scalar wrapper as the value it wraps.
 */
internal fun Model.formValueExpr(config: RenderConfig, expr: String): String =
    when (val model = formModel(config)) {
        is Model.Primitive.String -> expr
        is Model.Enum -> model.wireTextExpr(expr)
        // `true`, `0` and `a` for the JSON primitives `true`, `0` and `"a"`.
        is Model.FreeFormJson -> "$expr.content"
        is Model.Object -> model.properties.getValue("value").model.formValueExpr(config, "$expr.value")
        else -> "$expr.toString()"
    }

private val Identifier = Regex("[A-Za-z_][A-Za-z0-9_]*")

/** This `String` expression inside a string template: `$name` for a variable or its `toString()`, else `${…}`. */
private fun CodeBlock.interpolated(): CodeBlock {
    val code = toString()
    // A template writes its expressions with `toString()` already.
    val value = code.removeSuffix(".toString()")
    return when {
        Identifier.matches(value) -> CodeBlock.of("$%L", value)
        value != code -> CodeBlock.of("\${%L}", value)
        else -> CodeBlock.of("\${%L}", this)
    }
}

/** [text] as the literal text of a Kotlin string template. */
private fun templateText(text: String): String =
    text.replace("\\", "\\\\").replace("\"", "\\\"").replace("$", "\\$")

/** The name of the lambda parameter that holds the value of the optional and nullable property [propertyExpr]. */
private fun optionalValueName(propertyExpr: String): String =
    propertyExpr.split('.').joinToString("") { it.trim('`').replaceFirstChar(Char::uppercase) }
        .replaceFirstChar(Char::lowercase) + "Value"
