package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.transformers.nestedOrNull
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlin.jvm.JvmName

fun ApiTree.generateClient(config: RenderConfig): List<FileSpec> {
    if (children.isEmpty() && operations.isEmpty()) return emptyList()
    val needsSerializationUtils =
        hasInlineNonDiscriminatedParameterUnion() ||
            hasInlineNonDiscriminatedBodyUnion() ||
            hasInlineNonDiscriminatedResponseUnion()

    val rootName = name.toPascalCase()
    val rootClassName = ClassName(config.apiPackage, rootName)

    val childFiles = mutableListOf<FileSpec>()
    for (child in children) {
        val childSimpleName = child.childInterfaceSimpleName()
        val childClassName = ClassName(config.apiPackage, childSimpleName)
        val childTypeSpec = child.toTypeSpec(config, childClassName, emptyList())

        val fileBuilder = FileSpec.builder(config.apiPackage, childSimpleName)
            .addType(childTypeSpec)
        for (method in child.collectHttpMethods()) {
            fileBuilder.addImport("io.ktor.client.request", method)
        }
        childFiles.add(fileBuilder.build())
    }

    val rootFileBuilder = FileSpec.builder(config.apiPackage, rootName)
        .addType(toRootTypeSpec(config, rootClassName))

    generateServerType(config)?.let { rootFileBuilder.addType(it) }

    for (method in operations.keys.mapTo(mutableSetOf()) { it.value.lowercase() }) {
        rootFileBuilder.addImport("io.ktor.client.request", method)
    }

    val files = listOf(rootFileBuilder.build()) + childFiles
    val serializationUtils = if (needsSerializationUtils) {
        listOf(generateSerializationUtils(config.copy(modelPackage = config.apiPackage)))
    } else emptyList()

    return files + serializationUtils
}

private fun ApiTree.toRootTypeSpec(config: RenderConfig, className: ClassName): TypeSpec {
    val builder = TypeSpec.classBuilder(className.simpleName)
        .addSuperinterface(ClassName("kotlin", "AutoCloseable"))
    builder.addClientConstructorAndState(config, emptyList())
    for (ctor in generateSecondaryConstructors(config)) {
        builder.addFunction(ctor)
    }
    builder.addFunction(
        FunSpec.builder("close")
            .addModifiers(KModifier.OVERRIDE)
            .addStatement("client.close()")
            .build()
    )
    val orderedOperations = operations.entries.sortedBy { it.key.value }
    val sharedInlineParameterModels = orderedOperations
        .map { it.value }
        .sharedInlineParameterModels()
    val sharedInlineParameterKeys = sharedInlineParameterModels
        .map(InlineParameterModel::sharingKey)
        .toSet()

    // Inline parameter model types from operations
    sharedInlineParameterModels.forEach { inline ->
        inline.model
            .toInlineParameterTypeSpec(config, className, inline.simpleName)
            ?.let(builder::addType)
    }

    for ((method, route) in orderedOperations) {
        builder.addProperty(route.toOperationPropertySpec(method, className, emptyList()))
        builder.addType(
            route.toOperationTypeSpec(
                method = method,
                config = config,
                pathClassName = className,
                sharedInlineParameterKeys = sharedInlineParameterKeys,
                accumulatedParams = emptyList(),
            )
        )
    }

    for (child in children) {
        val childSimpleName = child.childInterfaceSimpleName()
        val childClassName = ClassName(config.apiPackage, childSimpleName)
        child.segment.addConcreteNavigationMember(
            builder = builder,
            childClassName = childClassName,
            currentAccumulatedParams = emptyList(),
            parentClassName = className,
            config = config,
        )
    }

    return builder.build()
}

private fun PathNode.toTypeSpec(
    config: RenderConfig,
    className: ClassName,
    parentAccumulatedParams: List<AccumulatedParam>,
): TypeSpec {
    val currentAccumulatedParams = accumulatedParams(parentAccumulatedParams)
    val builder = TypeSpec.classBuilder(className.simpleName)
    builder.addClientConstructorAndState(config, currentAccumulatedParams)
    val orderedOperations = operations.entries.sortedBy { it.key.value }
    val sharedInlineParameterModels = orderedOperations
        .map { it.value }
        .sharedInlineParameterModels()
    val sharedInlineParameterKeys = sharedInlineParameterModels
        .map(InlineParameterModel::sharingKey)
        .toSet()

    sharedInlineParameterModels.forEach { inline ->
        inline.model
            .toInlineParameterTypeSpec(config, className, inline.simpleName)
            ?.let(builder::addType)
    }

    for ((method, route) in orderedOperations) {
        builder.addProperty(route.toOperationPropertySpec(method, className, currentAccumulatedParams))
        builder.addType(
            route.toOperationTypeSpec(
                method = method,
                config = config,
                pathClassName = className,
                sharedInlineParameterKeys = sharedInlineParameterKeys,
                accumulatedParams = currentAccumulatedParams,
            )
        )
    }

    for (child in children) {
        val childSimpleName = child.childInterfaceSimpleName()
        val childClassName = className.nestedClass(childSimpleName)

        child.segment.addConcreteNavigationMember(
            builder = builder,
            childClassName = childClassName,
            currentAccumulatedParams = currentAccumulatedParams,
            parentClassName = className,
            config = config,
        )
        builder.addType(child.toTypeSpec(config, childClassName, currentAccumulatedParams))
    }

    return builder.build()
}

private fun ApiTree.hasInlineNonDiscriminatedParameterUnion(): Boolean =
    operations.values.hasInlineNonDiscriminatedParameterUnion() ||
        children.any(PathNode::hasInlineNonDiscriminatedParameterUnion)

private fun PathNode.hasInlineNonDiscriminatedParameterUnion(): Boolean =
    operations.values.hasInlineNonDiscriminatedParameterUnion() ||
        children.any(PathNode::hasInlineNonDiscriminatedParameterUnion)

private fun Iterable<Route>.hasInlineNonDiscriminatedParameterUnion(): Boolean =
    flatMap(Route::inlineParameterModels)
        .any { inline -> inline.model is Model.Union && inline.model.discriminator == null }

private fun ApiTree.hasInlineNonDiscriminatedBodyUnion(): Boolean =
    operations.values.hasInlineNonDiscriminatedBodyUnion() ||
        children.any(PathNode::hasInlineNonDiscriminatedBodyUnion)

private fun PathNode.hasInlineNonDiscriminatedBodyUnion(): Boolean =
    operations.values.hasInlineNonDiscriminatedBodyUnion() ||
        children.any(PathNode::hasInlineNonDiscriminatedBodyUnion)

private fun Iterable<Route>.hasInlineNonDiscriminatedBodyUnion(): Boolean =
    any { route ->
        route.body?.types.orEmpty().values.any(Route.Body::containsInlineNonDiscriminatedUnion)
    }

private fun ApiTree.hasInlineNonDiscriminatedResponseUnion(): Boolean =
    operations.values.hasInlineNonDiscriminatedResponseUnion() ||
        children.any(PathNode::hasInlineNonDiscriminatedResponseUnion)

private fun PathNode.hasInlineNonDiscriminatedResponseUnion(): Boolean =
    operations.values.hasInlineNonDiscriminatedResponseUnion() ||
        children.any(PathNode::hasInlineNonDiscriminatedResponseUnion)

private fun Iterable<Route>.hasInlineNonDiscriminatedResponseUnion(): Boolean =
    any { route ->
        route.returns.responses.values.any { returnType ->
            returnType.types.values.any(Model::containsInlineNonDiscriminatedUnion)
        } || route.returns.default?.types.orEmpty().values.any(Model::containsInlineNonDiscriminatedUnion)
    }

private fun Route.Body.containsInlineNonDiscriminatedUnion(): Boolean =
    when (this) {
        is Route.Body.SetBody -> type.containsInlineNonDiscriminatedUnion()
        is Route.Body.OverloadedBody -> cases.any { it.model.containsInlineNonDiscriminatedUnion() }
        is Route.Body.FormUrlEncoded -> parameters.any { it.type.containsInlineNonDiscriminatedUnion() }
        is Route.Body.Multipart.Value -> parameters.any { it.type.containsInlineNonDiscriminatedUnion() }
        is Route.Body.Multipart.Ref -> value.containsInlineNonDiscriminatedUnion()
    }

private fun Model.containsInlineNonDiscriminatedUnion(): Boolean =
    when (this) {
        is Model.Union ->
            (context.head is NamingContext.Path && discriminator == null) ||
                cases.any { it.model.containsInlineNonDiscriminatedUnion() }

        is Model.Object ->
            properties.values.any { it.model.containsInlineNonDiscriminatedUnion() } ||
                ((additionalProperties as? Model.Object.AdditionalProperties.Schema)
                    ?.value
                    ?.containsInlineNonDiscriminatedUnion() == true)

        is Model.Collection -> inner.containsInlineNonDiscriminatedUnion()
        is Model.DiscriminatedObject ->
            abstractProperties.values.any { it.model.containsInlineNonDiscriminatedUnion() } ||
                subtypes.any { subtype ->
                    subtype.properties.values.any { it.model.containsInlineNonDiscriminatedUnion() } ||
                        ((subtype.additionalProperties as? Model.Object.AdditionalProperties.Schema)
                            ?.value
                            ?.containsInlineNonDiscriminatedUnion() == true)
                }

        is Model.ByteArray,
        is Model.Date,
        is Model.DateTime,
        is Model.Enum,
        is Model.FreeFormJson,
        is Model.Primitive,
        is Model.Reference,
        is Model.Uuid -> false
    }

private fun Model.toInlineParameterTypeSpec(
    config: RenderConfig,
    ownerClassName: ClassName,
    nameOverride: String,
): TypeSpec? = when (this) {
    is Model.Enum -> toTypeSpec(config, nameOverride = nameOverride)
    is Model.Object -> toTypeSpec(config, classNameOverride = ownerClassName.nestedClass(nameOverride))
    is Model.Union -> toTypeSpec(config, classNameOverride = ownerClassName.nestedClass(nameOverride))
    is Model.DiscriminatedObject -> toTypeSpec(config, classNameOverride = ownerClassName.nestedClass(nameOverride))
    is Model.ByteArray,
    is Model.Collection,
    is Model.Date,
    is Model.DateTime,
    is Model.FreeFormJson,
    is Model.Reference,
    is Model.Uuid,
    is Model.Primitive -> null
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
private fun TypeSpec.Builder.addDeprecatedIfNeeded() = apply {
    if (route.deprecated) addAnnotation(deprecatedAnnotation())
}

context(route: Route)
private fun PropertySpec.Builder.addDeprecatedIfNeeded() = apply {
    if (route.deprecated) addAnnotation(deprecatedAnnotation())
}

private fun methodTypeName(method: HttpMethod): String =
    method.value.lowercase().replaceFirstChar { it.uppercase() }

private fun Route.toOperationPropertySpec(
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

private fun Route.toOperationTypeSpec(
    method: HttpMethod,
    config: RenderConfig,
    pathClassName: ClassName,
    sharedInlineParameterKeys: Set<String>,
    accumulatedParams: List<AccumulatedParam>,
): TypeSpec {
    val methodClassName = pathClassName.nestedClass(methodTypeName(method))
    val overloadedBody = body?.defaultOrNull() as? Route.Body.OverloadedBody
    val inlineModelScope = operationInlineModelScope(config, methodClassName)
    val inlineBodyTypeSpec = body?.inlineBodyTypeSpec(config, methodClassName)
    val routeInlineParameterModels = routeSpecificInlineParameterModels(sharedInlineParameterKeys)

    return TypeSpec.classBuilder(methodTypeName(method))
        .addDeprecatedIfNeeded()
        .apply {
            addClientConstructorAndState(config, accumulatedParams)
            routeInlineParameterModels.forEach { inline ->
                inline.model
                    .toInlineParameterTypeSpec(config, methodClassName, inline.simpleName)
                    ?.let(::addType)
            }
            overloadedBody
                ?.directInlineTypeSpecs(config, methodClassName, inlineModelScope)
                ?.forEach(::addType)
            inlineModelScope.methodTypeSpecs(config).forEach(::addType)
            inlineBodyTypeSpec?.let(::addType)
            if (!returns.isSingleUnitResponse() && !returns.isSingleDirectModelResponse()) {
                addType(buildResponseTypeSpec(config, methodClassName, inlineModelScope))
            }
            if (overloadedBody != null) {
                if (body?.required != true) {
                    addFunction(
                        toInvokeFunSpecForOptionalOverloadedBodyNoBody(
                            config = config,
                            pathClassName = pathClassName,
                            methodClassName = methodClassName,
                            sharedInlineParameterKeys = sharedInlineParameterKeys,
                            inlineModelScope = inlineModelScope,
                        )
                    )
                }
                overloadedBody
                    .distinctCaseSignatures(config, inlineModelScope)
                    .forEach { caseSignature ->
                        addFunction(
                            toInvokeFunSpecForOverloadedBodyCase(
                                config = config,
                                pathClassName = pathClassName,
                                methodClassName = methodClassName,
                                sharedInlineParameterKeys = sharedInlineParameterKeys,
                                bodyTypeName = caseSignature.typeName,
                                bodyJvmName = caseSignature.jvmName,
                                inlineModelScope = inlineModelScope,
                            )
                        )
                    }
            } else {
                addFunction(
                    toInvokeFunSpec(
                        config = config,
                        pathClassName = pathClassName,
                        methodClassName = methodClassName,
                        sharedInlineParameterKeys = sharedInlineParameterKeys,
                        usesNestedBodyType = inlineBodyTypeSpec != null,
                        inlineModelScope = inlineModelScope,
                    )
                )
            }
        }
        .build()
}

private fun operationConstructorArgs(accumulatedParams: List<AccumulatedParam>): String =
    buildList {
        add("client")
        addAll(accumulatedParams.map { it.name.toCamelCase() })
    }.joinToString(", ")

/** Build the operation invoke(...) signature with parameters and response wrapper. */
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
    builder.returns(returnType)
    builder.addExperimentalUuidOptInIfNeeded(*(signatureTypes + returnType).toTypedArray())
    builder.addCode(buildOperationBody(method, config, methodClassName, inlineModelScope))
    return builder.build()
}

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
    builder.addExperimentalUuidOptInIfNeeded(*(signatureTypes + returnType).toTypedArray())
    builder.addCode(buildOperationBody(method, config, methodClassName, inlineModelScope))
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
    builder.addExperimentalUuidOptInIfNeeded(*(signatureTypes + returnType).toTypedArray())
    builder.addCode(buildOperationBody(method, config, methodClassName, inlineModelScope, includeBody = false))
    return builder.build()
}

private fun Parameter.Input.sortOrder(): Int = when (this) {
    Parameter.Input.Query -> 0
    Parameter.Input.Header -> 1
    Parameter.Input.Cookie -> 2
    Parameter.Input.Path -> 3
}

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

    // For inline enums, compute default using the overridden type name (not NamingContext)
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
}

private val HttpStatusCodeType = ClassName("io.ktor.http", "HttpStatusCode")

/** Extract the preferred model from a ReturnType, preferring JSON content. */
private fun Route.ReturnType.preferredModel(): Model? {
    if (types.isEmpty()) return null
    val jsonEntry = types.entries.firstOrNull { ContentType.Application.Json.match(it.key) }
    return jsonEntry?.value ?: types.values.first()
}

/** Whether the returns need a sealed response wrapper (multiple statuses/default). */
internal fun Route.Returns.needsSealedInterface(): Boolean {
    val totalCases = responses.size + (if (default != null) 1 else 0)
    return totalCases > 1
}

/** Convert HttpStatusCode description to PascalCase for sealed response case names. */
private fun HttpStatusCode.toCaseName(): String =
    description.split(" ").joinToString("") { word ->
        word.lowercase().replaceFirstChar { it.uppercase() }
    }

private fun Route.buildResponseTypeSpec(
    config: RenderConfig,
    methodClassName: ClassName,
    inlineModelScope: OperationInlineModelScope,
): TypeSpec {
    if (returns.needsSealedInterface()) {
        return buildSealedResponseTypeSpec(config, methodClassName)
    }

    val model = returns.singlePreferredModelOrNull()
    if (model != null && model.isRouteInlineModel()) {
        model.toInlineOperationTypeSpecOrNull(
            config = config,
            ownerClassName = methodClassName,
            nameOverride = "Response",
            externalTypeNames = inlineModelScope.externalTypeNames(),
        )?.let { return it }
    }

    return if (model == null || model is Model.Primitive.Unit) {
        TypeSpec.objectBuilder("Response")
            .addModifiers(KModifier.DATA)
            .build()
    } else {
        val typeName = inlineModelScope.remap(model.toTypeName(config))
        TypeSpec.classBuilder("Response")
            .addModifiers(KModifier.DATA)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("value", typeName)
                    .build()
            )
            .addProperty(
                PropertySpec.builder("value", typeName)
                    .initializer("value")
                    .build()
            )
            .apply {
                inlineModelScope.responseTypeSpecs(config).forEach(::addType)
            }
            .build()
    }
}

private fun Route.invokeReturnType(
    config: RenderConfig,
    methodClassName: ClassName,
    inlineModelScope: OperationInlineModelScope,
): TypeName {
    if (returns.isSingleUnitResponse()) return UNIT
    if (returns.isSingleDirectModelResponse()) {
        // Single concrete external model — return it directly, no Response wrapper needed.
        val model = returns.singlePreferredModelOrNull()!!
        return inlineModelScope.remap(model.toTypeName(config))
    }
    return methodClassName.nestedClass("Response")
}

private fun Route.Returns.isSingleUnitResponse(): Boolean {
    if (needsSealedInterface()) return false
    val model = singlePreferredModelOrNull()
    return model == null || model is Model.Primitive.Unit
}

/** True when there is exactly one response with a non-unit, non-inline model (no wrapper needed). */
internal fun Route.Returns.isSingleDirectModelResponse(): Boolean {
    if (needsSealedInterface()) return false
    val model = singlePreferredModelOrNull()
    return model != null && model !is Model.Primitive.Unit && !model.isRouteInlineModel()
}

private fun Route.buildSealedResponseTypeSpec(
    config: RenderConfig,
    methodClassName: ClassName,
): TypeSpec {
    val responseClassName = methodClassName.nestedClass("Response")

    val builder = TypeSpec.interfaceBuilder("Response")
        .addModifiers(KModifier.SEALED)

    // Status code cases (ordered by status code value)
    for ((statusCode, returnType) in returns.responses.entries.sortedBy { it.key.value }) {
        val caseName = statusCode.toCaseName()
        val model = returnType.preferredModel()

        if (model == null || model is Model.Primitive.Unit) {
            builder.addType(
                TypeSpec.objectBuilder(caseName)
                    .addModifiers(KModifier.DATA)
                    .addSuperinterface(responseClassName)
                    .build()
            )
        } else {
            val typeName = model.toTypeName(config)
            builder.addType(
                TypeSpec.classBuilder(caseName)
                    .addModifiers(KModifier.DATA)
                    .primaryConstructor(
                        FunSpec.constructorBuilder()
                            .addParameter("value", typeName)
                            .build()
                    )
                    .addProperty(
                        PropertySpec.builder("value", typeName)
                            .initializer("value")
                            .build()
                    )
                    .addSuperinterface(responseClassName)
                    .build()
            )
        }
    }

    // Default case
    val defaultReturnType = returns.default
    if (defaultReturnType != null) {
        val model = defaultReturnType.preferredModel()
        val defaultBuilder = TypeSpec.classBuilder("Default")
            .addModifiers(KModifier.DATA)
            .addSuperinterface(responseClassName)

        val constructorBuilder = FunSpec.constructorBuilder()
            .addParameter("status", HttpStatusCodeType)

        val props = mutableListOf(
            PropertySpec.builder("status", HttpStatusCodeType)
                .initializer("status")
                .build()
        )

        if (model != null && model !is Model.Primitive.Unit) {
            val typeName = model.toTypeName(config)
            constructorBuilder.addParameter("value", typeName)
            props.add(
                PropertySpec.builder("value", typeName)
                    .initializer("value")
                    .build()
            )
        }

        defaultBuilder.primaryConstructor(constructorBuilder.build())
        props.forEach { defaultBuilder.addProperty(it) }
        builder.addType(defaultBuilder.build())
    }

    return builder.build()
}

internal fun Route.Returns.singlePreferredModelOrNull(): Model? =
    (responses.values.firstOrNull() ?: default)?.preferredModel()

internal fun Model.isRouteInlineModel(): Boolean =
    this is Model.ContextHolder && context.head is NamingContext.Path

private fun Model.toInlineOperationTypeSpecOrNull(
    config: RenderConfig,
    ownerClassName: ClassName,
    nameOverride: String,
    externalTypeNames: Map<ClassName, TypeName> = emptyMap(),
): TypeSpec? =
    when (this) {
        is Model.Object -> toTypeSpec(
            config,
            classNameOverride = ownerClassName.nestedClass(nameOverride),
            externalTypeNames = externalTypeNames,
        )
        is Model.Enum -> toTypeSpec(config, nameOverride = nameOverride)
        is Model.Union -> toTypeSpec(
            config,
            classNameOverride = ownerClassName.nestedClass(nameOverride),
            externalTypeNames = externalTypeNames,
        )

        is Model.DiscriminatedObject, // This is only supported as top-level components schema
        is Model.ByteArray,
        is Model.Collection,
        is Model.Date,
        is Model.DateTime,
        is Model.FreeFormJson,
        is Model.Uuid,
        is Model.Reference,
        is Model.Primitive -> null
    }

private fun Route.Bodies.inlineBodyTypeSpec(config: RenderConfig, ownerClassName: ClassName): TypeSpec? {
    if (defaultOrNull() is Route.Body.OverloadedBody) return null
    val model = defaultOrNull().bodyModelOrNull() ?: return null
    if (!model.isRouteInlineModel()) return null
    return model.toInlineOperationTypeSpecOrNull(config, ownerClassName, "Body")
}

private fun Route.Body.OverloadedBody.directInlineTypeSpecs(
    config: RenderConfig,
    ownerClassName: ClassName,
    inlineModelScope: OperationInlineModelScope,
): List<TypeSpec> {
    val emitted = mutableSetOf<String>()
    return cases.mapNotNull { case ->
        val directTypeName = (case.model as? Model.ContextHolder)?.context?.toClassName(config)?.simpleName
            ?: return@mapNotNull null
        case.model.toInlineOperationTypeSpecOrNull(
            config = config,
            ownerClassName = ownerClassName,
            nameOverride = directTypeName,
            externalTypeNames = inlineModelScope.externalTypeNames(),
        )
    }.filter { spec ->
        emitted.add(spec.name ?: return@filter false)
    }
}

private data class OverloadedBodyCaseSignature(
    val typeName: TypeName,
    val jvmName: String?,
)

private val KotlinListType = ClassName("kotlin.collections", "List")

private fun Route.Body.OverloadedBody.distinctCaseSignatures(
    config: RenderConfig,
    inlineModelScope: OperationInlineModelScope,
): List<OverloadedBodyCaseSignature> {
    val emittedTypes = mutableSetOf<TypeName>()
    val typeNames = cases.mapNotNull { case ->
        inlineModelScope
            .remap(case.model.toTypeName(config))
            .takeIf { emittedTypes.add(it) }
    }
    val conflictingListTypes = typeNames
        .filterIsInstance<ParameterizedTypeName>()
        .filter { it.rawType == KotlinListType }
        .takeIf { it.size > 1 }
        .orEmpty()
        .toSet()
    val emittedJvmNames = mutableSetOf<String>()
    return typeNames.map { typeName ->
        val jvmName = (typeName as? ParameterizedTypeName)
            ?.takeIf { it in conflictingListTypes }
            ?.listJvmName()
            ?.uniqueJvmName(emittedJvmNames)
        OverloadedBodyCaseSignature(typeName, jvmName)
    }
}

private fun ParameterizedTypeName.listJvmName(): String =
    "${typeArguments.single().jvmSimpleName()}List"

private fun TypeName.jvmSimpleName(): String =
    when (val type = copy(nullable = false)) {
        is ClassName -> type.simpleName
        is ParameterizedTypeName ->
            when {
                type.rawType == KotlinListType && type.typeArguments.size == 1 -> type.listJvmName()
                else -> type.rawType.simpleName
            }

        else -> type.toString()
            .substringAfterLast('.')
            .substringBefore('<')
            .replace("?", "")
            .toPascalCase()
    }

private fun String.uniqueJvmName(emitted: MutableSet<String>): String {
    if (emitted.add(this)) return this
    var index = 2
    while (true) {
        val candidate = "$this$index"
        if (emitted.add(candidate)) return candidate
        index++
    }
}

private fun Route.Body?.bodyModelOrNull(): Model? = when (this) {
    is Route.Body.SetBody -> type
    is Route.Body.OverloadedBody -> type
    is Route.Body.FormUrlEncoded,
    is Route.Body.Multipart,
    null -> null
}
