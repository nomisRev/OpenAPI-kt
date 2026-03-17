package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.transformers.nestedOrNull
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode

fun ApiTree.generateClient(config: RenderConfig): List<FileSpec> {
    if (children.isEmpty() && operations.isEmpty()) return emptyList()
    val needsSerializationUtils = hasInlineNonDiscriminatedParameterUnion()

    val rootName = name.toPascalCase()
    val rootClassName = ClassName(config.apiPackage, rootName)
    val rootInterface = TypeSpec.interfaceBuilder(rootName)

    // Root-level inline parameter model types
    operations.values.inlineParameterModels().forEach { (name, model) ->
        model.toInlineParameterTypeSpec(config, rootClassName, name)?.let(rootInterface::addType)
    }

    // Root-level operation nodes
    for ((method, route) in operations.entries.sortedBy { it.key.value }) {
        rootInterface.addType(route.toOperationTypeSpec(method, config, rootClassName))
        rootInterface.addProperty(route.toOperationPropertySpec(method, rootClassName))
    }

    // Direct children: separate files, top-level interfaces + impl classes
    val childFiles = mutableListOf<FileSpec>()
    for (child in children) {
        val childSimpleName = child.segment.name.toPascalCase()
        val childClassName = ClassName(config.apiPackage, childSimpleName)

        child.segment.addNavigationMember(rootInterface, childClassName, config)

        val childTypeSpec = child.toTypeSpec(config, childClassName)
        val childImplClasses = child.generateImplClasses(config, childClassName)

        val fileBuilder = FileSpec.builder(config.apiPackage, childSimpleName)
            .addType(childTypeSpec)
        for (implClass in childImplClasses) {
            fileBuilder.addType(implClass)
        }
        // Add HTTP method imports for generated impl code
        for (method in child.collectHttpMethods()) {
            fileBuilder.addImport("io.ktor.client.request", method)
        }
        childFiles.add(fileBuilder.build())
    }

    // Root file: interface + server sealed interface + factory + root impl
    val rootFileBuilder = FileSpec.builder(config.apiPackage, rootName)
        .addType(rootInterface.build())

    // Server sealed interface (if any)
    generateServerInterface(config)?.let { rootFileBuilder.addType(it) }

    // Factory function(s)
    for (factory in generateFactory(config)) {
        rootFileBuilder.addFunction(factory)
    }

    // Root implementation class
    generateRootImpl(config)?.let { rootFileBuilder.addType(it) }

    // Add HTTP method imports for root operations
    for (method in collectHttpMethods()) {
        rootFileBuilder.addImport("io.ktor.client.request", method)
    }

    val files = listOf(rootFileBuilder.build()) + childFiles
    val serializationUtils = if (needsSerializationUtils) {
        listOf(generateSerializationUtils(config.copy(modelPackage = config.apiPackage)))
    } else emptyList()
    return files + serializationUtils
}

private fun PathNode.toTypeSpec(config: RenderConfig, className: ClassName): TypeSpec {
    val builder = TypeSpec.interfaceBuilder(className.simpleName)

    // Inline parameter model types from operations
    operations.values.inlineParameterModels().forEach { (name, model) ->
        model.toInlineParameterTypeSpec(config, className, name)?.let(builder::addType)
    }

    // Operation nodes
    for ((method, route) in operations.entries.sortedBy { it.key.value }) {
        builder.addType(route.toOperationTypeSpec(method, config, className))
        builder.addProperty(route.toOperationPropertySpec(method, className))
    }

    // Children
    for (child in children) {
        val childSimpleName = child.segment.name.toPascalCase()
        val childClassName = className.nestedClass(childSimpleName)

        child.segment.addNavigationMember(builder, childClassName, config)
        builder.addType(child.toTypeSpec(config, childClassName))
    }

    return builder.build()
}

private fun PathSegment.addNavigationMember(
    builder: TypeSpec.Builder,
    childClassName: ClassName,
    config: RenderConfig,
) {
    when (this) {
        is PathSegment.Literal -> {
            builder.addProperty(
                PropertySpec.builder(name.toCamelCase(), childClassName)
                    .build()
            )
        }

        is PathSegment.Parameter -> {
            val paramName = name.toCamelCase()
            builder.addFunction(
                FunSpec.builder(paramName)
                    .addModifiers(KModifier.ABSTRACT)
                    .addParameter(paramName, type.toTypeName(config))
                    .returns(childClassName)
                    .build()
            )
        }
    }
}

/** Collect inline parameter model types from non-path parameters, paired with their simple name (PascalCase of param name). */
private fun Route.inlineParameterModels(): List<Pair<String, Model>> =
    parameters
        .filter { it.input != Parameter.Input.Path }
        .mapNotNull { input ->
            input.type.nestedOrNull()?.let { input.name.toPascalCase() to it }
        }

private fun Iterable<Route>.inlineParameterModels(): List<Pair<String, Model>> {
    val byName = linkedMapOf<String, Model>()
    for ((name, model) in flatMap(Route::inlineParameterModels)) {
        byName.putIfAbsent(name, model)
    }
    return byName.entries.map { it.key to it.value }
}

private fun ApiTree.hasInlineNonDiscriminatedParameterUnion(): Boolean =
    operations.values.hasInlineNonDiscriminatedParameterUnion() ||
        children.any(PathNode::hasInlineNonDiscriminatedParameterUnion)

private fun PathNode.hasInlineNonDiscriminatedParameterUnion(): Boolean =
    operations.values.hasInlineNonDiscriminatedParameterUnion() ||
        children.any(PathNode::hasInlineNonDiscriminatedParameterUnion)

private fun Iterable<Route>.hasInlineNonDiscriminatedParameterUnion(): Boolean =
    flatMap(Route::inlineParameterModels)
        .any { (_, model) -> model is Model.Union && model.discriminator == null }

private fun Model.toInlineParameterTypeSpec(
    config: RenderConfig,
    ownerClassName: ClassName,
    nameOverride: String,
): TypeSpec? = when (this) {
    is Model.Enum -> toTypeSpec(config, nameOverride = nameOverride)
    is Model.Object -> toTypeSpec(config, nameOverride = nameOverride)
    is Model.Union -> toTypeSpec(config, classNameOverride = ownerClassName.nestedClass(nameOverride))
    is Model.DiscriminatedObject -> toTypeSpec(config, classNameOverride = ownerClassName.nestedClass(nameOverride))
    else -> null
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
    interfaceClassName: ClassName,
): PropertySpec {
    val methodName = method.value.lowercase()
    return PropertySpec.builder(methodName, interfaceClassName.nestedClass(methodTypeName(method)))
        .addDeprecatedIfNeeded()
        .build()
}

private fun Route.toOperationTypeSpec(
    method: HttpMethod,
    config: RenderConfig,
    interfaceClassName: ClassName,
): TypeSpec {
    val methodClassName = interfaceClassName.nestedClass(methodTypeName(method))
    val inlineBodyTypeSpec = body?.inlineBodyTypeSpec(config)

    return TypeSpec.interfaceBuilder(methodTypeName(method))
        .addDeprecatedIfNeeded()
        .apply {
            inlineBodyTypeSpec?.let(::addType)
            if (!returns.isSingleUnitResponse()) {
                addType(buildResponseTypeSpec(config, methodClassName))
            }
            addFunction(
                toInvokeFunSpec(
                    config = config,
                    interfaceClassName = interfaceClassName,
                    methodClassName = methodClassName,
                    usesNestedBodyType = inlineBodyTypeSpec != null,
                )
            )
        }
        .build()
}

/** Build the operation invoke(...) signature with parameters and response wrapper. */
private fun Route.toInvokeFunSpec(
    config: RenderConfig,
    interfaceClassName: ClassName,
    methodClassName: ClassName,
    usesNestedBodyType: Boolean,
): FunSpec {
    val builder = FunSpec.builder("invoke")
        .addModifiers(KModifier.ABSTRACT, KModifier.SUSPEND, KModifier.OPERATOR)
        .addDeprecatedIfNeeded()

    // Collect non-path parameters, split into required/optional
    val nonPathParams = parameters.filter { it.input != Parameter.Input.Path }
    val requiredParams = nonPathParams.filter { it.isRequired }.sortedBy { it.input.sortOrder() }
    val optionalParams = nonPathParams.filter { !it.isRequired }.sortedBy { it.input.sortOrder() }

    // Resolve body parameters
    val bodyParams = body?.toInvokeParameterSpecs(config, methodClassName, usesNestedBodyType)
    val bodyRequired = body?.required == true

    // 1. Required params
    for (input in requiredParams) {
        builder.addParameter(input.toParameterSpec(config, interfaceClassName))
    }
    // 2. Required body
    if (bodyRequired && bodyParams != null) {
        bodyParams.forEach { builder.addParameter(it) }
    }
    // 3. Optional params
    for (input in optionalParams) {
        builder.addParameter(input.toParameterSpec(config, interfaceClassName))
    }
    // 4. Optional body
    if (!bodyRequired && bodyParams != null) {
        bodyParams.forEach { builder.addParameter(it) }
    }

    builder.returns(invokeReturnType(methodClassName))
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
    interfaceClassName: ClassName,
): ParameterSpec {
    val paramName = name.toParamName()
    val model = type

    val baseTypeName = model.inlineParameterTypeName(interfaceClassName, name) ?: model.toTypeName(config)

    val typeName = if (!isRequired) baseTypeName.copy(nullable = true) else baseTypeName

    // For inline enums, compute default using the overridden type name (not NamingContext)
    val literalDefault = if (model is Model.Enum && model.nestedOrNull() != null) {
        val enumClassName = interfaceClassName.nestedClass(name.toPascalCase())
        when (val d = model.default) {
            null -> null
            Model.Default.Null -> CodeBlock.of("null")
            is Model.Default.Value -> CodeBlock.of("%T.%L", enumClassName, toEnumValueName(d.value))
        }
    } else {
        model.defaultLiteral(config)
    }

    return ParameterSpec.builder(paramName, typeName).apply {
        when {
            isRequired && literalDefault != null -> defaultValue(literalDefault)
            !isRequired && literalDefault != null -> defaultValue(literalDefault)
            !isRequired -> defaultValue(CodeBlock.of("null"))
        }
    }.build()
}

private fun Model.inlineParameterTypeName(
    interfaceClassName: ClassName,
    paramName: String,
): ClassName? =
    if (this is Model.ContextHolder && context.head is NamingContext.Path && nestedOrNull() != null) {
        interfaceClassName.nestedClass(paramName.toPascalCase())
    } else null

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
private fun Route.Returns.needsSealedInterface(): Boolean {
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
): TypeSpec {
    if (returns.needsSealedInterface()) {
        return buildSealedResponseTypeSpec(config, methodClassName)
    }

    val model = returns.singlePreferredModelOrNull()
    if (model != null && model.isRouteInlineModel()) {
        model.toInlineOperationTypeSpecOrNull(config, "Response")?.let { return it }
    }

    return if (model == null || model is Model.Primitive.Unit) {
        TypeSpec.objectBuilder("Response")
            .addModifiers(KModifier.DATA)
            .build()
    } else {
        val typeName = model.toTypeName(config)
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
            .build()
    }
}

private fun Route.invokeReturnType(methodClassName: ClassName) =
    if (returns.isSingleUnitResponse()) UNIT else methodClassName.nestedClass("Response")

private fun Route.Returns.isSingleUnitResponse(): Boolean {
    if (needsSealedInterface()) return false
    val model = singlePreferredModelOrNull()
    return model == null || model is Model.Primitive.Unit
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

private fun Route.Returns.singlePreferredModelOrNull(): Model? {
    val singleResponse = responses.values.firstOrNull() ?: default ?: return null
    return singleResponse.preferredModel()
}

private fun Model.isRouteInlineModel(): Boolean =
    this is Model.ContextHolder && context.head is NamingContext.Path

private fun Model.toInlineOperationTypeSpecOrNull(config: RenderConfig, nameOverride: String): TypeSpec? =
    when (this) {
        is Model.Object -> toTypeSpec(config, nameOverride = nameOverride)
        is Model.Enum -> toTypeSpec(config, nameOverride = nameOverride)
        else -> null
    }

private fun Route.Bodies.inlineBodyTypeSpec(config: RenderConfig): TypeSpec? {
    val setBody = defaultOrNull() as? Route.Body.SetBody ?: return null
    if (!setBody.type.isRouteInlineModel()) return null
    return setBody.type.toInlineOperationTypeSpecOrNull(config, "Body")
}
