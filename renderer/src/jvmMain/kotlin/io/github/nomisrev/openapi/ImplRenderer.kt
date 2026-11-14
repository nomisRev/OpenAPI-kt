@file:Suppress("TooManyFunctions")

package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.joinToCode
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.routes.admitsJson
import io.github.nomisrev.openapi.routes.isJsonLike
import io.github.nomisrev.openapi.routes.isWildcard
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode

private val ContentTypeType = ClassName("io.ktor.http", "ContentType")
private val HttpHeadersType = ClassName("io.ktor.http", "HttpHeaders")
private val MultiPartFormDataContentType =
    ClassName("io.ktor.client.request.forms", "MultiPartFormDataContent")
private val ParametersType = ClassName("io.ktor.http", "Parameters")
private val JsonType = ClassName("kotlinx.serialization.json", "Json")
private val ResponseExceptionType = ClassName("io.ktor.client.plugins", "ResponseException")
private val JsonPrimitiveType = ClassName("kotlinx.serialization.json", "JsonPrimitive")
private val JsonNullType = ClassName("kotlinx.serialization.json", "JsonNull")
private val JsonConvertExceptionType = ClassName("io.ktor.serialization", "JsonConvertException")

private val BodyMember = MemberName("io.ktor.client.call", "body")
private val HeaderMember = MemberName("io.ktor.client.request", "header")
private val SetBodyMember = MemberName("io.ktor.client.request", "setBody")
private val ContentTypeFunMember = MemberName("io.ktor.http", "contentType")
private val HeadersOfMember = MemberName("io.ktor.http", "headersOf")
private val FormDataMember = MemberName("io.ktor.client.request.forms", "formData")
private val FormUrlEncodeMember = MemberName("io.ktor.http", "formUrlEncode")

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

        // A composite is expanded into its steps when the tree is built, so a node is never one.
        is PathSegment.Composite -> error("The composite segment '${segment.name}' is a node per part")

        is PathSegment.FixedValue,
        is PathSegment.Literal -> parentAccumulatedParams
    }

internal fun TypeSpec.Builder.addClientConstructorAndState(
    config: RenderConfig,
    accumulatedParams: List<AccumulatedParam>,
) {
    val accumulatedTypeNames = accumulatedParams.map { param ->
        if (param.storeAsString) STRING else param.type.publicInputTypeName(config)
    }
    addExperimentalUuidOptInIfNeeded(accumulatedTypeNames)

    val constructor = FunSpec.constructorBuilder()
        .addModifiers(KModifier.INTERNAL)
        .addParameter("client", HttpClientType)
    for ([index, param] in accumulatedParams.withIndex()) {
        val typeName = accumulatedTypeNames[index]
        constructor.addParameter(param.name.toCamelCase(), typeName)
    }
    primaryConstructor(constructor.build())

    addProperty(
        PropertySpec.builder("client", HttpClientType)
            .addModifiers(KModifier.PRIVATE)
            .initializer("client")
            .build()
    )
    for ([index, param] in accumulatedParams.withIndex()) {
        val paramName = param.name.toCamelCase()
        val typeName = accumulatedTypeNames[index]
        addProperty(
            PropertySpec.builder(paramName, typeName)
                .addModifiers(KModifier.PRIVATE)
                .initializer("%N", paramName)
                .build()
        )
    }
}

/** Adds the navigation to [childClassName] as [memberName], unique among the node's children ([uniqueNames]). */
@Suppress("LongParameterList")
internal fun PathSegment.addConcreteNavigationMember(
    builder: TypeSpec.Builder,
    memberName: String,
    childClassName: ClassName,
    currentAccumulatedParams: List<AccumulatedParam>,
    parentClassName: ClassName,
    config: RenderConfig,
) {
    when (this) {
        is PathSegment.FixedValue,
        is PathSegment.Literal ->
            builder.addConcreteNavigationProperty(memberName, childClassName, currentAccumulatedParams)

        is PathSegment.OverloadedParameter ->
            builder.addOverloadedNavigationMembers(
                segment = this,
                functionName = memberName,
                childClassName = childClassName,
                currentAccumulatedParams = currentAccumulatedParams,
                parentClassName = parentClassName,
                config = config,
            )

        is PathSegment.Parameter ->
            builder.addConcreteParameterNavigationMember(
                functionName = memberName,
                parameters = listOf(name.toCamelCase() to model.publicInputTypeName(config)),
                childClassName = childClassName,
                currentAccumulatedParams = currentAccumulatedParams,
            )

        is PathSegment.Composite -> error("The composite segment '$name' is a node per part")
    }
}

private fun TypeSpec.Builder.addConcreteNavigationProperty(
    paramName: String,
    childClassName: ClassName,
    currentAccumulatedParams: List<AccumulatedParam>,
) {
    addProperty(
        PropertySpec.builder(paramName, childClassName)
            .initializer("%T(%L)", childClassName, currentAccumulatedParams.constructorArgs())
            .build()
    )
}

/** The navigation function to [childClassName], with one argument for each of the segment's [parameters]. */
private fun TypeSpec.Builder.addConcreteParameterNavigationMember(
    functionName: String,
    parameters: List<Pair<String, TypeName>>,
    childClassName: ClassName,
    currentAccumulatedParams: List<AccumulatedParam>,
) {
    val function = FunSpec.builder(functionName).returns(childClassName)
    parameters.forEach { [paramName, typeName] ->
        function.addParameter(paramName, typeName).addExperimentalUuidOptInIfNeeded(typeName)
    }
    addFunction(
        function
            .addStatement(
                "return %T(%L)",
                childClassName,
                currentAccumulatedParams.constructorArgs(parameters.map { CodeBlock.of("%N", it.first) }),
            )
            .build()
    )
}

private fun TypeSpec.Builder.addOverloadedNavigationMembers(
    segment: PathSegment.OverloadedParameter,
    functionName: String,
    childClassName: ClassName,
    currentAccumulatedParams: List<AccumulatedParam>,
    parentClassName: ClassName,
    config: RenderConfig,
) {
    val paramName = segment.name.toCamelCase()
    val enumClassNames = segment.enumClassNames(parentClassName, config)
    enumClassNames.forEach { [case, enumClassName] ->
        val enumModel = case.model as Model.Enum
        addType(enumModel.toTypeSpec(config, nameOverride = enumClassName.simpleName))
    }

    val emittedTypes = mutableSetOf<TypeName>()
    for (case in segment.cases) {
        val caseTypeName = when (case.model) {
            is Model.Enum -> requireNotNull(enumClassNames[case])
            is Model.ByteArray,
            is Model.RawResponse,
            is Model.Collection,
            is Model.Dictionary,
            is Model.Date,
            is Model.DateTime,
            is Model.DiscriminatedObject,
            is Model.FreeFormJson,
            is Model.Object,
            is Model.Primitive,
            is Model.Reference,
            is Model.AnyOf,
            is Model.OneOf,
            is Model.Uuid -> case.model.toTypeName(config)
        }
        if (!emittedTypes.add(caseTypeName)) continue

        val functionBuilder = FunSpec.builder(functionName)
            .addParameter(paramName, caseTypeName)
            .addExperimentalUuidOptInIfNeeded(caseTypeName)
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
            currentAccumulatedParams.constructorArgs(listOf(CodeBlock.of("%L", encodedArg))),
        )
        addFunction(functionBuilder.build())
    }
}

private fun PathSegment.OverloadedParameter.enumClassNames(
    parentClassName: ClassName,
    config: RenderConfig,
): Map<Model.Union.Case, ClassName> {
    val enumCases = cases.filter { it.model is Model.Enum }
    val baseName = name.toPascalCase()
    return when {
        enumCases.isEmpty() -> emptyMap()
        enumCases.size == 1 -> mapOf(enumCases.single() to parentClassName.nestedClass(baseName))
        else -> enumCases.associateWith { case ->
            val enumModel = case.model as Model.Enum
            parentClassName.nestedClass(enumModel.context.toClassName(config).simpleName)
        }
    }
}

/**
 * The request and the handling of its response. An operation that returns the response itself ([Model.RawResponse])
 * has no converter to ask for its media type, so it sends the `Accept` of the media type it declares, as the
 * operations with a method per media type ([contentType]) do.
 */
internal fun Route.buildOperationBody(
    config: RenderConfig,
    method: HttpMethod,
    methodClassName: ClassName,
    includeBody: Boolean = true,
    selectedBody: Route.Body? = body?.defaultOrNull(),
    bodyExpr: CodeBlock? = null,
    contentType: ContentType? = null,
    requestType: RequestTypeArgument? = null,
): CodeBlock {
    val accept = contentType ?: returns.rawResponseContentTypeOrNull()
    return if (returns.needsSealedInterface()) {
        if (contentType != null && returns.contentTypeStrategy() is ContentTypeStrategy.SeparateMethods) {
            buildMultiContentTypeSealedOperationBody(
                config = config,
                method = method,
                methodClassName = methodClassName,
                contentType = contentType,
                includeBody = includeBody,
                selectedBody = selectedBody,
                bodyExpr = bodyExpr,
                requestType = requestType,
            )
        } else {
            buildSealedOperationBody(
                config,
                method,
                methodClassName,
                includeBody,
                selectedBody,
                bodyExpr,
                accept,
                requestType,
            )
        }
    } else {
        buildDirectOperationBody(
            config,
            method,
            includeBody,
            selectedBody,
            bodyExpr,
            accept,
            requestType,
        )
    }
}

private data class OperationRequestInvocationContext(
    val httpMethodName: String,
    val pathLiteral: CodeBlock,
    val needsRequestConfig: Boolean,
    val bodyMayBeNull: Boolean,
)

private enum class OperationRequestInvocationStyle {
    ResponseBinding,
    StatementOnly,
}

private fun Route.operationRequestInvocationContext(
    config: RenderConfig,
    method: HttpMethod,
    includeBody: Boolean,
    selectedBody: Route.Body?,
    contentType: ContentType?,
): OperationRequestInvocationContext =
    OperationRequestInvocationContext(
        httpMethodName = method.value.lowercase(),
        pathLiteral = pathLiteral(config),
        needsRequestConfig = hasRequestConfig(includeBody) || contentType != null,
        bodyMayBeNull = includeBody && when (selectedBody) {
            is Route.Body.OverloadedBody -> false
            is Route.Body.FormUrlEncoded,
            is Route.Body.Multipart.Ref,
            is Route.Body.Multipart.Value,
            is Route.Body.SetBody,
            is Route.Body.CallerSet,
            null -> body?.required != true
        },
    )

@Suppress("LongParameterList")
private fun CodeBlock.Builder.addRequestInvocation(
    route: Route,
    request: OperationRequestInvocationContext,
    style: OperationRequestInvocationStyle,
    config: RenderConfig,
    includeBody: Boolean,
    selectedBody: Route.Body?,
    bodyExpr: CodeBlock?,
    acceptContentType: ContentType?,
    requestType: RequestTypeArgument?,
) {
    if (!request.needsRequestConfig) {
        when (style) {
            OperationRequestInvocationStyle.ResponseBinding ->
                addStatement("val response = client.%L(%L)", request.httpMethodName, request.pathLiteral)

            OperationRequestInvocationStyle.StatementOnly ->
                addStatement("client.%L(%L)", request.httpMethodName, request.pathLiteral)
        }
        return
    }

    when (style) {
        OperationRequestInvocationStyle.ResponseBinding ->
            beginControlFlow("val response = client.%L(%L)", request.httpMethodName, request.pathLiteral)

        OperationRequestInvocationStyle.StatementOnly ->
            beginControlFlow("client.%L(%L)", request.httpMethodName, request.pathLiteral)
    }

    route.addRequestConfigCode(
        config = config,
        code = this,
        includeBody = includeBody,
        selectedBody = selectedBody,
        bodyMayBeNull = request.bodyMayBeNull,
        bodyExpr = bodyExpr,
        acceptContentType = acceptContentType,
        requestType = requestType,
    )

    endControlFlow()
}

private fun Route.buildSealedOperationBody(
    config: RenderConfig,
    method: HttpMethod,
    methodClassName: ClassName,
    includeBody: Boolean,
    selectedBody: Route.Body?,
    bodyExpr: CodeBlock?,
    contentType: ContentType?,
    requestType: RequestTypeArgument?,
): CodeBlock {
    val code = CodeBlock.builder()
    val request = operationRequestInvocationContext(config, method, includeBody, selectedBody, contentType)

    code.addRequestInvocation(
        route = this,
        request = request,
        style = OperationRequestInvocationStyle.ResponseBinding,
        config = config,
        includeBody = includeBody,
        selectedBody = selectedBody,
        bodyExpr = bodyExpr,
        acceptContentType = contentType,
        requestType = requestType,
    )

    code.beginControlFlow("return when (response.status.value)")
    addSealedResponseStatusCases(code, methodClassName)
    addSealedResponseRangeCases(code, methodClassName)
    addSealedResponseDefaultCase(code, methodClassName)
    code.endControlFlow()
    return code.build()
}

@Suppress("LongMethod")
private fun Route.buildMultiContentTypeSealedOperationBody(
    config: RenderConfig,
    method: HttpMethod,
    methodClassName: ClassName,
    contentType: ContentType,
    includeBody: Boolean,
    selectedBody: Route.Body?,
    bodyExpr: CodeBlock?,
    requestType: RequestTypeArgument?,
): CodeBlock {
    val code = CodeBlock.builder()
    val request = operationRequestInvocationContext(config, method, includeBody, selectedBody, contentType)

    code.addRequestInvocation(
        route = this,
        request = request,
        style = OperationRequestInvocationStyle.ResponseBinding,
        config = config,
        includeBody = includeBody,
        selectedBody = selectedBody,
        bodyExpr = bodyExpr,
        acceptContentType = contentType,
        requestType = requestType,
    )

    code.beginControlFlow("return when (response.status.value)")
    addMultiContentTypeSealedResponseStatusCases(code, methodClassName, contentType)
    addMultiContentTypeSealedResponseRangeCases(code, methodClassName, contentType)
    addMultiContentTypeSealedResponseDefaultCase(code, methodClassName, contentType)
    code.endControlFlow()
    return code.build()
}

/**
 * Body for an operation with at most one declared response, which returns its payload directly. Like the
 * sealed path's `else -> throw ResponseException(...)`, a status other than the single declared one throws
 * before the body is decoded, independent of the client's `expectSuccess`. A lone `default` matches every
 * status, so it needs no check.
 */
private fun Route.buildDirectOperationBody(
    config: RenderConfig,
    method: HttpMethod,
    includeBody: Boolean,
    selectedBody: Route.Body?,
    bodyExpr: CodeBlock?,
    contentType: ContentType?,
    requestType: RequestTypeArgument?,
): CodeBlock {
    val code = CodeBlock.builder()
    val model = returns.singlePreferredModelOrNull()
    val returnsBody = model != null && model !is Model.Primitive.Unit
    val declaredStatus = returns.responses.keys.singleOrNull()
    val request = operationRequestInvocationContext(config, method, includeBody, selectedBody, contentType)

    code.addRequestInvocation(
        route = this,
        request = request,
        style = if (returnsBody || declaredStatus != null) {
            OperationRequestInvocationStyle.ResponseBinding
        } else {
            OperationRequestInvocationStyle.StatementOnly
        },
        config = config,
        includeBody = includeBody,
        selectedBody = selectedBody,
        bodyExpr = bodyExpr,
        acceptContentType = contentType,
        requestType = requestType,
    )

    if (declaredStatus != null) {
        code.addStatement(
            "if (response.status.value != %L) throw %T(response, %S)",
            declaredStatus.value,
            ResponseExceptionType,
            "",
        )
    }

    if (returnsBody) {
        code.addStatement(
            "return %L",
            responseBody(
                model = returns.singlePreferredModelOrNull(contentType),
                contentType = contentType ?: returns.singlePreferredContentTypeOrNull(),
            ),
        )
    }

    return code.build()
}

private fun Route.addSealedResponseStatusCases(code: CodeBlock.Builder, methodClassName: ClassName) {
    val responseClassName = methodClassName.nestedClass("Response")
    for ([statusCode, returnTypeEntry] in returns.responses.entries.sortedBy { it.key.value }) {
        val caseName = statusCode.toCaseName()
        val caseClassName = responseClassName.nestedClass(caseName)
        val model = returnTypeEntry.preferredModel()
        code.addStatement(
            "%L -> %L",
            statusCode.value,
            buildResponseCaseExpression(caseClassName, model, returnTypeEntry.preferredContentType()),
        )
    }
}

private fun Route.addSealedResponseRangeCases(code: CodeBlock.Builder, methodClassName: ClassName) {
    val responseClassName = methodClassName.nestedClass("Response")
    for ([range, returnType] in returns.responseRanges) {
        val status = ResponseStatus.Range(range, returnType)
        val caseClassName = responseClassName.nestedClass(status.caseName)
        code.addStatement(
            "in %L -> %L",
            requireNotNull(status.matchingRange),
            buildRangeResponseCaseExpression(
                caseClassName,
                status.caseName,
                status,
                returnType.preferredModel(),
                returnType.preferredContentType(),
            ),
        )
    }
}

private fun Route.addSealedResponseDefaultCase(code: CodeBlock.Builder, methodClassName: ClassName) {
    val responseClassName = methodClassName.nestedClass("Response")
    if (returns.default != null) {
        val defaultClassName = responseClassName.nestedClass("Default")
        val defaultModel = returns.default!!.preferredModel()
        if (defaultModel != null && defaultModel !is Model.Primitive.Unit) {
            code.addStatement(
                "else -> %T(response.status, %L)",
                defaultClassName,
                responseBody(defaultModel, returns.default!!.preferredContentType()),
            )
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
}

private fun Route.addMultiContentTypeSealedResponseStatusCases(
    code: CodeBlock.Builder,
    methodClassName: ClassName,
    contentType: ContentType,
) {
    val responseClassName = methodClassName.nestedClass(contentTypeResponseTypeName(contentType))
    for ([statusCode, returnTypeEntry] in returns.responses.entries.sortedBy { it.key.value }) {
        if (statusCode.isSuccessStatusCode()) {
            val model = returnTypeEntry.modelForMediaType(contentType)
            if (model == null) {
                if (returnTypeEntry.types.isEmpty()) {
                    code.addStatement(
                        "%L -> %T",
                        statusCode.value,
                        methodClassName.nestedClass(statusCode.toCaseName()),
                    )
                } else if (returns.responseRanges.isNotEmpty()) {
                    code.addStatement(
                        "%L -> throw %T(response, %S)",
                        statusCode.value,
                        ResponseExceptionType,
                        "",
                    )
                }
                continue
            }
            val caseClassName = responseClassName.nestedClass(statusCode.toCaseName())
            if (model is Model.Primitive.Unit) {
                code.addStatement("%L -> %T", statusCode.value, caseClassName)
            } else if (model.isRouteInlineModel() && model !is Model.Collection) {
                code.addStatement("%L -> response.%M<%T>()", statusCode.value, BodyMember, caseClassName)
            } else {
                code.addStatement(
                    "%L -> %T(%L)",
                    statusCode.value,
                    caseClassName,
                    responseBody(model, contentType),
                )
            }
            continue
        }

        when (val errorStrategy = classifyErrorStatus(statusCode, returnTypeEntry)) {
            ErrorCaseStrategy.NoContent -> code.addStatement(
                "%L -> %T",
                statusCode.value,
                methodClassName.nestedClass(statusCode.toCaseName()),
            )

            is ErrorCaseStrategy.SingleContentType -> {
                val caseClassName = methodClassName.nestedClass(statusCode.toCaseName())
                code.addStatement(
                    "%L -> %L",
                    statusCode.value,
                    buildResponseCaseExpression(caseClassName, errorStrategy.model, errorStrategy.contentType),
                )
            }

            is ErrorCaseStrategy.MultipleContentTypes -> {
                code.add(buildMultiCTErrorDispatch(methodClassName, statusCode, errorStrategy.variants))
            }
        }
    }
}

private fun Route.addMultiContentTypeSealedResponseRangeCases(
    code: CodeBlock.Builder,
    methodClassName: ClassName,
    contentType: ContentType,
) {
    val responseClassName = methodClassName.nestedClass(contentTypeResponseTypeName(contentType))
    for ([range, returnType] in returns.responseRanges) {
        val status = ResponseStatus.Range(range, returnType)
        val matchingRange = requireNotNull(status.matchingRange)
        if (status.isSuccessStatusCode) {
            if (returnType.types.isEmpty()) {
                val caseClassName = methodClassName.nestedClass(status.caseName)
                code.addStatement(
                    "in %L -> %L",
                    matchingRange,
                    buildRangeResponseCaseExpression(caseClassName, status.caseName, status, null, null),
                )
            } else {
                val caseClassName = responseClassName.nestedClass(status.caseName)
                code.addStatement(
                    "in %L -> %L",
                    matchingRange,
                    buildRangeResponseCaseExpression(
                        caseClassName,
                        status.caseName,
                        status,
                        returnType.modelForContentType(contentType),
                        contentType,
                    ),
                )
            }
            continue
        }

        val variants = returnType.types.entries.toList()
        when {
            variants.isEmpty() -> {
                val caseClassName = methodClassName.nestedClass(status.caseName)
                code.addStatement(
                    "in %L -> %L",
                    matchingRange,
                    buildRangeResponseCaseExpression(caseClassName, status.caseName, status, null, null),
                )
            }

            variants.size == 1 -> {
                val caseClassName = methodClassName.nestedClass(status.caseName)
                code.addStatement(
                    "in %L -> %L",
                    matchingRange,
                    buildRangeResponseCaseExpression(
                        caseClassName,
                        status.caseName,
                        status,
                        variants.single().value,
                        variants.single().key,
                    ),
                )
            }

            else -> code.add(
                buildMultiCTRangeErrorDispatch(
                    methodClassName = methodClassName,
                    status = status,
                    variants = variants,
                )
            )
        }
    }
}

private fun buildMultiCTRangeErrorDispatch(
    methodClassName: ClassName,
    status: ResponseStatus.Range,
    variants: List<Map.Entry<ContentType, Model>>,
): CodeBlock {
    val preferredVariant = variants.firstOrNull { it.key.match(ContentType.Application.Json) } ?: variants.first()
    val code = CodeBlock.builder()
    code.add("in %L -> run {\n", requireNotNull(status.matchingRange))
    code.indent()
    code.addStatement("val ct = response.%M()", ContentTypeFunMember)
    code.beginControlFlow("when")
    for ([errorContentType, errorModel] in variants) {
        val caseName = "${contentTypeToIdentifier(errorContentType)}${status.caseName}"
        val caseClassName = methodClassName.nestedClass(caseName)
        code.addStatement(
            "ct?.match(%L) == true -> %L",
            errorContentType.toResponseMatchCodeBlock(),
            buildRangeResponseCaseExpression(caseClassName, caseName, status, errorModel, errorContentType),
        )
    }
    val preferredCaseName = "${contentTypeToIdentifier(preferredVariant.key)}${status.caseName}"
    val preferredCaseClassName = methodClassName.nestedClass(preferredCaseName)
    code.addStatement(
        "else -> %L",
        buildRangeResponseCaseExpression(
            preferredCaseClassName,
            preferredCaseName,
            status,
            preferredVariant.value,
            preferredVariant.key,
        ),
    )
    code.endControlFlow()
    code.unindent()
    code.add("}\n")
    return code.build()
}

private fun buildMultiCTErrorDispatch(
    methodClassName: ClassName,
    statusCode: HttpStatusCode,
    variants: List<Pair<ContentType, Model>>,
): CodeBlock {
    val preferredVariant = variants.firstOrNull { it.first.match(ContentType.Application.Json) } ?: variants.first()
    val code = CodeBlock.builder()
    code.add("%L -> run {\n", statusCode.value)
    code.indent()
    code.addStatement("val ct = response.%M()", ContentTypeFunMember)
    code.beginControlFlow("when")
    for ([errorContentType, errorModel] in variants) {
        val caseName = "${contentTypeToIdentifier(errorContentType)}${statusCode.toCaseName()}"
        val caseClassName = methodClassName.nestedClass(caseName)
        code.addStatement(
            "ct?.match(%L) == true -> %L",
            errorContentType.toResponseMatchCodeBlock(),
            buildResponseCaseExpression(caseClassName, errorModel, errorContentType),
        )
    }
    val preferredCaseName = "${contentTypeToIdentifier(preferredVariant.first)}${statusCode.toCaseName()}"
    val preferredCaseClassName = methodClassName.nestedClass(preferredCaseName)
    code.addStatement(
        "else -> %L",
        buildResponseCaseExpression(preferredCaseClassName, preferredVariant.second, preferredVariant.first),
    )
    code.endControlFlow()
    code.unindent()
    code.add("}\n")
    return code.build()
}

private fun Route.addMultiContentTypeSealedResponseDefaultCase(
    code: CodeBlock.Builder,
    methodClassName: ClassName,
    contentType: ContentType,
) {
    if (returns.default != null) {
        val defaultClassName = methodClassName.nestedClass("Default")
        val defaultModel = returns.default!!.modelForMediaType(contentType) ?: returns.default!!.preferredModel()
        val defaultContentType =
            if (returns.default!!.modelForMediaType(contentType) != null) contentType
            else returns.default!!.preferredContentType()
        if (defaultModel != null && defaultModel !is Model.Primitive.Unit) {
            code.addStatement(
                "else -> %T(response.status, %L)",
                defaultClassName,
                responseBody(defaultModel, defaultContentType),
            )
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
}

private fun buildResponseCaseExpression(
    caseClassName: ClassName,
    model: Model?,
    contentType: ContentType?,
): CodeBlock =
    when {
        model == null || model is Model.Primitive.Unit -> CodeBlock.of("%T", caseClassName)
        model.isRouteInlineModel() && model !is Model.Collection ->
            CodeBlock.of("response.%M<%T>()", BodyMember, caseClassName)

        else -> CodeBlock.of("%T(%L)", caseClassName, responseBody(model, contentType))
    }

@Suppress("LongParameterList")
private fun buildRangeResponseCaseExpression(
    caseClassName: ClassName,
    caseName: String,
    status: ResponseStatus.Range,
    model: Model?,
    contentType: ContentType?,
): CodeBlock {
    if (model == null || model is Model.Primitive.Unit) {
        return CodeBlock.of("%T(response.status)", caseClassName)
    }

    val body = if (model.isRouteInlineModel() && model !is Model.Collection) {
        CodeBlock.of("response.%M<%T>()", BodyMember, caseClassName.nestedClass("${caseName}Body"))
    } else {
        responseBody(model, contentType)
    }
    val range = requireNotNull(status.matchingRange)
    val noContentChecks = listOf(204, 304).filter(range::contains)
    if (noContentChecks.isEmpty()) {
        return CodeBlock.of("%T(response.status, %L)", caseClassName, body)
    }

    val noContentCondition = noContentChecks.joinToString(" || ") { "response.status.value == $it" }
    return CodeBlock.of(
        "if (%L) %T(response.status, null) else %T(response.status, %L)",
        noContentCondition,
        caseClassName,
        caseClassName,
        body,
    )
}

private fun Route.hasRequestConfig(includeBody: Boolean): Boolean {
    val nonPathParams = parameters.filter { it.input != Parameter.Input.Path }
    return nonPathParams.isNotEmpty() || (body != null && includeBody)
}

@Suppress("CanBeNonNullable")
private fun Route.addRequestConfigCode(
    config: RenderConfig,
    code: CodeBlock.Builder,
    includeBody: Boolean = true,
    selectedBody: Route.Body? = body?.defaultOrNull(),
    bodyMayBeNull: Boolean = includeBody && body?.required != true,
    bodyExpr: CodeBlock? = null,
    acceptContentType: ContentType? = null,
    requestType: RequestTypeArgument? = null,
) {
    val nonPathParams = parameters.filter { it.input != Parameter.Input.Path }

    if (acceptContentType != null) {
        code.addStatement(
            "%M(%T.Accept, %L)",
            HeaderMember,
            HttpHeadersType,
            acceptContentType.toContentTypeCodeBlock()
        )
    }

    for (param in nonPathParams.filter { it.input == Parameter.Input.Query }) {
        code.addParameterPairsCode(config, PairTarget.Query, param)
    }

    for (param in nonPathParams.filter { it.input == Parameter.Input.Header && it.isSupported }) {
        code.addHeaderCode(
            config = config,
            headerMember = HeaderMember,
            name = param.name,
            expr = param.name.toParamName(),
            model = param.type.publicInputModelOrSelf(),
            mayBeNull = param.mayBeNull,
            explode = param.explode,
        )
    }

    for (param in nonPathParams.filter { it.input == Parameter.Input.Cookie }) {
        code.addParameterPairsCode(config, PairTarget.Cookie, param)
    }

    if (includeBody && selectedBody != null) {
        addBodyCode(
            config = config,
            code = code,
            body = selectedBody,
            bodyMayBeNull = bodyMayBeNull,
            bodyExpr = bodyExpr,
            requestType = requestType,
            callerContentType = callerContentTypeArgumentOrNull(config, selectedBody),
            sentContentType = body?.variants()?.firstOrNull { it.body === selectedBody }?.sentContentType,
            callerBlock = if (selectedBody is Route.Body.CallerSet) callerBlockParameterName() else null,
        )
    }
}

/**
 * Appends the query parameter or cookie [param] by its `style` and `explode` ([addPairsCode]). A parameter with
 * `content` is a scalar sent as is, and a parameter without a supported serialization is reported by
 * `GenerationDiagnostics` instead of sent.
 */
private fun CodeBlock.Builder.addParameterPairsCode(config: RenderConfig, target: PairTarget, param: Route.Input) {
    if (!param.isSupported) return
    addPairsCode(
        config = config,
        target = target,
        name = param.name,
        expr = param.name.toParamName(),
        model = param.type.publicInputModelOrSelf(),
        mayBeNull = param.mayBeNull,
        style = param.formStyle ?: Route.Body.FormStyle.FormExploded,
    )
}

/** Whether the argument of this parameter can be `null`: it is optional, or its schema admits `null`. */
private val Route.Input.mayBeNull: Boolean
    get() = !isRequired || type.isNullable || type.publicInputModelOrSelf().isNullable

/**
 * Emits a KotlinPoet [CodeBlock] for the given [ContentType], parameters included: a declared
 * `application/json; charset=utf-8` is `ContentType.Application.Json.withParameter("charset", "utf-8")`.
 *
 * Well-known Ktor named constants are preferred so the generated code is idiomatic.
 * Unknown types fall back to the `ContentType(contentType, subtype)` constructor.
 */
internal fun ContentType.toContentTypeCodeBlock(): CodeBlock {
    val code = CodeBlock.builder().add(withoutParameters().toMediaTypeCodeBlock())
    for (parameter in parameters) {
        code.add(".withParameter(%S, %S)", parameter.name, parameter.value)
    }
    return code.build()
}

/**
 * Emits the `ct?.match(…)` pattern a response declared as this [ContentType] is recognised by: its media type without
 * parameters. Ktor's `match` requires every parameter of the pattern on the response's `Content-Type`, and a server
 * that declares `application/json; charset=utf-8` may still send a bare `application/json` (or another charset, which
 * Ktor decodes); the variants a dispatch tells apart already differ in their media type.
 */
private fun ContentType.toResponseMatchCodeBlock(): CodeBlock = withoutParameters().toContentTypeCodeBlock()

@Suppress("CyclomaticComplexMethod")
private fun ContentType.toMediaTypeCodeBlock(): CodeBlock {
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
    config: RenderConfig,
    code: CodeBlock.Builder,
    body: Route.Body,
    bodyMayBeNull: Boolean,
    bodyExpr: CodeBlock? = null,
    requestType: RequestTypeArgument? = null,
    callerContentType: CallerContentTypeArgument? = null,
    sentContentType: ContentType? = null,
    callerBlock: String? = null,
) {
    when (body) {
        is Route.Body.CallerSet -> code.addCallerSetBodyCode(
            body,
            callerContentType,
            requireNotNull(callerBlock) { "A caller-set body is built in a block." },
        )

        is Route.Body.SetBody -> {
            val contentType = requestType?.contentTypeCode
                ?: callerContentType?.let { CodeBlock.of("%N", it.name) }
                ?: body.contentType.toContentTypeCodeBlock()
            val check = callerContentType?.requireCode()
            code.addSetLikeBodyCode(contentType, bodyExpr, bodyMayBeNull, check) { value ->
                if (requestType == null) {
                    addStatement("%M(%L)", SetBodyMember, setBodyArgument(body.type, body.contentType, value))
                } else {
                    addRequestTypeSetBody(requestType, value)
                }
            }
        }

        is Route.Body.OverloadedBody -> {
            val contentType = requestType?.contentTypeCode ?: body.contentType.toContentTypeCodeBlock()
            code.addSetLikeBodyCode(contentType, bodyExpr, bodyMayBeNull, contentTypeCheck = null) { value ->
                addStatement("%M(%L)", SetBodyMember, value)
            }
        }

        is Route.Body.FormUrlEncoded -> code.addFormUrlEncodedBodyCode(
            config,
            body,
            requireNotNull(sentContentType) { "A form body is sent as the media type it is declared under." },
        )
        is Route.Body.Multipart.Value -> code.addMultipartValueBodyCode(config, body)
        is Route.Body.Multipart.Ref -> code.addMultipartRefBodyCode(bodyMayBeNull)
    }
}

/**
 * A [Route.Body.CallerSet] body: the declared media type, or the one the caller passes in [callerContentType], then
 * the caller's [block], which sets the body and may still change anything set before it, the media type included.
 * An optional media type argument that is absent leaves it to the block.
 */
private fun CodeBlock.Builder.addCallerSetBodyCode(
    body: Route.Body.CallerSet,
    callerContentType: CallerContentTypeArgument?,
    block: String,
) {
    when {
        callerContentType == null ->
            addStatement("%M(%L)", ContentTypeFunMember, body.contentType.toContentTypeCodeBlock())

        callerContentType.nullable -> {
            beginControlFlow("if (%N != null)", callerContentType.name)
            add(callerContentType.copy(nullable = false).requireCode())
            addStatement("%M(%N)", ContentTypeFunMember, callerContentType.name)
            endControlFlow()
        }

        else -> {
            add(callerContentType.requireCode())
            addStatement("%M(%N)", ContentTypeFunMember, callerContentType.name)
        }
    }
    addStatement("%N()", block)
}

/**
 * `setBody(…)` for a body whose content type is chosen at runtime through `requestType`: the clashing variants share
 * one Kotlin signature, but a `String` must still become a JSON string for the JSON media types among them.
 */
private fun CodeBlock.Builder.addRequestTypeSetBody(requestType: RequestTypeArgument, value: String) {
    val arguments = requestType.variants.map { variant ->
        val model = (variant.body as? Route.Body.SetBody)?.type
        setBodyArgument(model, variant.sentContentType, value)
    }
    if (arguments.distinct().size == 1) {
        addStatement("%M(%L)", SetBodyMember, arguments.first())
        return
    }
    beginControlFlow("when (requestType)")
    for ([name, argument] in requestType.constantNames.zip(arguments)) {
        addStatement("%T.%N -> %M(%L)", requestType.enumClassName, name, SetBodyMember, argument)
    }
    endControlFlow()
}

@Suppress("CanBeNonNullable")
private fun CodeBlock.Builder.addSetLikeBodyCode(
    contentType: CodeBlock,
    bodyExpr: CodeBlock?,
    bodyMayBeNull: Boolean,
    contentTypeCheck: CodeBlock?,
    setBody: CodeBlock.Builder.(value: String) -> Unit,
) {
    fun setContentType() {
        contentTypeCheck?.let(::add)
        addStatement("%M(%L)", ContentTypeFunMember, contentType)
    }
    if (bodyExpr != null) {
        setContentType()
        addStatement("%M(%L)", SetBodyMember, bodyExpr)
    } else if (!bodyMayBeNull) {
        setContentType()
        setBody("body")
    } else {
        beginControlFlow("body?.let")
        setContentType()
        setBody("it")
        endControlFlow()
    }
}

/**
 * A form body is the `String` of `formUrlEncode`, which Ktor sends as `TextContent` labelled with the request's
 * `Content-Type`, and `text/plain` when none is set; so the media type the form is declared under is set, parameters
 * included, as for any other body. It is the form's own media type, never `requestType.contentType`: a form shares
 * its signature with a `multipart/form-data` body of the same fields, which Ktor labels itself.
 *
 * `formUrlEncode` serializes UTF-8 `application/x-www-form-urlencoded`: a space is `+` and every byte outside
 * `A-Z a-z 0-9 - . _ ~` is percent-encoded, so values with `&`, `=`, `+` or non-ASCII characters decode back to
 * themselves. (The URL Standard leaves `*` and encodes `~`; every decoder reads both forms alike.) A form declared in
 * another charset is not generated (`unsupportedFormCharsetOrNull`).
 *
 * Every field is serialized like a query parameter of its Encoding Object `style` and `explode` ([addPairsCode]).
 * Encoding every reserved character is also right for `allowReserved: true`, which only permits leaving them as is:
 * a form decoder reads `%2F` and `/` alike, and must not see a value's own `&` or `=` unencoded.
 */
private fun CodeBlock.Builder.addFormUrlEncodedBodyCode(
    config: RenderConfig,
    body: Route.Body.FormUrlEncoded,
    contentType: ContentType,
) {
    addStatement("%M(%L)", ContentTypeFunMember, contentType.toContentTypeCodeBlock())
    add("%M(%T.build {\n", SetBodyMember, ParametersType)
    indent()
    for (formData in body.parameters) {
        addPairsCode(
            config = config,
            target = PairTarget.FormBody,
            name = formData.name,
            expr = formData.name.toParamName(),
            model = formData.type,
            mayBeNull = formData.type.isNullable || !formData.isRequired,
            style = body.styleOf(formData),
        )
    }
    unindent()
    add("}.%M())\n", FormUrlEncodeMember)
}

private fun CodeBlock.Builder.addMultipartValueBodyCode(config: RenderConfig, body: Route.Body.Multipart.Value) {
    add("%M(%T(%M {\n", SetBodyMember, MultiPartFormDataContentType, FormDataMember)
    indent()
    for (formData in body.parameters) {
        val paramName = formData.name.toParamName()
        val valueExpr = formData.multipartValueExpr(config, paramName)
        val headersExpr = formData.contentType?.let { contentType ->
            CodeBlock.of("%M(%T.ContentType, %S)", HeadersOfMember, HttpHeadersType, contentType.toString())
        }
        if (!formData.isRequired) {
            beginControlFlow("if (%L != null)", paramName)
            appendStatement(headersExpr, formData, valueExpr)
            endControlFlow()
        } else {
            appendStatement(headersExpr, formData, valueExpr)
        }
    }
    unindent()
    add("}))\n")
}

private fun CodeBlock.Builder.appendStatement(
    headersExpr: CodeBlock?,
    formData: Route.Body.Multipart.FormData,
    valueExpr: CodeBlock
) {
    if (headersExpr != null) {
        addStatement("append(%S, %L, %L)", formData.name, valueExpr, headersExpr)
    } else {
        addStatement("append(%S, %L)", formData.name, valueExpr)
    }
}

private fun CodeBlock.Builder.addMultipartRefBodyCode(bodyMayBeNull: Boolean) {
    if (!bodyMayBeNull) {
        addStatement(
            "%M(%T.MultiPart.FormData)",
            ContentTypeFunMember,
            ContentTypeType,
        )
        addStatement("%M(body)", SetBodyMember)
    } else {
        beginControlFlow("body?.let")
        addStatement(
            "%M(%T.MultiPart.FormData)",
            ContentTypeFunMember,
            ContentTypeType,
        )
        addStatement("%M(it)", SetBodyMember)
        endControlFlow()
    }
}

/**
 * The constructor arguments of a child class: the client, each accumulated parameter (`%N`, so a name that is not an
 * identifier is escaped), then [extraArgs].
 */
private fun List<AccumulatedParam>.constructorArgs(extraArgs: List<CodeBlock> = emptyList()): CodeBlock =
    (listOf(CodeBlock.of("client")) + map { CodeBlock.of("%N", it.name.toCamelCase()) } + extraArgs).joinToCode(", ")

private fun Route.Body.Multipart.FormData.multipartValueExpr(config: RenderConfig, paramName: String): CodeBlock =
    when {
        contentType?.isJsonLike() == true ->
            CodeBlock.of("%T.encodeToString(%L, %L)", JsonType, type.serializerCode(config), paramName)

        type.isJsonLikeMultipartModel() ->
            CodeBlock.of("%T.encodeToString(%L)", JsonType, paramName)

        else -> CodeBlock.of("%L", type.wireValueExpr(paramName))
    }

/**
 * Ktor's `ContentNegotiation` never converts `String`: it is one of its default ignored types, so `body<String>()`
 * returns the raw payload and `setBody(String)` sends it verbatim. A `type: string` body under a JSON media type is
 * a JSON string literal on the wire, so it is exchanged as a [JsonPrimitiveType] instead, which the client's own
 * JSON converter encodes and decodes.
 */
private fun Model?.isJsonStringBody(contentType: ContentType?): Boolean =
    this is Model.Primitive.String && contentType?.isJsonLike() == true

/** `setBody(…)` argument for a request body of [model] sent as [contentType]. */
internal fun setBodyArgument(model: Model?, contentType: ContentType?, value: String): CodeBlock =
    if (model.isJsonStringBody(contentType)) CodeBlock.of("%T(%L)", JsonPrimitiveType, value)
    else CodeBlock.of("%L", value)

/** The `body()` call, without receiver, decoding a response of [model] received as [contentType]. */
private fun bodyCall(model: Model?, contentType: ContentType?): CodeBlock {
    if (model == null || !model.isJsonStringBody(contentType)) return CodeBlock.of("%M()", BodyMember)
    val nullCase = if (model.isNullable) CodeBlock.of("if (it is %T) null else ", JsonNullType) else CodeBlock.of("")
    return CodeBlock.of(
        "%M<%T>().let { %Lif (it.isString) it.content else throw %T(\"Illegal input: expected a JSON string but was \$it\") }",
        BodyMember,
        JsonPrimitiveType,
        nullCase,
        JsonConvertExceptionType,
    )
}

/**
 * Decodes a response of [model] declared as [contentType]. A string declared under a media type range that admits
 * JSON (any media type, any `application` subtype) is a JSON string when the server sends a JSON `Content-Type` and
 * raw text otherwise, so the generated code decides on the `Content-Type` of the response, with the same JSON check as
 * [isJsonLike]. Other models go through `ContentNegotiation`, which already decides on that `Content-Type`.
 */
private fun responseBody(model: Model?, contentType: ContentType?): CodeBlock {
    // The response itself, as the call produced it: the body is not read, so the caller chooses how to.
    if (model is Model.RawResponse) return CodeBlock.of("response")
    val isRangeAdmittingJson = contentType != null && contentType.isWildcard() && contentType.admitsJson()
    if (model !is Model.Primitive.String || !isRangeAdmittingJson) {
        return CodeBlock.of("response.%L", bodyCall(model, contentType))
    }
    return CodeBlock.of(
        "if (response.%M()?.let { it.match(%T.Application.Json) || it.contentSubtype.endsWith(%S, ignoreCase = true) } == true) response.%L else response.%L",
        ContentTypeFunMember,
        ContentTypeType,
        "+json",
        bodyCall(model, ContentType.Application.Json),
        bodyCall(model, ContentType.Text.Plain),
    )
}

private fun Model.isJsonLikeMultipartModel(): Boolean = when (this) {
    is Model.ByteArray,
    is Model.RawResponse,
    is Model.Date,
    is Model.DateTime,
    is Model.Enum,
    is Model.Primitive,
    is Model.Uuid -> false

    is Model.Collection,
    is Model.Dictionary,
    is Model.DiscriminatedObject,
    is Model.FreeFormJson,
    is Model.Object,
    is Model.Reference,
    is Model.Union -> true
}

private fun Model.Enum.toPathParamValueExpression(
    paramName: String,
    enumClassName: ClassName,
): CodeBlock {
    val code = CodeBlock.builder()
    code.add("when (%L) {\n", paramName)
    code.indent()
    for (value in values) {
        val rawValue = value.wireValue()
        code.add("%T.%L -> %S\n", enumClassName, toEnumValueName(rawValue), rawValue)
    }
    code.unindent()
    code.add("}")
    return code.build()
}

/**
 * Returns the expression that produces the wire string for a named parameter.
 * For enums with a `value` property, appends `.value`. Otherwise falls back to `toString()`.
 */
private fun Model.wireValueExpr(paramName: String): String =
    when {
        this is Model.Enum -> wireTextExpr(paramName)
        this is Model.Date || this is Model.DateTime || this is Model.Uuid -> "$paramName.toString()"
        else -> paramName
    }
