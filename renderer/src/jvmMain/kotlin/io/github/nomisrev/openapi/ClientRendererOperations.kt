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
    if (!context.route.returns.isSingleUnitResponse() && !context.route.returns.isSingleDirectModelResponse()) {
        addType(context.route.buildResponseTypeSpec(context.config, context.methodClassName, context.inlineModelScope))
    }
}

private fun TypeSpec.Builder.addOperationInvokeFunctions(context: OperationTypeSpecContext) {
    if (context.flattenedBody != null) {
        context.flattenedBody.overloads.forEach { overload ->
            addFunction(
                context.route.toInvokeFunSpecForFlattenedBody(
                    config = context.config,
                    pathClassName = context.pathClassName,
                    methodClassName = context.methodClassName,
                    sharedInlineParameterKeys = context.sharedInlineParameterKeys,
                    overload = overload,
                    inlineModelScope = context.inlineModelScope,
                )
            )
        }
        return
    }

    val overloadedBody = context.overloadedBody
    if (overloadedBody != null) {
        if (context.route.body?.required != true) {
            addFunction(
                context.route.toInvokeFunSpecForOptionalOverloadedBodyNoBody(
                    config = context.config,
                    pathClassName = context.pathClassName,
                    methodClassName = context.methodClassName,
                    sharedInlineParameterKeys = context.sharedInlineParameterKeys,
                    inlineModelScope = context.inlineModelScope,
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
                    )
                )
            }
        return
    }

    addFunction(
        context.route.toInvokeFunSpec(
            config = context.config,
            pathClassName = context.pathClassName,
            methodClassName = context.methodClassName,
            sharedInlineParameterKeys = context.sharedInlineParameterKeys,
            usesNestedBodyType = context.inlineBodyTypeSpec != null,
            inlineModelScope = context.inlineModelScope,
        )
    )
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
): FunSpec {
    val builder = FunSpec.builder("invoke")
        .addModifiers(KModifier.SUSPEND, KModifier.OPERATOR)
        .addDeprecatedIfNeeded()
    val signatureTypes = mutableListOf<TypeName>()

    val nonPathParams = parameters.filter { it.input != Parameter.Input.Path }
    val requiredParams = nonPathParams.filter { it.isRequired }.sortedBy { it.input.sortOrder() }
    val optionalParams = nonPathParams.filter { !it.isRequired }.sortedBy { it.input.sortOrder() }

    val bodyParams = body?.toInvokeParameterSpecs(config, methodClassName, usesNestedBodyType)
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

    val returnType = invokeReturnType(config, methodClassName, inlineModelScope)
    @Suppress("SpreadOperator")
    return builder.returns(returnType)
        .addExperimentalUuidOptInIfNeeded(signatureTypes + returnType)
        .addCode(buildOperationBody(method, methodClassName))
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
): FunSpec {
    val builder = FunSpec.builder("invoke")
        .addModifiers(KModifier.SUSPEND, KModifier.OPERATOR)
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

    val returnType = invokeReturnType(config, methodClassName, inlineModelScope)
    builder.returns(returnType)
    builder.addExperimentalUuidOptInIfNeeded(signatureTypes + returnType)
    builder.addCode(
        buildFlattenedBodyOperationBody(
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
): FunSpec {
    val builder = FunSpec.builder("invoke")
        .addModifiers(KModifier.SUSPEND, KModifier.OPERATOR)
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

    val returnType = invokeReturnType(config, methodClassName, inlineModelScope)
    builder.returns(returnType)
    builder.addExperimentalUuidOptInIfNeeded(signatureTypes + returnType)
    builder.addCode(buildOperationBody(method, methodClassName))
    return builder.build()
}

private fun Route.toInvokeFunSpecForOptionalOverloadedBodyNoBody(
    config: RenderConfig,
    pathClassName: ClassName,
    methodClassName: ClassName,
    sharedInlineParameterKeys: Set<String>,
    inlineModelScope: OperationInlineModelScope,
): FunSpec {
    val builder = FunSpec.builder("invoke")
        .addModifiers(KModifier.SUSPEND, KModifier.OPERATOR)
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

    val returnType = invokeReturnType(config, methodClassName, inlineModelScope)
    builder.returns(returnType)
    builder.addExperimentalUuidOptInIfNeeded(signatureTypes + returnType)
    builder.addCode(buildOperationBody(method, methodClassName, includeBody = false))
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

private fun Route.Bodies.toInvokeParameterSpecs(
    config: RenderConfig,
    methodClassName: ClassName,
    usesNestedBodyType: Boolean,
): List<ParameterSpec>? = when (val body = defaultOrNull()) {
    null -> null
    is Route.Body.SetBody -> {
        val bodyType = if (usesNestedBodyType) {
            methodClassName.nestedClass("Body")
        } else {
            body.type.toTypeName(config)
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
            body.type.toTypeName(config)
        }
        val typeName = if (!required) bodyType.copy(nullable = true) else bodyType
        listOf(
            ParameterSpec.builder("body", typeName).apply {
                if (!required) defaultValue(CodeBlock.of("null"))
            }.build()
        )
    }

    is Route.Body.FormUrlEncoded -> {
        body.parameters.map { formData ->
            ParameterSpec.builder(formData.name.toParamName(), formData.type.toTypeName(config)).build()
        }
    }

    is Route.Body.Multipart.Value -> {
        body.parameters.map { formData ->
            ParameterSpec.builder(formData.name.toParamName(), formData.type.toTypeName(config)).build()
        }
    }

    is Route.Body.Multipart.Ref -> {
        val typeName = body.value.toTypeName(config).let {
            if (!required) it.copy(nullable = true) else it
        }
        listOf(
            ParameterSpec.builder("body", typeName).apply {
                if (!required) defaultValue(CodeBlock.of("null"))
            }.build()
        )
    }
}
