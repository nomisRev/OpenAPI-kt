package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.routes.Route
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode

private val ContentTypeType = ClassName("io.ktor.http", "ContentType")
private val MultiPartFormDataContentType =
    ClassName("io.ktor.client.request.forms", "MultiPartFormDataContent")
private val ParametersType = ClassName("io.ktor.http", "Parameters")
private val ResponseExceptionType = ClassName("io.ktor.client.plugins", "ResponseException")

private val BodyMember = MemberName("io.ktor.client.call", "body")
private val ParameterMember = MemberName("io.ktor.client.request", "parameter")
private val HeaderMember = MemberName("io.ktor.client.request", "header")
private val CookieMember = MemberName("io.ktor.client.request", "cookie")
private val SetBodyMember = MemberName("io.ktor.client.request", "setBody")
private val ContentTypeFunMember = MemberName("io.ktor.http", "contentType")
private val FormDataMember = MemberName("io.ktor.client.request.forms", "formData")
private val FormUrlEncodeMember = MemberName("io.ktor.http", "formUrlEncode")
private val AppendMember = MemberName("io.ktor.client.request.forms", "append")

internal data class AccumulatedParam(
    val name: String,
    val type: Model,
    val storeAsString: Boolean = false,
)

internal fun PathNode.collectHttpMethods(): Set<String> {
    val methods = operations.keys.mapTo(mutableSetOf()) { it.value.lowercase() }
    for (child in children) {
        methods.addAll(child.collectHttpMethods())
    }
    return methods
}

internal fun PathNode.accumulatedParams(parentAccumulatedParams: List<AccumulatedParam>): List<AccumulatedParam> =
    when (val segment = segment) {
        is PathSegment.OverloadedParameter -> parentAccumulatedParams + AccumulatedParam(
            name = segment.name,
            type = segment.type,
            storeAsString = true,
        )

        is PathSegment.Parameter -> {
            parentAccumulatedParams + AccumulatedParam(
                name = segment.name,
                type = segment.model,
            )
        }

        is PathSegment.FixedValue,
        is PathSegment.Literal -> parentAccumulatedParams
    }

internal fun TypeSpec.Builder.addClientConstructorAndState(
    config: RenderConfig,
    accumulatedParams: List<AccumulatedParam>,
) {
    val constructor = FunSpec.constructorBuilder()
        .addModifiers(KModifier.INTERNAL)
        .addParameter("client", HttpClientType)
    for (param in accumulatedParams) {
        val typeName = if (param.storeAsString) STRING else param.type.toTypeName(config)
        constructor.addParameter(param.name.toCamelCase(), typeName)
    }
    primaryConstructor(constructor.build())

    addProperty(
        PropertySpec.builder("client", HttpClientType)
            .addModifiers(KModifier.PRIVATE)
            .initializer("client")
            .build()
    )
    for (param in accumulatedParams) {
        val paramName = param.name.toCamelCase()
        val typeName = if (param.storeAsString) STRING else param.type.toTypeName(config)
        addProperty(
            PropertySpec.builder(paramName, typeName)
                .addModifiers(KModifier.PRIVATE)
                .initializer(paramName)
                .build()
        )
    }
}

internal fun PathSegment.addConcreteNavigationMember(
    builder: TypeSpec.Builder,
    childClassName: ClassName,
    currentAccumulatedParams: List<AccumulatedParam>,
    parentClassName: ClassName,
    config: RenderConfig,
) {
    when (this) {
        is PathSegment.FixedValue -> builder.addProperty(
            PropertySpec.builder(name.toParamName(), childClassName)
                .initializer("%T(%L)", childClassName, currentAccumulatedParams.constructorArgs())
                .build()
        )

        is PathSegment.Literal -> builder.addProperty(
            PropertySpec.builder(name.toParamName(), childClassName)
                .initializer("%T(%L)", childClassName, currentAccumulatedParams.constructorArgs())
                .build()
        )

        is PathSegment.OverloadedParameter -> {
            val paramName = name.toCamelCase()
            val enumClassNames = enumClassNames(parentClassName, config)
            enumClassNames.forEach { (case, enumClassName) ->
                val enumModel = case.model as Model.Enum
                builder.addType(enumModel.toTypeSpec(config, nameOverride = enumClassName.simpleName))
            }

            val emittedTypes = mutableSetOf<TypeName>()
            for (case in cases) {
                val caseTypeName = when (case.model) {
                    is Model.Enum -> requireNotNull(enumClassNames[case])
                    else -> case.model.toTypeName(config)
                }
                if (!emittedTypes.add(caseTypeName)) continue

                val functionBuilder = FunSpec.builder(paramName)
                    .addParameter(paramName, caseTypeName)
                    .returns(childClassName)
                val encodedArg = when (val caseModel = case.model) {
                    is Model.Enum -> {
                        val enumClassName = requireNotNull(enumClassNames[case])
                        functionBuilder.addStatement(
                            "val encoded = %L",
                            caseModel.toPathParamValueExpression(paramName, enumClassName),
                        )
                        "encoded"
                    }

                    else -> if (caseTypeName == STRING) paramName else "$paramName.toString()"
                }
                functionBuilder.addStatement(
                    "return %T(%L)",
                    childClassName,
                    currentAccumulatedParams.constructorArgs(encodedArg),
                )
                builder.addFunction(functionBuilder.build())
            }
        }

        is PathSegment.Parameter -> {
            val paramName = name.toCamelCase()
            builder.addFunction(
                FunSpec.builder(paramName)
                    .addParameter(paramName, model.toTypeName(config))
                    .returns(childClassName)
                    .addStatement(
                        "return %T(%L)",
                        childClassName,
                        currentAccumulatedParams.constructorArgs(paramName),
                    )
                    .build()
            )
        }
    }
}

private fun PathSegment.OverloadedParameter.enumClassNames(
    parentClassName: ClassName,
    config: RenderConfig,
): Map<Model.Union.Case, ClassName> {
    val enumCases = cases.filter { it.model is Model.Enum }
    if (enumCases.isEmpty()) return emptyMap()

    val baseName = name.toPascalCase()
    if (enumCases.size == 1) {
        return mapOf(enumCases.single() to parentClassName.nestedClass(baseName))
    }

    return enumCases.associateWith { case ->
        val enumModel = case.model as Model.Enum
        parentClassName.nestedClass(enumModel.context.toClassName(config).simpleName)
    }
}

internal fun Route.buildOperationBody(
    method: HttpMethod,
    config: RenderConfig,
    methodClassName: ClassName,
    inlineModelScope: OperationInlineModelScope,
    includeBody: Boolean = true,
): CodeBlock {
    val code = CodeBlock.builder()
    val httpMethodName = method.value.lowercase()
    val pathLiteral = segments.toPathLiteral()

    val nonPathParams = parameters.filter { it.input != Parameter.Input.Path }
    val hasRequestConfig = nonPathParams.isNotEmpty() || (body != null && includeBody)
    val bodyMayBeNull = includeBody && when (body?.defaultOrNull()) {
        is Route.Body.OverloadedBody -> false
        else -> body?.required != true
    }
    val responseClassName = methodClassName.nestedClass("Response")

    if (returns.needsSealedInterface()) {
        if (hasRequestConfig) {
            code.beginControlFlow("val response = client.%L(%L)", httpMethodName, pathLiteral)
            addRequestConfigCode(code, includeBody, bodyMayBeNull)
            code.endControlFlow()
        } else {
            code.addStatement("val response = client.%L(%L)", httpMethodName, pathLiteral)
        }

        code.beginControlFlow("return when (response.status.value)")
        for ((statusCode, returnTypeEntry) in returns.responses.entries.sortedBy { it.key.value }) {
            val caseName = statusCode.toCaseName()
            val caseClassName = responseClassName.nestedClass(caseName)
            val model = returnTypeEntry.preferredModel()

            if (model == null || model is Model.Primitive.Unit) {
                code.addStatement("%L -> %T", statusCode.value, caseClassName)
            } else {
                code.addStatement("%L -> %T(response.%M())", statusCode.value, caseClassName, BodyMember)
            }
        }

        if (returns.default != null) {
            val defaultClassName = responseClassName.nestedClass("Default")
            val defaultModel = returns.default!!.preferredModel()
            if (defaultModel != null && defaultModel !is Model.Primitive.Unit) {
                code.addStatement("else -> %T(response.status, response.%M())", defaultClassName, BodyMember)
            } else {
                code.addStatement("else -> %T(response.status)", defaultClassName)
            }
        } else {
            code.addStatement(
                "else -> throw %T(response, %S)",
                ResponseExceptionType,
                "",
            )
        }
        code.endControlFlow()
    } else {
        val model = returns.singlePreferredModelOrNull()

        if (model == null || model is Model.Primitive.Unit) {
            if (hasRequestConfig) {
                code.beginControlFlow("client.%L(%L)", httpMethodName, pathLiteral)
                addRequestConfigCode(code, includeBody, bodyMayBeNull)
                code.endControlFlow()
            } else {
                code.addStatement("client.%L(%L)", httpMethodName, pathLiteral)
            }
        } else {
            val usesInlineResponseType = model.isRouteInlineModel() &&
                    (model is Model.Object || model is Model.Enum || model is Model.Union)
            if (usesInlineResponseType) {
                if (hasRequestConfig) {
                    code.add("return client.%L(%L) {\n", httpMethodName, pathLiteral)
                    code.indent()
                    addRequestConfigCode(code, includeBody, bodyMayBeNull)
                    code.unindent()
                    code.add("}.%M()\n", BodyMember)
                } else {
                    code.addStatement("return client.%L(%L).%M()", httpMethodName, pathLiteral, BodyMember)
                }
            } else {
                val modelType = inlineModelScope.remap(model.toTypeName(config))
                if (hasRequestConfig) {
                    code.add("val value: %T = client.%L(%L) {\n", modelType, httpMethodName, pathLiteral)
                    code.indent()
                    addRequestConfigCode(code, includeBody, bodyMayBeNull)
                    code.unindent()
                    code.add("}.%M()\n", BodyMember)
                } else {
                    code.addStatement(
                        "val value: %T = client.%L(%L).%M()",
                        modelType,
                        httpMethodName,
                        pathLiteral,
                        BodyMember,
                    )
                }
                code.addStatement("return %T(value)", responseClassName)
            }
        }
    }

    return code.build()
}

private fun Route.addRequestConfigCode(
    code: CodeBlock.Builder,
    includeBody: Boolean = true,
    bodyMayBeNull: Boolean = includeBody && body?.required != true,
) {
    val nonPathParams = parameters.filter { it.input != Parameter.Input.Path }

    for (param in nonPathParams.filter { it.input == Parameter.Input.Query }) {
        val paramName = param.name.toParamName()
        val valueExpr = param.type.wireValueExpr(paramName)
        if (param.isRequired) {
            code.addStatement("%M(%S, %L)", ParameterMember, param.name, valueExpr)
        } else {
            val itExpr = param.type.wireItExpr()
            code.addStatement("%L?.let·{ %M(%S, %L) }", paramName, ParameterMember, param.name, itExpr)
        }
    }

    for (param in nonPathParams.filter { it.input == Parameter.Input.Header }) {
        val paramName = param.name.toParamName()
        val valueExpr = param.type.wireValueExpr(paramName)
        if (param.isRequired) {
            code.addStatement("%M(%S, %L)", HeaderMember, param.name, valueExpr)
        } else {
            val itExpr = param.type.wireItExpr()
            code.addStatement("%L?.let·{ %M(%S, %L) }", paramName, HeaderMember, param.name, itExpr)
        }
    }

    for (param in nonPathParams.filter { it.input == Parameter.Input.Cookie }) {
        val paramName = param.name.toParamName()
        val valueExpr = param.type.wireValueExpr(paramName)
        if (param.isRequired) {
            code.addStatement("%M(%S, %L)", CookieMember, param.name, valueExpr)
        } else {
            val itExpr = param.type.wireItExpr()
            code.addStatement("%L?.let·{ %M(%S, %L) }", paramName, CookieMember, param.name, itExpr)
        }
    }

    if (includeBody && body != null) {
        addBodyCode(code, body!!, bodyMayBeNull)
    }
}

/**
 * Emits a KotlinPoet [CodeBlock] for `contentType(…)` matching the given [ContentType].
 *
 * Well-known Ktor named constants are preferred so the generated code is idiomatic.
 * Unknown types fall back to the `ContentType(contentType, subtype)` constructor.
 */
private fun ContentType.toContentTypeCodeBlock(): CodeBlock {
    // Map to well-known Ktor ContentType named constants where possible.
    val namedSuffix: String? = when (this) {
        ContentType.Application.Json -> "Application.Json"
        ContentType.Application.OctetStream -> "Application.OctetStream"
        ContentType.Application.Xml -> "Application.Xml"
        ContentType.Application.FormUrlEncoded -> "Application.FormUrlEncoded"
        ContentType.Application.Pdf -> "Application.Pdf"
        ContentType.Application.Zip -> "Application.Zip"
        ContentType.Text.Plain -> "Text.Plain"
        ContentType.Text.Html -> "Text.Html"
        ContentType.Text.Xml -> "Text.Xml"
        ContentType.Text.CSS -> "Text.CSS"
        ContentType.Text.JavaScript -> "Text.JavaScript"
        ContentType.Image.PNG -> "Image.PNG"
        ContentType.Image.JPEG -> "Image.JPEG"
        ContentType.Image.GIF -> "Image.GIF"
        ContentType.Image.SVG -> "Image.SVG"
        else -> null
    }
    return if (namedSuffix != null) {
        CodeBlock.of("%T.$namedSuffix", ContentTypeType)
    } else {
        CodeBlock.of("%T(%S, %S)", ContentTypeType, contentType, contentSubtype)
    }
}

private fun addBodyCode(
    code: CodeBlock.Builder,
    bodies: Route.Bodies,
    bodyMayBeNull: Boolean,
) {
    val body = bodies.defaultOrNull() ?: return
    when (body) {
        is Route.Body.SetBody -> {
            val ctCode = body.contentType.toContentTypeCodeBlock()
            if (!bodyMayBeNull) {
                code.addStatement("%M(%L)", ContentTypeFunMember, ctCode)
                code.addStatement("%M(body)", SetBodyMember)
            } else {
                code.beginControlFlow("body?.let")
                code.addStatement("%M(%L)", ContentTypeFunMember, ctCode)
                code.addStatement("%M(it)", SetBodyMember)
                code.endControlFlow()
            }
        }

        is Route.Body.OverloadedBody -> {
            val ctCode = body.contentType.toContentTypeCodeBlock()
            if (!bodyMayBeNull) {
                code.addStatement("%M(%L)", ContentTypeFunMember, ctCode)
                code.addStatement("%M(body)", SetBodyMember)
            } else {
                code.beginControlFlow("body?.let")
                code.addStatement("%M(%L)", ContentTypeFunMember, ctCode)
                code.addStatement("%M(it)", SetBodyMember)
                code.endControlFlow()
            }
        }

        is Route.Body.FormUrlEncoded -> {
            code.add("%M(%T.build {\n", SetBodyMember, ParametersType)
            code.indent()
            for (formData in body.parameters) {
                val paramName = formData.name.toParamName()
                val valueExpr = formData.type.wireValueExpr(paramName)
                code.addStatement("append(%S, %L)", formData.name, valueExpr)
            }
            code.unindent()
            code.add("}.%M())\n", FormUrlEncodeMember)
        }

        is Route.Body.Multipart.Value -> {
            code.add("%M(%T(%M {\n", SetBodyMember, MultiPartFormDataContentType, FormDataMember)
            code.indent()
            for (formData in body.parameters) {
                val paramName = formData.name.toParamName()
                val valueExpr = formData.type.wireValueExpr(paramName)
                code.addStatement("%M(%S, %L)", AppendMember, formData.name, valueExpr)
            }
            code.unindent()
            code.add("}))\n")
        }

        is Route.Body.Multipart.Ref -> {
            if (!bodyMayBeNull) {
                code.addStatement(
                    "%M(%T.MultiPart.FormData)",
                    ContentTypeFunMember,
                    ContentTypeType,
                )
                code.addStatement("%M(body)", SetBodyMember)
            } else {
                code.beginControlFlow("body?.let")
                code.addStatement(
                    "%M(%T.MultiPart.FormData)",
                    ContentTypeFunMember,
                    ContentTypeType,
                )
                code.addStatement("%M(it)", SetBodyMember)
                code.endControlFlow()
            }
        }
    }
}

private fun List<AccumulatedParam>.constructorArgs(extraArg: String? = null): String =
    buildList {
        add("client")
        addAll(this@constructorArgs.map { it.name.toCamelCase() })
        extraArg?.let(::add)
    }.joinToString(", ")

private fun Model.Enum.toPathParamValueExpression(
    paramName: String,
    enumClassName: ClassName,
): CodeBlock {
    val code = CodeBlock.builder()
    code.add("when (%L) {\n", paramName)
    code.indent()
    for (value in values) {
        val rawValue = value ?: "null"
        code.add("%T.%L -> %S\n", enumClassName, toEnumValueName(rawValue), rawValue)
    }
    code.unindent()
    code.add("}")
    return code.build()
}

private fun List<PathSegment>.toPathLiteral(): String {
    val inner = joinToString("/") { segment ->
        when (segment) {
            is PathSegment.FixedValue -> segment.wireValue
            is PathSegment.Literal -> segment.name
            is PathSegment.Parameter -> "\$${segment.name.toCamelCase()}"
            is PathSegment.OverloadedParameter -> "\$${segment.name.toCamelCase()}"
        }
    }
    val path = if (inner.isEmpty()) "/" else "/$inner"
    return "\"$path\""
}

private fun Route.Returns.needsSealedInterface(): Boolean {
    val totalCases = responses.size + (if (default != null) 1 else 0)
    return totalCases > 1
}

private fun HttpStatusCode.toCaseName(): String =
    description.split(" ").joinToString("") { word ->
        word.lowercase().replaceFirstChar { it.uppercase() }
    }

private fun Route.ReturnType.preferredModel(): Model? {
    if (types.isEmpty()) return null
    val jsonEntry = types.entries.firstOrNull { ContentType.Application.Json.match(it.key) }
    return jsonEntry?.value ?: types.values.first()
}

private fun Route.Returns.singlePreferredModelOrNull(): Model? {
    val singleResponse = responses.values.firstOrNull() ?: default ?: return null
    return singleResponse.preferredModel()
}

private fun Model.isRouteInlineModel(): Boolean =
    this is Model.ContextHolder && context.head is NamingContext.Path

/**
 * Returns the expression that produces the wire string for a named parameter.
 * For enums with a `value` property, appends `.value`. Otherwise returns the name as-is.
 */
private fun Model.wireValueExpr(paramName: String): String =
    if (this is Model.Enum && needsValueProperty()) "$paramName.value" else paramName

/**
 * Returns the expression to use inside `?.let { ... }` lambdas when accessing the wire value.
 * For enums with a `value` property, returns `"it.value"`. Otherwise returns `"it"`.
 */
private fun Model.wireItExpr(): String =
    if (this is Model.Enum && needsValueProperty()) "it.value" else "it"
