@file:Suppress("TooManyFunctions")

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
private val ParameterMember = MemberName("io.ktor.client.request", "parameter")
private val HeaderMember = MemberName("io.ktor.client.request", "header")
private val CookieMember = MemberName("io.ktor.client.request", "cookie")
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
        is PathSegment.FixedValue, is PathSegment.Literal ->
            builder.addConcreteNavigationProperty(name.toParamName(), childClassName, currentAccumulatedParams)

        is PathSegment.OverloadedParameter ->
            builder.addOverloadedNavigationMembers(
                segment = this,
                childClassName = childClassName,
                currentAccumulatedParams = currentAccumulatedParams,
                parentClassName = parentClassName,
                config = config,
            )

        is PathSegment.Parameter ->
            builder.addConcreteParameterNavigationMember(
                paramName = name.toCamelCase(),
                typeName = model.publicInputTypeName(config),
                childClassName = childClassName,
                currentAccumulatedParams = currentAccumulatedParams,
            )
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

private fun TypeSpec.Builder.addConcreteParameterNavigationMember(
    paramName: String,
    typeName: TypeName,
    childClassName: ClassName,
    currentAccumulatedParams: List<AccumulatedParam>,
) {
    addFunction(
        FunSpec.builder(paramName)
            .addParameter(paramName, typeName)
            .addExperimentalUuidOptInIfNeeded(typeName)
            .returns(childClassName)
            .addStatement(
                "return %T(%L)",
                childClassName,
                currentAccumulatedParams.constructorArgs(paramName),
            )
            .build()
    )
}

private fun TypeSpec.Builder.addOverloadedNavigationMembers(
    segment: PathSegment.OverloadedParameter,
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

        val functionBuilder = FunSpec.builder(paramName)
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
            currentAccumulatedParams.constructorArgs(encodedArg),
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

internal fun Route.buildOperationBody(
    config: RenderConfig,
    method: HttpMethod,
    methodClassName: ClassName,
    includeBody: Boolean = true,
    selectedBody: Route.Body? = body?.defaultOrNull(),
    bodyExpr: CodeBlock? = null,
    bodyGuard: String? = null,
    contentType: ContentType? = null,
    requestType: RequestTypeArgument? = null,
): CodeBlock {
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
                bodyGuard = bodyGuard,
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
                bodyGuard,
                contentType,
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
            bodyGuard,
            contentType,
            requestType,
        )
    }
}

private data class OperationRequestInvocationContext(
    val httpMethodName: String,
    val pathLiteral: String,
    val needsRequestConfig: Boolean,
    val bodyMayBeNull: Boolean,
    val returnBody: CodeBlock = CodeBlock.of("%M()", BodyMember),
)

private enum class OperationRequestInvocationStyle {
    ResponseBinding,
    StatementOnly,
    ReturnBody,
}

private fun Route.operationRequestInvocationContext(
    method: HttpMethod,
    includeBody: Boolean,
    selectedBody: Route.Body?,
    contentType: ContentType?,
): OperationRequestInvocationContext =
    OperationRequestInvocationContext(
        httpMethodName = method.value.lowercase(),
        pathLiteral = segments.toPathLiteral(),
        needsRequestConfig = hasRequestConfig(includeBody) || contentType != null,
        bodyMayBeNull = includeBody && when (selectedBody) {
            is Route.Body.OverloadedBody -> false
            is Route.Body.FormUrlEncoded,
            is Route.Body.Multipart.Ref,
            is Route.Body.Multipart.Value,
            is Route.Body.SetBody,
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
    bodyGuard: String?,
    acceptContentType: ContentType?,
    requestType: RequestTypeArgument?,
) {
    if (!request.needsRequestConfig) {
        when (style) {
            OperationRequestInvocationStyle.ResponseBinding ->
                addStatement("val response = client.%L(%L)", request.httpMethodName, request.pathLiteral)

            OperationRequestInvocationStyle.StatementOnly ->
                addStatement("client.%L(%L)", request.httpMethodName, request.pathLiteral)

            OperationRequestInvocationStyle.ReturnBody ->
                addStatement("return client.%L(%L).%L", request.httpMethodName, request.pathLiteral, request.returnBody)
        }
        return
    }

    when (style) {
        OperationRequestInvocationStyle.ResponseBinding ->
            beginControlFlow("val response = client.%L(%L)", request.httpMethodName, request.pathLiteral)

        OperationRequestInvocationStyle.StatementOnly ->
            beginControlFlow("client.%L(%L)", request.httpMethodName, request.pathLiteral)

        OperationRequestInvocationStyle.ReturnBody -> {
            add("return client.%L(%L) {\n", request.httpMethodName, request.pathLiteral)
            indent()
        }
    }

    route.addRequestConfigCode(
        config = config,
        code = this,
        includeBody = includeBody,
        selectedBody = selectedBody,
        bodyMayBeNull = request.bodyMayBeNull,
        bodyExpr = bodyExpr,
        bodyGuard = bodyGuard,
        acceptContentType = acceptContentType,
        requestType = requestType,
    )

    when (style) {
        OperationRequestInvocationStyle.ResponseBinding,
        OperationRequestInvocationStyle.StatementOnly -> endControlFlow()

        OperationRequestInvocationStyle.ReturnBody -> {
            unindent()
            add("}.%L\n", request.returnBody)
        }
    }
}

private fun Route.buildSealedOperationBody(
    config: RenderConfig,
    method: HttpMethod,
    methodClassName: ClassName,
    includeBody: Boolean,
    selectedBody: Route.Body?,
    bodyExpr: CodeBlock?,
    bodyGuard: String?,
    contentType: ContentType?,
    requestType: RequestTypeArgument?,
): CodeBlock {
    val code = CodeBlock.builder()
    val request = operationRequestInvocationContext(method, includeBody, selectedBody, contentType)

    code.addRequestInvocation(
        route = this,
        request = request,
        style = OperationRequestInvocationStyle.ResponseBinding,
        config = config,
        includeBody = includeBody,
        selectedBody = selectedBody,
        bodyExpr = bodyExpr,
        bodyGuard = bodyGuard,
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
    bodyGuard: String?,
    requestType: RequestTypeArgument?,
): CodeBlock {
    val code = CodeBlock.builder()
    val request = operationRequestInvocationContext(method, includeBody, selectedBody, contentType)

    code.addRequestInvocation(
        route = this,
        request = request,
        style = OperationRequestInvocationStyle.ResponseBinding,
        config = config,
        includeBody = includeBody,
        selectedBody = selectedBody,
        bodyExpr = bodyExpr,
        bodyGuard = bodyGuard,
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

private fun Route.buildDirectOperationBody(
    config: RenderConfig,
    method: HttpMethod,
    includeBody: Boolean,
    selectedBody: Route.Body?,
    bodyExpr: CodeBlock?,
    bodyGuard: String?,
    contentType: ContentType?,
    requestType: RequestTypeArgument?,
): CodeBlock {
    val code = CodeBlock.builder()
    val model = returns.singlePreferredModelOrNull()
    val request = operationRequestInvocationContext(method, includeBody, selectedBody, contentType).copy(
        returnBody = bodyCall(
            model = returns.singlePreferredModelOrNull(contentType),
            contentType = contentType ?: returns.singlePreferredContentTypeOrNull(),
        ),
    )

    if (model == null || model is Model.Primitive.Unit) {
        code.addRequestInvocation(
            route = this,
            request = request,
            style = OperationRequestInvocationStyle.StatementOnly,
            config = config,
            includeBody = includeBody,
            selectedBody = selectedBody,
            bodyExpr = bodyExpr,
            bodyGuard = bodyGuard,
            acceptContentType = contentType,
            requestType = requestType,
        )
    } else {
        code.addRequestInvocation(
            route = this,
            request = request,
            style = OperationRequestInvocationStyle.ReturnBody,
            config = config,
            includeBody = includeBody,
            selectedBody = selectedBody,
            bodyExpr = bodyExpr,
            bodyGuard = bodyGuard,
            acceptContentType = contentType,
            requestType = requestType,
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
            val model = returnTypeEntry.types[contentType]
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
    val preferredVariant = variants.firstOrNull { ContentType.Application.Json.match(it.key) } ?: variants.first()
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
            errorContentType.toContentTypeCodeBlock(),
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
    val preferredVariant = variants.firstOrNull { ContentType.Application.Json.match(it.first) } ?: variants.first()
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
            errorContentType.toContentTypeCodeBlock(),
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
        val defaultModel = returns.default!!.types[contentType] ?: returns.default!!.preferredModel()
        val defaultContentType =
            if (returns.default!!.types[contentType] != null) contentType else returns.default!!.preferredContentType()
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
    bodyGuard: String? = null,
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
        val paramName = param.name.toParamName()
        val publicModel = param.type.publicInputModelOrSelf()
        val valueExpr = publicModel.wireValueExpr(paramName)
        if (param.isRequired) {
            code.addStatement("%M(%S, %L)", ParameterMember, param.name, valueExpr)
        } else {
            val itExpr = publicModel.wireItExpr()
            code.addStatement("%L?.let·{ %M(%S, %L) }", paramName, ParameterMember, param.name, itExpr)
        }
    }

    for (param in nonPathParams.filter { it.input == Parameter.Input.Header }) {
        val paramName = param.name.toParamName()
        val publicModel = param.type.publicInputModelOrSelf()
        val valueExpr = publicModel.wireValueExpr(paramName)
        if (param.isRequired) {
            code.addStatement("%M(%S, %L)", HeaderMember, param.name, valueExpr)
        } else {
            val itExpr = publicModel.wireItExpr()
            code.addStatement("%L?.let·{ %M(%S, %L) }", paramName, HeaderMember, param.name, itExpr)
        }
    }

    for (param in nonPathParams.filter { it.input == Parameter.Input.Cookie }) {
        val paramName = param.name.toParamName()
        val publicModel = param.type.publicInputModelOrSelf()
        val valueExpr = publicModel.wireValueExpr(paramName)
        if (param.isRequired) {
            code.addStatement("%M(%S, %L)", CookieMember, param.name, valueExpr)
        } else {
            val itExpr = publicModel.wireItExpr()
            code.addStatement("%L?.let·{ %M(%S, %L) }", paramName, CookieMember, param.name, itExpr)
        }
    }

    if (includeBody && selectedBody != null) {
        addBodyCode(
            config = config,
            code = code,
            body = selectedBody,
            bodyMayBeNull = bodyMayBeNull,
            bodyExpr = bodyExpr,
            bodyGuard = bodyGuard,
            requestType = requestType,
        )
    }
}

/**
 * Emits a KotlinPoet [CodeBlock] for `contentType(…)` matching the given [ContentType].
 *
 * Well-known Ktor named constants are preferred so the generated code is idiomatic.
 * Unknown types fall back to the `ContentType(contentType, subtype)` constructor.
 */
@Suppress("CyclomaticComplexMethod")
internal fun ContentType.toContentTypeCodeBlock(): CodeBlock {
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
    bodyGuard: String? = null,
    requestType: RequestTypeArgument? = null,
) {
    when (body) {
        is Route.Body.SetBody -> {
            val contentType = requestType?.contentTypeCode ?: body.contentType.toContentTypeCodeBlock()
            code.addSetLikeBodyCode(contentType, bodyExpr, bodyMayBeNull, bodyGuard) { value ->
                if (requestType == null) {
                    addStatement("%M(%L)", SetBodyMember, setBodyArgument(body.type, body.contentType, value))
                } else {
                    addRequestTypeSetBody(requestType, value)
                }
            }
        }

        is Route.Body.OverloadedBody -> {
            val contentType = requestType?.contentTypeCode ?: body.contentType.toContentTypeCodeBlock()
            code.addSetLikeBodyCode(contentType, bodyExpr, bodyMayBeNull, bodyGuard) { value ->
                addStatement("%M(%L)", SetBodyMember, value)
            }
        }

        is Route.Body.FormUrlEncoded -> code.addFormUrlEncodedBodyCode(body)
        is Route.Body.Multipart.Value -> code.addMultipartValueBodyCode(config, body)
        is Route.Body.Multipart.Ref -> code.addMultipartRefBodyCode(bodyMayBeNull)
    }
}

/**
 * `setBody(…)` for a body whose content type is chosen at runtime through `requestType`: the clashing variants share
 * one Kotlin signature, but a `String` must still become a JSON string for the JSON media types among them.
 */
private fun CodeBlock.Builder.addRequestTypeSetBody(requestType: RequestTypeArgument, value: String) {
    val arguments = requestType.variants.map { variant ->
        val model = (variant.body as? Route.Body.SetBody)?.type
        setBodyArgument(model, variant.contentType, value)
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
    bodyGuard: String?,
    setBody: CodeBlock.Builder.(value: String) -> Unit,
) {
    if (bodyExpr != null) {
        if (bodyMayBeNull && bodyGuard != null) {
            beginControlFlow("if (%L)", bodyGuard)
            addStatement("%M(%L)", ContentTypeFunMember, contentType)
            addStatement("%M(%L)", SetBodyMember, bodyExpr)
            endControlFlow()
        } else {
            addStatement("%M(%L)", ContentTypeFunMember, contentType)
            addStatement("%M(%L)", SetBodyMember, bodyExpr)
        }
    } else if (!bodyMayBeNull) {
        addStatement("%M(%L)", ContentTypeFunMember, contentType)
        setBody("body")
    } else {
        beginControlFlow("body?.let")
        addStatement("%M(%L)", ContentTypeFunMember, contentType)
        setBody("it")
        endControlFlow()
    }
}

private fun CodeBlock.Builder.addFormUrlEncodedBodyCode(body: Route.Body.FormUrlEncoded) {
    add("%M(%T.build {\n", SetBodyMember, ParametersType)
    indent()
    for (formData in body.parameters) {
        val paramName = formData.name.toParamName()
        val valueExpr = formData.type.wireValueExpr(paramName)
        addStatement("append(%S, %L)", formData.name, valueExpr)
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

private fun List<AccumulatedParam>.constructorArgs(extraArg: String? = null): String =
    buildList {
        add("client")
        addAll(this@constructorArgs.map { it.name.toCamelCase() })
        extraArg?.let(::add)
    }.joinToString(", ")

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

private fun responseBody(model: Model?, contentType: ContentType?): CodeBlock =
    CodeBlock.of("response.%L", bodyCall(model, contentType))

private fun ContentType.isJsonLike(): Boolean =
    ContentType.Application.Json.match(this) || contentSubtype.endsWith("+json")

private fun Model.isJsonLikeMultipartModel(): Boolean = when (this) {
    is Model.ByteArray,
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

/**
 * Returns the expression that produces the wire string for a named parameter.
 * For enums with a `value` property, appends `.value`. Otherwise falls back to `toString()`.
 */
private fun Model.wireValueExpr(paramName: String): String =
    when {
        this is Model.Enum && needsValueProperty() -> "$paramName.value"
        this is Model.Enum -> "$paramName.toString()"
        this is Model.Date || this is Model.DateTime || this is Model.Uuid -> "$paramName.toString()"
        else -> paramName
    }

/**
 * Returns the expression to use inside `?.let { ... }` lambdas when accessing the wire value.
 * For enums with a `value` property, returns `"it.value"`. Otherwise falls back to `"it.toString()"`.
 */
private fun Model.wireItExpr(): String =
    when {
        this is Model.Enum && needsValueProperty() -> "it.value"
        this is Model.Enum -> "it.toString()"
        this is Model.Date || this is Model.DateTime || this is Model.Uuid -> "it.toString()"
        else -> "it"
    }
