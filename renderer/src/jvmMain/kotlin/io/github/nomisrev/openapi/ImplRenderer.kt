package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.routes.Route
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode

// Type references for generated code
private val HttpClientType = ClassName("io.ktor.client", "HttpClient")
private val ContentTypeType = ClassName("io.ktor.http", "ContentType")
private val MultiPartFormDataContentType =
    ClassName("io.ktor.client.request.forms", "MultiPartFormDataContent")
private val ParametersType = ClassName("io.ktor.http", "Parameters")
private val ResponseExceptionType = ClassName("io.ktor.client.plugins", "ResponseException")

// Member references for extension function imports
private val BodyMember = MemberName("io.ktor.client.call", "body")
private val ParameterMember = MemberName("io.ktor.client.request", "parameter")
private val HeaderMember = MemberName("io.ktor.client.request", "header")
private val CookieMember = MemberName("io.ktor.client.request", "cookie")
private val SetBodyMember = MemberName("io.ktor.client.request", "setBody")
private val ContentTypeFunMember = MemberName("io.ktor.http", "contentType")
private val FormDataMember = MemberName("io.ktor.client.request.forms", "formData")
private val FormUrlEncodeMember = MemberName("io.ktor.http", "formUrlEncode")
private val AppendMember = MemberName("io.ktor.client.request.forms", "append")

/** Accumulated path parameter from tree traversal. */
private data class AccumulatedParam(
    val name: String,
    val type: Model,
    val storeAsString: Boolean = false,
)

/** Collect all HTTP method names used in operations for this node and its descendants. */
internal fun PathNode.collectHttpMethods(): Set<String> {
    val methods = operations.keys.mapTo(mutableSetOf()) { it.value.lowercase() }
    for (child in children) {
        methods.addAll(child.collectHttpMethods())
    }
    return methods
}

/** Collect HTTP method names used in root operations. */
internal fun ApiTree.collectHttpMethods(): Set<String> =
    operations.keys.mapTo(mutableSetOf()) { it.value.lowercase() }

/** Generate the root Ktor implementation class. */
fun ApiTree.generateRootImpl(config: RenderConfig): TypeSpec? {
    if (children.isEmpty() && operations.isEmpty()) return null
    val rootName = name.toPascalCase()
    val rootClassName = ClassName(config.apiPackage, rootName)
    val implName = "Ktor$rootName"

    val builder = TypeSpec.classBuilder(implName)
        .addModifiers(KModifier.INTERNAL)
        .addSuperinterface(rootClassName)

    // Constructor: just client
    builder.primaryConstructor(
        FunSpec.constructorBuilder()
            .addParameter("client", HttpClientType)
            .build()
    )
    builder.addProperty(
        PropertySpec.builder("client", HttpClientType)
            .addModifiers(KModifier.PRIVATE)
            .initializer("client")
            .build()
    )

    // Child navigation overrides
    for (child in children) {
        val childSimpleName = child.childInterfaceSimpleName()
        val childClassName = ClassName(config.apiPackage, childSimpleName)
        child.segment.addNavigationOverride(
            builder,
            childClassName,
            "Ktor$childSimpleName",
            emptyList(),
            rootClassName,
            config,
        )
    }

    // Operation overrides
    val orderedOperations = operations.entries.sortedBy { it.key.value }
    val sharedInlineParameterKeys = orderedOperations
        .map { it.value }
        .sharedInlineParameterModels()
        .map(InlineParameterModel::sharingKey)
        .toSet()
    for ((method, route) in orderedOperations) {
        builder.addProperty(
            route.toImplOperationPropertySpec(
                method = method,
                config = config,
                interfaceClassName = rootClassName,
                sharedInlineParameterKeys = sharedInlineParameterKeys,
            )
        )
    }

    return builder.build()
}

/**
 * Generate all Ktor implementation classes for a child node and its descendants.
 * Returns a flat list of TypeSpecs (not nested).
 */
fun PathNode.generateImplClasses(
    config: RenderConfig,
    interfaceClassName: ClassName,
): List<TypeSpec> = generateImplClassesInternal(config, interfaceClassName, emptyList())

private fun PathNode.generateImplClassesInternal(
    config: RenderConfig,
    interfaceClassName: ClassName,
    parentAccumulatedParams: List<AccumulatedParam>,
): List<TypeSpec> {
    // Compute accumulated params for this node
    val seg = segment
    val currentParams = when (seg) {
        is PathSegment.Parameter -> {
            val unionType = seg.model as? Model.Union
            unionType?.requireSupportedFlattenablePathUnion(seg.name)
            parentAccumulatedParams + AccumulatedParam(
                name = seg.name,
                type = seg.model,
                storeAsString = unionType?.isFlattenablePathUnion() == true,
            )
        }
        is PathSegment.Literal -> parentAccumulatedParams
    }

    val implName = "Ktor" + interfaceClassName.simpleNames.joinToString("")

    val builder = TypeSpec.classBuilder(implName)
        .addModifiers(KModifier.INTERNAL)
        .addSuperinterface(interfaceClassName)

    // Constructor: client + accumulated params
    val ctorBuilder = FunSpec.constructorBuilder()
        .addParameter("client", HttpClientType)
    for (param in currentParams) {
        val typeName = if (param.storeAsString) STRING else param.type.toTypeName(config)
        ctorBuilder.addParameter(param.name.toCamelCase(), typeName)
    }
    builder.primaryConstructor(ctorBuilder.build())

    // Properties: client + accumulated params (all private)
    builder.addProperty(
        PropertySpec.builder("client", HttpClientType)
            .addModifiers(KModifier.PRIVATE)
            .initializer("client")
            .build()
    )
    for (param in currentParams) {
        val paramName = param.name.toCamelCase()
        val typeName = if (param.storeAsString) STRING else param.type.toTypeName(config)
        builder.addProperty(
            PropertySpec.builder(paramName, typeName)
                .addModifiers(KModifier.PRIVATE)
                .initializer(paramName)
                .build()
        )
    }

    // Child navigation overrides
    for (child in children) {
        val childSimpleName = child.childInterfaceSimpleName()
        val childInterfaceClassName = interfaceClassName.nestedClass(childSimpleName)
        val childImplName = "Ktor" + childInterfaceClassName.simpleNames.joinToString("")
        child.segment.addNavigationOverride(
            builder, childInterfaceClassName, childImplName, currentParams, interfaceClassName, config
        )
    }

    // Operation overrides
    val orderedOperations = operations.entries.sortedBy { it.key.value }
    val sharedInlineParameterKeys = orderedOperations
        .map { it.value }
        .sharedInlineParameterModels()
        .map(InlineParameterModel::sharingKey)
        .toSet()
    for ((method, route) in orderedOperations) {
        builder.addProperty(
            route.toImplOperationPropertySpec(
                method = method,
                config = config,
                interfaceClassName = interfaceClassName,
                sharedInlineParameterKeys = sharedInlineParameterKeys,
            )
        )
    }

    // Collect this class + recursively all descendants
    val result = mutableListOf(builder.build())
    for (child in children) {
        val childSimpleName = child.childInterfaceSimpleName()
        val childInterfaceClassName = interfaceClassName.nestedClass(childSimpleName)
        result.addAll(child.generateImplClassesInternal(config, childInterfaceClassName, currentParams))
    }
    return result
}

/**
 * Add a navigation override (property for literal, function for parameter).
 * Creates the child impl with client + accumulated params (+ new param for parameter segments).
 */
private fun PathSegment.addNavigationOverride(
    builder: TypeSpec.Builder,
    childInterfaceClassName: ClassName,
    childImplName: String,
    currentAccumulatedParams: List<AccumulatedParam>,
    parentInterfaceClassName: ClassName,
    config: RenderConfig,
) {
    val paramArgs = currentAccumulatedParams.joinToString(", ") { it.name.toCamelCase() }
    val ctorArgs = if (paramArgs.isEmpty()) "client" else "client, $paramArgs"

    when (this) {
        is PathSegment.Literal -> {
            builder.addProperty(
                PropertySpec.builder(name.toCamelCase(), childInterfaceClassName)
                    .addModifiers(KModifier.OVERRIDE)
                    .initializer("$childImplName($ctorArgs)")
                    .build()
            )
        }

        is PathSegment.Parameter -> {
            val paramName = name.toCamelCase()
            val flattenableUnion = (model as? Model.Union)
                ?.takeIf { it.isFlattenablePathUnion() }
                ?.also { it.requireSupportedFlattenablePathUnion(name) }
            if (flattenableUnion != null) {
                val enumClassName = parentInterfaceClassName.nestedClass(name.toPascalCase())
                val emittedTypes = mutableSetOf<TypeName>()
                for (case in flattenableUnion.cases) {
                    val caseTypeName = when (case.model) {
                        is Model.Enum -> enumClassName
                        else -> case.model.toTypeName(config)
                    }
                    if (!emittedTypes.add(caseTypeName)) continue

                    val functionBuilder = FunSpec.builder(paramName)
                        .addModifiers(KModifier.OVERRIDE)
                        .addParameter(paramName, caseTypeName)
                        .returns(childInterfaceClassName)
                    val encodedArg = when (val caseModel = case.model) {
                        is Model.Enum -> {
                            functionBuilder.addStatement(
                                "val encoded = %L",
                                caseModel.toPathParamValueExpression(paramName, enumClassName),
                            )
                            "encoded"
                        }
                        else -> if (caseTypeName == STRING) paramName else "$paramName.toString()"
                    }
                    val allArgs = if (paramArgs.isEmpty()) {
                        "client, $encodedArg"
                    } else {
                        "client, $paramArgs, $encodedArg"
                    }
                    functionBuilder.addStatement("return $childImplName($allArgs)")
                    builder.addFunction(functionBuilder.build())
                }
            } else {
                val allArgs = if (paramArgs.isEmpty()) "client, $paramName" else "client, $paramArgs, $paramName"
                builder.addFunction(
                    FunSpec.builder(paramName)
                        .addModifiers(KModifier.OVERRIDE)
                        .addParameter(paramName, model.toTypeName(config))
                        .returns(childInterfaceClassName)
                        .addStatement("return $childImplName($allArgs)")
                        .build()
                )
            }
        }
    }
}

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

private fun deprecatedAnnotation(): AnnotationSpec =
    AnnotationSpec.builder(Deprecated::class)
        .addMember("%S", "Deprecated by the API provider")
        .build()

context(route: Route)
private fun FunSpec.Builder.addDeprecatedIfNeeded() = apply {
    if (route.deprecated) addAnnotation(deprecatedAnnotation())
}

context(route: Route)
private fun PropertySpec.Builder.addDeprecatedIfNeeded() = apply {
    if (route.deprecated) addAnnotation(deprecatedAnnotation())
}

private fun methodTypeName(method: HttpMethod): String =
    method.value.lowercase().replaceFirstChar { it.uppercase() }

private fun Route.toImplOperationPropertySpec(
    method: HttpMethod,
    config: RenderConfig,
    interfaceClassName: ClassName,
    sharedInlineParameterKeys: Set<String>,
): PropertySpec {
    val methodName = method.value.lowercase()
    val methodClassName = interfaceClassName.nestedClass(methodTypeName(method))
    val usesNestedBodyType = body?.usesNestedBodyType() == true

    val anonymousImpl = TypeSpec.anonymousClassBuilder()
        .addSuperinterface(methodClassName)
        .addFunction(
            toImplInvokeFunSpec(
                method = method,
                config = config,
                interfaceClassName = interfaceClassName,
                methodClassName = methodClassName,
                sharedInlineParameterKeys = sharedInlineParameterKeys,
                usesNestedBodyType = usesNestedBodyType,
            )
        )
        .build()

    return PropertySpec.builder(methodName, methodClassName)
        .addModifiers(KModifier.OVERRIDE)
        .addDeprecatedIfNeeded()
        .initializer("%L", anonymousImpl)
        .build()
}

/** Build the implementation of operation invoke(...). */
private fun Route.toImplInvokeFunSpec(
    method: HttpMethod,
    config: RenderConfig,
    interfaceClassName: ClassName,
    methodClassName: ClassName,
    sharedInlineParameterKeys: Set<String>,
    usesNestedBodyType: Boolean,
): FunSpec {
    val builder = FunSpec.builder("invoke")
        .addModifiers(KModifier.OVERRIDE, KModifier.SUSPEND, KModifier.OPERATOR)
        .addDeprecatedIfNeeded()

    // Add same parameters as the interface invoke(...)
    val nonPathParams = parameters.filter { it.input != Parameter.Input.Path }
    val requiredParams = nonPathParams.filter { it.isRequired }.sortedBy { it.input.sortOrder() }
    val optionalParams = nonPathParams.filter { !it.isRequired }.sortedBy { it.input.sortOrder() }
    val bodyParams = body?.toImplInvokeParameterSpecs(config, methodClassName, usesNestedBodyType)
    val bodyRequired = body?.required == true

    // 1. Required params
    for (input in requiredParams) {
        builder.addParameter(
            input.toImplParameterSpec(
                config = config,
                interfaceClassName = interfaceClassName,
                methodClassName = methodClassName,
                sharedInlineParameterKeys = sharedInlineParameterKeys,
            )
        )
    }
    // 2. Required body
    if (bodyRequired && bodyParams != null) {
        bodyParams.forEach { builder.addParameter(it) }
    }
    // 3. Optional params
    for (input in optionalParams) {
        builder.addParameter(
            input.toImplParameterSpec(
                config = config,
                interfaceClassName = interfaceClassName,
                methodClassName = methodClassName,
                sharedInlineParameterKeys = sharedInlineParameterKeys,
            )
        )
    }
    // 4. Optional body
    if (!bodyRequired && bodyParams != null) {
        bodyParams.forEach { builder.addParameter(it) }
    }

    builder.returns(invokeReturnType(methodClassName))

    // Build the function body
    builder.addCode(buildOperationBody(method, config, methodClassName))

    return builder.build()
}

/** Build the CodeBlock for an invoke(...) body. */
private fun Route.buildOperationBody(
    method: HttpMethod,
    config: RenderConfig,
    methodClassName: ClassName,
): CodeBlock {
    val code = CodeBlock.builder()
    val httpMethodName = method.value.lowercase()
    val pathLiteral = segments.toPathLiteral()

    val nonPathParams = parameters.filter { it.input != Parameter.Input.Path }
    val hasRequestConfig = nonPathParams.isNotEmpty() || body != null
    val responseClassName = methodClassName.nestedClass("Response")

    if (returns.needsSealedInterface()) {
        // Multi-response wrapper: decode status-specific cases.
        if (hasRequestConfig) {
            code.beginControlFlow("val response = client.%L(%L)", httpMethodName, pathLiteral)
            addRequestConfigCode(code, config)
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

        // Default or throw
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
                addRequestConfigCode(code, config)
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
                    addRequestConfigCode(code, config)
                    code.unindent()
                    code.add("}.%M()\n", BodyMember)
                } else {
                    code.addStatement("return client.%L(%L).%M()", httpMethodName, pathLiteral, BodyMember)
                }
            } else {
                val modelType = model.toTypeName(config)
                if (hasRequestConfig) {
                    code.add("val value: %T = client.%L(%L) {\n", modelType, httpMethodName, pathLiteral)
                    code.indent()
                    addRequestConfigCode(code, config)
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

private fun Route.invokeReturnType(methodClassName: ClassName) =
    if (returns.isSingleUnitResponse()) UNIT else methodClassName.nestedClass("Response")

private fun Route.Returns.isSingleUnitResponse(): Boolean {
    if (needsSealedInterface()) return false
    val model = singlePreferredModelOrNull()
    return model == null || model is Model.Primitive.Unit
}

/** Add request configuration code (parameters, headers, cookies, body). */
private fun Route.addRequestConfigCode(code: CodeBlock.Builder, config: RenderConfig) {
    val nonPathParams = parameters.filter { it.input != Parameter.Input.Path }

    // Query parameters
    for (param in nonPathParams.filter { it.input == Parameter.Input.Query }) {
        val paramName = param.name.toParamName()
        if (param.isRequired) {
            code.addStatement("%M(%S, %L)", ParameterMember, param.name, paramName)
        } else {
            code.addStatement("%L?.let·{ %M(%S, it) }", paramName, ParameterMember, param.name)
        }
    }

    // Header parameters
    for (param in nonPathParams.filter { it.input == Parameter.Input.Header }) {
        val paramName = param.name.toParamName()
        if (param.isRequired) {
            code.addStatement("%M(%S, %L)", HeaderMember, param.name, paramName)
        } else {
            code.addStatement("%L?.let·{ %M(%S, it) }", paramName, HeaderMember, param.name)
        }
    }

    // Cookie parameters
    for (param in nonPathParams.filter { it.input == Parameter.Input.Cookie }) {
        val paramName = param.name.toParamName()
        if (param.isRequired) {
            code.addStatement("%M(%S, %L)", CookieMember, param.name, paramName)
        } else {
            code.addStatement("%L?.let·{ %M(%S, it) }", paramName, CookieMember, param.name)
        }
    }

    // Request body
    if (body != null) {
        addBodyCode(code, body!!, config)
    }
}

/** Add request body code. */
private fun addBodyCode(code: CodeBlock.Builder, bodies: Route.Bodies, config: RenderConfig) {
    val body = bodies.defaultOrNull() ?: return
    when (body) {
        is Route.Body.SetBody -> {
            if (bodies.required) {
                code.addStatement("%M(%T.Application.Json)", ContentTypeFunMember, ContentTypeType)
                code.addStatement("%M(body)", SetBodyMember)
            } else {
                code.beginControlFlow("body?.let")
                code.addStatement("%M(%T.Application.Json)", ContentTypeFunMember, ContentTypeType)
                code.addStatement("%M(it)", SetBodyMember)
                code.endControlFlow()
            }
        }

        is Route.Body.OverloadedBody -> {
            if (bodies.required) {
                code.addStatement("%M(%T.Application.Json)", ContentTypeFunMember, ContentTypeType)
                code.addStatement("%M(body)", SetBodyMember)
            } else {
                code.beginControlFlow("body?.let")
                code.addStatement("%M(%T.Application.Json)", ContentTypeFunMember, ContentTypeType)
                code.addStatement("%M(it)", SetBodyMember)
                code.endControlFlow()
            }
        }

        is Route.Body.FormUrlEncoded -> {
            code.add("%M(%T.build {\n", SetBodyMember, ParametersType)
            code.indent()
            for (formData in body.parameters) {
                code.addStatement("append(%S, %L)", formData.name, formData.name.toParamName())
            }
            code.unindent()
            code.add("}.%M())\n", FormUrlEncodeMember)
        }

        is Route.Body.Multipart.Value -> {
            code.add("%M(%T(%M {\n", SetBodyMember, MultiPartFormDataContentType, FormDataMember)
            code.indent()
            for (formData in body.parameters) {
                code.addStatement("%M(%S, %L)", AppendMember, formData.name, formData.name.toParamName())
            }
            code.unindent()
            code.add("}))\n")
        }

        is Route.Body.Multipart.Ref -> {
            if (bodies.required) {
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

/** Build the path string literal for use in generated code. */
private fun List<PathSegment>.toPathLiteral(): String {
    val inner = joinToString("/") { segment ->
        when (segment) {
            is PathSegment.Literal -> segment.name
            is PathSegment.Parameter -> "\$${segment.name.toCamelCase()}"
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

private fun Parameter.Input.sortOrder(): Int = when (this) {
    Parameter.Input.Query -> 0
    Parameter.Input.Header -> 1
    Parameter.Input.Cookie -> 2
    Parameter.Input.Path -> 3
}

/** Build parameter spec for impl invoke(...) — no default values (Kotlin disallows defaults on override). */
private fun Route.Input.toImplParameterSpec(
    config: RenderConfig,
    interfaceClassName: ClassName,
    methodClassName: ClassName,
    sharedInlineParameterKeys: Set<String>,
): ParameterSpec {
    val paramName = name.toParamName()
    val model = type
    val baseTypeName = inlineParameterTypeName(
        config = config,
        pathClassName = interfaceClassName,
        methodClassName = methodClassName,
        sharedInlineParameterKeys = sharedInlineParameterKeys,
    ) ?: model.toTypeName(config)
    val typeName = if (!isRequired) baseTypeName.copy(nullable = true) else baseTypeName
    return ParameterSpec.builder(paramName, typeName).build()
}

private fun Route.Bodies.toImplInvokeParameterSpecs(
    config: RenderConfig,
    methodClassName: ClassName,
    usesNestedBodyType: Boolean,
): List<ParameterSpec>? {
    val body = defaultOrNull() ?: return null
    return when (body) {
        is Route.Body.SetBody -> {
            val bodyType = if (usesNestedBodyType) {
                methodClassName.nestedClass("Body")
            } else {
                body.type.toTypeName(config)
            }
            val typeName = if (!required) bodyType.copy(nullable = true) else bodyType
            listOf(ParameterSpec.builder("body", typeName).build())
        }

        is Route.Body.OverloadedBody -> {
            val bodyType = if (usesNestedBodyType) {
                methodClassName.nestedClass("Body")
            } else {
                body.type.toTypeName(config)
            }
            val typeName = if (!required) bodyType.copy(nullable = true) else bodyType
            listOf(ParameterSpec.builder("body", typeName).build())
        }

        is Route.Body.FormUrlEncoded -> {
            body.parameters.map { formData ->
                ParameterSpec.builder(
                    formData.name.toParamName(), formData.type.toTypeName(config),
                ).build()
            }
        }

        is Route.Body.Multipart.Value -> {
            body.parameters.map { formData ->
                ParameterSpec.builder(
                    formData.name.toParamName(), formData.type.toTypeName(config),
                ).build()
            }
        }

        is Route.Body.Multipart.Ref -> {
            val typeName = body.value.toTypeName(config).let {
                if (!required) it.copy(nullable = true) else it
            }
            listOf(ParameterSpec.builder("body", typeName).build())
        }
    }
}

private fun Model.isRouteInlineModel(): Boolean =
    this is Model.ContextHolder && context.head is NamingContext.Path

private fun Route.Bodies.usesNestedBodyType(): Boolean {
    val model = defaultOrNull().bodyModelOrNull() ?: return false
    return model.isRouteInlineModel() &&
        (model is Model.Object || model is Model.Enum || model is Model.Union)
}

private fun Route.Body?.bodyModelOrNull(): Model? = when (this) {
    is Route.Body.SetBody -> type
    is Route.Body.OverloadedBody -> type
    is Route.Body.FormUrlEncoded,
    is Route.Body.Multipart,
    null -> null
}
