@file:Suppress("TooManyFunctions")

package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.transformers.nestedOrNull
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kotlin.jvm.JvmName

private fun deprecatedAnnotation(): AnnotationSpec =
    AnnotationSpec.builder(Deprecated::class)
        .addMember("%S", "Deprecated by the API provider")
        .build()

context(route: Route)
private fun FunSpec.Builder.addDeprecatedIfNeeded() = apply {
    if (route.deprecated) addAnnotation(deprecatedAnnotation())
}

context(route: Route)
private fun TypeSpec.Builder.addDeprecatedIfNeeded() = apply {
    if (route.deprecated) addAnnotation(deprecatedAnnotation())
}

context(route: Route)
private fun PropertySpec.Builder.addDeprecatedIfNeeded() = apply {
    if (route.deprecated) addAnnotation(deprecatedAnnotation())
}

private fun methodTypeName(method: HttpMethod): String =
    method.value.lowercase().replaceFirstChar { it.uppercase() }

internal fun Route.toOperationPropertySpec(
    method: HttpMethod,
    pathClassName: ClassName,
    accumulatedParams: List<AccumulatedParam>,
): PropertySpec {
    val methodName = method.value.lowercase()
    val methodClassName = pathClassName.nestedClass(methodTypeName(method))
    return PropertySpec.builder(methodName, methodClassName)
        .addDeprecatedIfNeeded()
        .initializer("%T(%L)", methodClassName, operationConstructorArgs(accumulatedParams))
        .build()
}

internal fun Route.toOperationTypeSpec(
    method: HttpMethod,
    config: RenderConfig,
    pathClassName: ClassName,
    sharedInlineParameterKeys: Set<String>,
    accumulatedParams: List<AccumulatedParam>,
): TypeSpec {
    val methodClassName = pathClassName.nestedClass(methodTypeName(method))
    val overloadedBody = body?.defaultOrNull() as? Route.Body.OverloadedBody
    val inlineModelScope = operationInlineModelScope(config, methodClassName)
    val flattenedBody = body?.flattenedBodyRendering(config, methodClassName)
    val inlineBodyTypeSpec = body?.inlineBodyTypeSpec(
        config = config,
        ownerClassName = methodClassName,
        externalTypeNames = flattenedBody?.externalTypeNames.orEmpty(),
        internal = flattenedBody != null,
    )
    val routeInlineParameterModels = routeSpecificInlineParameterModels(sharedInlineParameterKeys)

    return TypeSpec.classBuilder(methodTypeName(method))
        .addDeprecatedIfNeeded()
        .apply {
            addOperationTypeContent(
                OperationTypeSpecContext(
                    route = this@toOperationTypeSpec,
                    config = config,
                    pathClassName = pathClassName,
                    sharedInlineParameterKeys = sharedInlineParameterKeys,
                    accumulatedParams = accumulatedParams,
                    methodClassName = methodClassName,
                    overloadedBody = overloadedBody,
                    inlineModelScope = inlineModelScope,
                    flattenedBody = flattenedBody,
                    inlineBodyTypeSpec = inlineBodyTypeSpec,
                    routeInlineParameterModels = routeInlineParameterModels,
                )
            )
        }
        .build()
}

private fun operationConstructorArgs(accumulatedParams: List<AccumulatedParam>): String =
    buildList {
        add("client")
        addAll(accumulatedParams.map { it.name.toCamelCase() })
    }.joinToString(", ")

private fun operationFunBuilder(functionName: String, operator: Boolean): FunSpec.Builder =
    FunSpec.builder(functionName)
        .addModifiers(KModifier.SUSPEND)
        .apply {
            if (operator) addModifiers(KModifier.OPERATOR)
        }

private data class OperationTypeSpecContext(
    val route: Route,
    val config: RenderConfig,
    val pathClassName: ClassName,
    val sharedInlineParameterKeys: Set<String>,
    val accumulatedParams: List<AccumulatedParam>,
    val methodClassName: ClassName,
    val overloadedBody: Route.Body.OverloadedBody?,
    val inlineModelScope: OperationInlineModelScope,
    val flattenedBody: FlattenedBodyRendering?,
    val inlineBodyTypeSpec: TypeSpec?,
    val routeInlineParameterModels: List<InlineParameterModel>,
)

private fun TypeSpec.Builder.addOperationTypeContent(context: OperationTypeSpecContext) {
    addClientConstructorAndState(context.config, context.accumulatedParams)
    addOperationInlineTypes(context)
    addOperationResponseType(context)
    addOperationInvokeFunctions(context)
}

private fun TypeSpec.Builder.addOperationInlineTypes(context: OperationTypeSpecContext) {
    context.routeInlineParameterModels.forEach { inline ->
        inline.model
            .toInlineParameterTypeSpec(context.config, context.methodClassName, inline.simpleName)
            ?.let(::addType)
    }
    context.overloadedBody
        ?.directInlineTypeSpecs(context.config, context.methodClassName, context.inlineModelScope)
        ?.forEach(::addType)
    context.flattenedBody?.publicTypeSpecs?.forEach(::addType)
    context.inlineModelScope.methodTypeSpecs(context.config).forEach(::addType)
    context.inlineBodyTypeSpec?.let(::addType)
}

private fun TypeSpec.Builder.addOperationResponseType(context: OperationTypeSpecContext) {
    when (val strategy = context.route.returns.contentTypeStrategy()) {
        ContentTypeStrategy.SingleContentType -> {
            if (!context.route.returns.isSingleUnitResponse() && !context.route.returns.isSingleDirectModelResponse()) {
                addType(context.route.buildResponseTypeSpec(context.config, context.methodClassName, context.inlineModelScope))
            }
        }

        is ContentTypeStrategy.SeparateMethods -> {
            context.route.buildMultiContentTypeResponseSpecs(
                config = context.config,
                methodClassName = context.methodClassName,
                inlineModelScope = context.inlineModelScope,
            ).forEach(::addType)
        }
    }
}

@Suppress("LongMethod")
private fun TypeSpec.Builder.addOperationInvokeFunctions(context: OperationTypeSpecContext) {
    val bodyVariants = context.route.body?.variants().orEmpty()
    val overloadedBody = context.overloadedBody
    val requestBodyStrategy = if (bodyVariants.size > 1) {
        context.route.body!!.detectSignatureClashes(context.config)
    } else {
        null
    }
    if (requestBodyStrategy is RequestBodyStrategy.ClashingWithEnum) {
        addType(buildRequestTypeEnum(requestBodyStrategy.clashing))
    }
    when (val strategy = context.route.returns.contentTypeStrategy()) {
        ContentTypeStrategy.SingleContentType ->
            addOperationInvokeFunctionsForContentType(
                context = context,
                bodyVariants = bodyVariants,
                overloadedBody = overloadedBody,
                requestBodyStrategy = requestBodyStrategy,
                contentType = null,
                functionName = "invoke",
                operator = true,
            )

        is ContentTypeStrategy.SeparateMethods ->
            strategy.successContentTypes.forEach { contentType ->
                addOperationInvokeFunctionsForContentType(
                    context = context,
                    bodyVariants = bodyVariants,
                    overloadedBody = overloadedBody,
                    requestBodyStrategy = requestBodyStrategy,
                    contentType = contentType,
                    functionName = contentTypeToMethodName(contentType),
                    operator = false,
                )
            }
    }
}

@Suppress("LongMethod")
private fun TypeSpec.Builder.addOperationInvokeFunctionsForContentType(
    context: OperationTypeSpecContext,
    bodyVariants: List<Route.Bodies.Variant>,
    overloadedBody: Route.Body.OverloadedBody?,
    requestBodyStrategy: RequestBodyStrategy?,
    contentType: ContentType?,
    functionName: String,
    operator: Boolean,
) {
    when {
        requestBodyStrategy is RequestBodyStrategy.ClashingWithEnum -> {
            addFunction(
                context.route.toInvokeFunSpecWithRequestType(
                    config = context.config,
                    pathClassName = context.pathClassName,
                    methodClassName = context.methodClassName,
                    sharedInlineParameterKeys = context.sharedInlineParameterKeys,
                    usesNestedBodyType = false,
                    inlineModelScope = context.inlineModelScope,
                    selectedBody = requestBodyStrategy.clashing.first().body,
                    functionName = functionName,
                    operator = operator,
                    contentType = contentType,
                )
            )
            requestBodyStrategy.unique.forEach { variant ->
                addFunction(
                    context.route.toInvokeFunSpec(
                        config = context.config,
                        pathClassName = context.pathClassName,
                        methodClassName = context.methodClassName,
                        sharedInlineParameterKeys = context.sharedInlineParameterKeys,
                        usesNestedBodyType = false,
                        inlineModelScope = context.inlineModelScope,
                        selectedBody = variant.body,
                        functionName = functionName,
                        operator = operator,
                        contentType = contentType,
                    )
                )
            }
        }

        bodyVariants.size > 1 -> when (val strategy = requestBodyStrategy) {
            is RequestBodyStrategy.SeparateOverloads -> strategy.variants.forEach { variant ->
                addFunction(
                    context.route.toInvokeFunSpec(
                        config = context.config,
                        pathClassName = context.pathClassName,
                        methodClassName = context.methodClassName,
                        sharedInlineParameterKeys = context.sharedInlineParameterKeys,
                        usesNestedBodyType = false,
                        inlineModelScope = context.inlineModelScope,
                        selectedBody = variant.body,
                        functionName = functionName,
                        operator = operator,
                        contentType = contentType,
                    )
                )
            }

            is RequestBodyStrategy.Single -> addFunction(
                context.route.toInvokeFunSpec(
                    config = context.config,
                    pathClassName = context.pathClassName,
                    methodClassName = context.methodClassName,
                    sharedInlineParameterKeys = context.sharedInlineParameterKeys,
                    usesNestedBodyType = false,
                    inlineModelScope = context.inlineModelScope,
                    selectedBody = strategy.variant.body,
                    functionName = functionName,
                    operator = operator,
                    contentType = contentType,
                )
            )

            is RequestBodyStrategy.ClashingWithEnum -> error("Unreachable")

            null -> error("Unreachable")
        }

        context.flattenedBody != null -> context.flattenedBody.overloads.forEach { overload ->
            addFunction(
                context.route.toInvokeFunSpecForFlattenedBody(
                    config = context.config,
                    pathClassName = context.pathClassName,
                    methodClassName = context.methodClassName,
                    sharedInlineParameterKeys = context.sharedInlineParameterKeys,
                    overload = overload,
                    inlineModelScope = context.inlineModelScope,
                    functionName = functionName,
                    operator = operator,
                    contentType = contentType,
                )
            )
        }

        overloadedBody != null -> {
            if (context.route.body?.required != true) {
                addFunction(
                    context.route.toInvokeFunSpecForOptionalOverloadedBodyNoBody(
                        config = context.config,
                        pathClassName = context.pathClassName,
                        methodClassName = context.methodClassName,
                        sharedInlineParameterKeys = context.sharedInlineParameterKeys,
                        inlineModelScope = context.inlineModelScope,
                        functionName = functionName,
                        operator = operator,
                        contentType = contentType,
                    )
                )
            }
            overloadedBody
                .distinctCaseSignatures(context.config, context.inlineModelScope)
                .forEach { caseSignature ->
                    addFunction(
                        context.route.toInvokeFunSpecForOverloadedBodyCase(
                            config = context.config,
                            pathClassName = context.pathClassName,
                            methodClassName = context.methodClassName,
                            sharedInlineParameterKeys = context.sharedInlineParameterKeys,
                            bodyTypeName = caseSignature.typeName,
                            bodyJvmName = caseSignature.jvmName,
                            inlineModelScope = context.inlineModelScope,
                            functionName = functionName,
                            operator = operator,
                            contentType = contentType,
                        )
                    )
                }
        }

        else -> addFunction(
            context.route.toInvokeFunSpec(
                config = context.config,
                pathClassName = context.pathClassName,
                methodClassName = context.methodClassName,
                sharedInlineParameterKeys = context.sharedInlineParameterKeys,
                usesNestedBodyType = context.inlineBodyTypeSpec != null,
                inlineModelScope = context.inlineModelScope,
                functionName = functionName,
                operator = operator,
                contentType = contentType,
            )
        )
    }
}

/** Build the operation invoke(...) signature with parameters and response wrapper. */
@Suppress("LongParameterList")
private fun Route.toInvokeFunSpec(
    config: RenderConfig,
    pathClassName: ClassName,
    methodClassName: ClassName,
    sharedInlineParameterKeys: Set<String>,
    usesNestedBodyType: Boolean,
    inlineModelScope: OperationInlineModelScope,
    functionName: String = "invoke",
    operator: Boolean = true,
    contentType: ContentType? = null,
    selectedBody: Route.Body? = body?.defaultOrNull(),
): FunSpec {
    val builder = operationFunBuilder(functionName, operator)
        .addDeprecatedIfNeeded()
    val signatureTypes = mutableListOf<TypeName>()

    val nonPathParams = parameters.filter { it.input != Parameter.Input.Path }
    val requiredParams = nonPathParams.filter { it.isRequired }.sortedBy { it.input.sortOrder() }
    val optionalParams = nonPathParams.filter { !it.isRequired }.sortedBy { it.input.sortOrder() }

    val bodyParams = selectedBody?.toInvokeParameterSpecs(
        config = config,
        methodClassName = methodClassName,
        usesNestedBodyType = usesNestedBodyType,
        required = body?.required == true,
    )
    val bodyRequired = body?.required == true

    for (input in requiredParams) {
        val parameter = input.toParameterSpec(
            config = config,
            pathClassName = pathClassName,
            methodClassName = methodClassName,
            sharedInlineParameterKeys = sharedInlineParameterKeys,
        )
        builder.addParameter(parameter)
        signatureTypes += parameter.type
    }
    if (bodyRequired && bodyParams != null) {
        bodyParams.forEach { parameter ->
            builder.addParameter(parameter)
            signatureTypes += parameter.type
        }
    }
    for (input in optionalParams) {
        val parameter = input.toParameterSpec(
            config = config,
            pathClassName = pathClassName,
            methodClassName = methodClassName,
            sharedInlineParameterKeys = sharedInlineParameterKeys,
        )
        builder.addParameter(parameter)
        signatureTypes += parameter.type
    }
    if (!bodyRequired && bodyParams != null) {
        bodyParams.forEach { parameter ->
            builder.addParameter(parameter)
            signatureTypes += parameter.type
        }
    }

    val returnType = invokeReturnType(config, methodClassName, inlineModelScope, contentType)
    @Suppress("SpreadOperator")
    return builder.returns(returnType)
        .addExperimentalUuidOptInIfNeeded(signatureTypes + returnType)
        .addCode(buildOperationBody(config, method, methodClassName, selectedBody = selectedBody, contentType = contentType))
        .build()
}

@Suppress("LongParameterList")
private fun Route.toInvokeFunSpecWithRequestType(
    config: RenderConfig,
    pathClassName: ClassName,
    methodClassName: ClassName,
    sharedInlineParameterKeys: Set<String>,
    usesNestedBodyType: Boolean,
    inlineModelScope: OperationInlineModelScope,
    functionName: String = "invoke",
    operator: Boolean = true,
    contentType: ContentType? = null,
    selectedBody: Route.Body? = body?.defaultOrNull(),
): FunSpec {
    val builder = operationFunBuilder(functionName, operator)
        .addDeprecatedIfNeeded()
    val signatureTypes = mutableListOf<TypeName>()

    val nonPathParams = parameters.filter { it.input != Parameter.Input.Path }
    val requiredParams = nonPathParams.filter { it.isRequired }.sortedBy { it.input.sortOrder() }
    val optionalParams = nonPathParams.filter { !it.isRequired }.sortedBy { it.input.sortOrder() }

    val bodyParams = selectedBody?.toInvokeParameterSpecs(
        config = config,
        methodClassName = methodClassName,
        usesNestedBodyType = usesNestedBodyType,
        required = body?.required == true,
    )

    for (input in requiredParams) {
        val parameter = input.toParameterSpec(
            config = config,
            pathClassName = pathClassName,
            methodClassName = methodClassName,
            sharedInlineParameterKeys = sharedInlineParameterKeys,
        )
        builder.addParameter(parameter)
        signatureTypes += parameter.type
    }
    if (bodyParams != null) {
        bodyParams.forEach { parameter ->
            builder.addParameter(parameter)
            signatureTypes += parameter.type
        }
    }

    val requestTypeParameter = ParameterSpec.builder("requestType", methodClassName.nestedClass("RequestType")).build()
    builder.addParameter(requestTypeParameter)
    signatureTypes += requestTypeParameter.type

    for (input in optionalParams) {
        val parameter = input.toParameterSpec(
            config = config,
            pathClassName = pathClassName,
            methodClassName = methodClassName,
            sharedInlineParameterKeys = sharedInlineParameterKeys,
        )
        builder.addParameter(parameter)
        signatureTypes += parameter.type
    }

    val returnType = invokeReturnType(config, methodClassName, inlineModelScope, contentType)
    @Suppress("SpreadOperator")
    return builder.returns(returnType)
        .addExperimentalUuidOptInIfNeeded(signatureTypes + returnType)
        .addCode(
            buildOperationBody(
                config,
                method,
                methodClassName,
                selectedBody = selectedBody,
                contentType = contentType,
                requestTypeContentType = CodeBlock.of("requestType.contentType"),
            )
        )
        .build()
}

@Suppress("LongParameterList")
private fun Route.toInvokeFunSpecForFlattenedBody(
    config: RenderConfig,
    pathClassName: ClassName,
    methodClassName: ClassName,
    sharedInlineParameterKeys: Set<String>,
    overload: FlattenedBodyOverload,
    inlineModelScope: OperationInlineModelScope,
    functionName: String = "invoke",
    operator: Boolean = true,
    contentType: ContentType? = null,
): FunSpec {
    val builder = operationFunBuilder(functionName, operator)
        .addDeprecatedIfNeeded()
    val signatureTypes = mutableListOf<TypeName>()

    val nonPathParams = parameters.filter { it.input != Parameter.Input.Path }
    val requiredParams = nonPathParams.filter { it.isRequired }.sortedBy { it.input.sortOrder() }
    val optionalParams = nonPathParams.filter { !it.isRequired }.sortedBy { it.input.sortOrder() }

    for (input in requiredParams) {
        val parameter = input.toParameterSpec(
            config = config,
            pathClassName = pathClassName,
            methodClassName = methodClassName,
            sharedInlineParameterKeys = sharedInlineParameterKeys,
        )
        builder.addParameter(parameter)
        signatureTypes += parameter.type
    }

    if (overload.bodyParamsBeforeOptionalParams) {
        overload.parameters.forEach { parameter ->
            builder.addParameter(parameter)
            signatureTypes += parameter.type
        }
    }

    for (input in optionalParams) {
        val parameter = input.toParameterSpec(
            config = config,
            pathClassName = pathClassName,
            methodClassName = methodClassName,
            sharedInlineParameterKeys = sharedInlineParameterKeys,
        )
        builder.addParameter(parameter)
        signatureTypes += parameter.type
    }

    if (!overload.bodyParamsBeforeOptionalParams) {
        overload.parameters.forEach { parameter ->
            builder.addParameter(parameter)
            signatureTypes += parameter.type
        }
    }

    val returnType = invokeReturnType(config, methodClassName, inlineModelScope, contentType)
    builder.returns(returnType)
    builder.addExperimentalUuidOptInIfNeeded(signatureTypes + returnType)
    builder.addCode(
        buildFlattenedBodyOperationBody(
            config = config,
            method = method,
            methodClassName = methodClassName,
            overload = overload,
        )
    )
    return builder.build()
}

@Suppress("LongParameterList")
private fun Route.toInvokeFunSpecForOverloadedBodyCase(
    config: RenderConfig,
    pathClassName: ClassName,
    methodClassName: ClassName,
    sharedInlineParameterKeys: Set<String>,
    bodyTypeName: TypeName,
    bodyJvmName: String?,
    inlineModelScope: OperationInlineModelScope,
    functionName: String = "invoke",
    operator: Boolean = true,
    contentType: ContentType? = null,
): FunSpec {
    val builder = operationFunBuilder(functionName, operator)
        .addDeprecatedIfNeeded()
    val signatureTypes = mutableListOf<TypeName>()
    bodyJvmName?.let { jvmName ->
        builder.addAnnotation(
            AnnotationSpec.builder(JvmName::class)
                .addMember("%S", jvmName)
                .build()
        )
    }

    val nonPathParams = parameters.filter { it.input != Parameter.Input.Path }
    val requiredParams = nonPathParams.filter { it.isRequired }.sortedBy { it.input.sortOrder() }
    val optionalParams = nonPathParams.filter { !it.isRequired }.sortedBy { it.input.sortOrder() }
    val bodyParameter = ParameterSpec.builder("body", bodyTypeName).build()

    for (input in requiredParams) {
        val parameter = input.toParameterSpec(
            config = config,
            pathClassName = pathClassName,
            methodClassName = methodClassName,
            sharedInlineParameterKeys = sharedInlineParameterKeys,
        )
        builder.addParameter(parameter)
        signatureTypes += parameter.type
    }
    builder.addParameter(bodyParameter)
    signatureTypes += bodyParameter.type
    for (input in optionalParams) {
        val parameter = input.toParameterSpec(
            config = config,
            pathClassName = pathClassName,
            methodClassName = methodClassName,
            sharedInlineParameterKeys = sharedInlineParameterKeys,
        )
        builder.addParameter(parameter)
        signatureTypes += parameter.type
    }

    val returnType = invokeReturnType(config, methodClassName, inlineModelScope, contentType)
    builder.returns(returnType)
    builder.addExperimentalUuidOptInIfNeeded(signatureTypes + returnType)
    builder.addCode(buildOperationBody(config, method, methodClassName, contentType = contentType))
    return builder.build()
}

private fun Route.toInvokeFunSpecForOptionalOverloadedBodyNoBody(
    config: RenderConfig,
    pathClassName: ClassName,
    methodClassName: ClassName,
    sharedInlineParameterKeys: Set<String>,
    inlineModelScope: OperationInlineModelScope,
    functionName: String = "invoke",
    operator: Boolean = true,
    contentType: ContentType? = null,
): FunSpec {
    val builder = operationFunBuilder(functionName, operator)
        .addDeprecatedIfNeeded()
    val signatureTypes = mutableListOf<TypeName>()

    val nonPathParams = parameters.filter { it.input != Parameter.Input.Path }
    val requiredParams = nonPathParams.filter { it.isRequired }.sortedBy { it.input.sortOrder() }
    val optionalParams = nonPathParams.filter { !it.isRequired }.sortedBy { it.input.sortOrder() }

    for (input in requiredParams) {
        val parameter = input.toParameterSpec(
            config = config,
            pathClassName = pathClassName,
            methodClassName = methodClassName,
            sharedInlineParameterKeys = sharedInlineParameterKeys,
        )
        builder.addParameter(parameter)
        signatureTypes += parameter.type
    }
    for (input in optionalParams) {
        val parameter = input.toParameterSpec(
            config = config,
            pathClassName = pathClassName,
            methodClassName = methodClassName,
            sharedInlineParameterKeys = sharedInlineParameterKeys,
        )
        builder.addParameter(parameter)
        signatureTypes += parameter.type
    }

    val returnType = invokeReturnType(config, methodClassName, inlineModelScope, contentType)
    builder.returns(returnType)
    builder.addExperimentalUuidOptInIfNeeded(signatureTypes + returnType)
    builder.addCode(buildOperationBody(config, method, methodClassName, includeBody = false, contentType = contentType))
    return builder.build()
}

@Suppress("MagicNumber")
private fun Parameter.Input.sortOrder(): Int = when (this) {
    Parameter.Input.Query -> 0
    Parameter.Input.Header -> 1
    Parameter.Input.Cookie -> 2
    Parameter.Input.Path -> 3
}

@Suppress("CyclomaticComplexMethod")
private fun Route.Input.toParameterSpec(
    config: RenderConfig,
    pathClassName: ClassName,
    methodClassName: ClassName,
    sharedInlineParameterKeys: Set<String>,
): ParameterSpec {
    val paramName = name.toParamName()
    val model = type
    val publicModel = model.publicInputModelOrSelf()
    val resolvedInlineParameterClassName = inlineParameterClassName(
        pathClassName = pathClassName,
        methodClassName = methodClassName,
        sharedInlineParameterKeys = sharedInlineParameterKeys,
    )

    val baseTypeName = inlineParameterTypeName(
        config = config,
        pathClassName = pathClassName,
        methodClassName = methodClassName,
        sharedInlineParameterKeys = sharedInlineParameterKeys,
    ) ?: publicModel.toTypeName(config)

    val typeName = if (!isRequired) baseTypeName.copy(nullable = true) else baseTypeName

    val literalDefault = if (publicModel is Model.Enum && publicModel.nestedOrNull() != null) {
        val enumClassName = resolvedInlineParameterClassName ?: publicModel.context.toClassName(config)
        when (val d = publicModel.default) {
            null -> null
            Model.Default.Null -> CodeBlock.of("null")
            is Model.Default.Value -> CodeBlock.of("%T.%L", enumClassName, toEnumValueName(d.value))
        }
    } else {
        publicModel.defaultLiteral(config)
    }

    return ParameterSpec.builder(paramName, typeName).apply {
        when {
            isRequired && literalDefault != null -> defaultValue(literalDefault)
            !isRequired && literalDefault != null -> defaultValue(literalDefault)
            !isRequired -> defaultValue(CodeBlock.of("null"))
        }
    }.build()
}

private fun Route.Body.toInvokeParameterSpecs(
    config: RenderConfig,
    methodClassName: ClassName,
    usesNestedBodyType: Boolean,
    required: Boolean,
): List<ParameterSpec>? = when (this) {
    is Route.Body.SetBody -> {
        val bodyType = if (usesNestedBodyType) {
            methodClassName.nestedClass("Body")
        } else {
            type.toTypeName(config)
        }
        val typeName = if (!required) bodyType.copy(nullable = true) else bodyType
        listOf(
            ParameterSpec.builder("body", typeName).apply {
                if (!required) defaultValue(CodeBlock.of("null"))
            }.build()
        )
    }

    is Route.Body.OverloadedBody -> {
        val bodyType = if (usesNestedBodyType) {
            methodClassName.nestedClass("Body")
        } else {
            type.toTypeName(config)
        }
        val typeName = if (!required) bodyType.copy(nullable = true) else bodyType
        listOf(
            ParameterSpec.builder("body", typeName).apply {
                if (!required) defaultValue(CodeBlock.of("null"))
            }.build()
        )
    }

    is Route.Body.FormUrlEncoded -> {
        parameters.map { formData ->
            val typeName =
                formData.type.toTypeName(config).copy(nullable = formData.type.isNullable || !formData.isRequired)
            ParameterSpec.builder(formData.name.toParamName(), typeName).apply {
                if (!formData.isRequired) {
                    defaultValue(CodeBlock.of("null"))
                }
            }.build()
        }
    }

    is Route.Body.Multipart.Value -> {
        parameters.map { formData ->
            val typeName =
                formData.type.toTypeName(config).copy(nullable = formData.type.isNullable || !formData.isRequired)
            ParameterSpec.builder(formData.name.toParamName(), typeName).apply {
                if (!formData.isRequired) {
                    defaultValue(CodeBlock.of("null"))
                }
            }.build()
        }
    }

    is Route.Body.Multipart.Ref -> {
        val typeName = value.toTypeName(config).let {
            if (!required) it.copy(nullable = true) else it
        }
        listOf(
            ParameterSpec.builder("body", typeName).apply {
                if (!required) defaultValue(CodeBlock.of("null"))
            }.build()
        )
    }
}
