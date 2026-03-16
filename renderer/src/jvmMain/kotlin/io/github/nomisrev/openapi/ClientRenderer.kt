package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.transformers.nestedOrNull
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode

fun ApiTree.generateClient(config: RenderConfig): List<FileSpec> {
    if (children.isEmpty() && operations.isEmpty()) return emptyList()

    val rootName = name.toPascalCase()
    val rootClassName = ClassName(config.apiPackage, rootName)
    val rootInterface = TypeSpec.interfaceBuilder(rootName)

    // Root-level inline enum types
    operations.values.flatMap { it.inlineEnums() }.forEach { (name, enum) ->
        rootInterface.addType(enum.toTypeSpec(config, nameOverride = name))
    }

    // Root-level operation stubs
    for ((method, route) in operations.entries.sortedBy { it.key.value }) {
        route.buildResultTypeSpec(method, config, rootClassName)?.let { rootInterface.addType(it) }
        rootInterface.addFunction(route.toOperationFunSpec(method, config, rootClassName))
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

    return listOf(rootFileBuilder.build()) + childFiles
}

private fun PathNode.toTypeSpec(config: RenderConfig, className: ClassName): TypeSpec {
    val builder = TypeSpec.interfaceBuilder(className.simpleName)

    // Inline enum types from operations
    operations.values.flatMap { it.inlineEnums() }.forEach { (name, enum) ->
        builder.addType(enum.toTypeSpec(config, nameOverride = name))
    }

    // Operation stubs
    for ((method, route) in operations.entries.sortedBy { it.key.value }) {
        route.buildResultTypeSpec(method, config, className)?.let { builder.addType(it) }
        builder.addFunction(route.toOperationFunSpec(method, config, className))
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

/** Collect inline enum types from non-path parameters, paired with their simple name (PascalCase of param name). */
private fun Route.inlineEnums(): List<Pair<String, Model.Enum>> =
    parameters
        .filter { it.input != Parameter.Input.Path }
        .mapNotNull { input ->
            val model = input.type
            if (model is Model.Enum && model.nestedOrNull() != null) {
                Pair(input.name.toPascalCase(), model)
            } else null
        }

context(route: Route)
fun FunSpec.Builder.addDeprecatedIfNeeded() = apply {
    if (route.deprecated) {
        addAnnotation(
            AnnotationSpec.builder(Deprecated::class).addMember("%S", "Deprecated by the API provider").build()
        )
    }
}

/** Build the operation suspend function with parameters and body. */
private fun Route.toOperationFunSpec(
    method: HttpMethod,
    config: RenderConfig,
    interfaceClassName: ClassName,
): FunSpec {
    val builder = FunSpec.builder(method.value.lowercase())
        .addModifiers(KModifier.ABSTRACT, KModifier.SUSPEND)
        .addDeprecatedIfNeeded()

    // Collect non-path parameters, split into required/optional
    val nonPathParams = parameters.filter { it.input != Parameter.Input.Path }
    val requiredParams = nonPathParams.filter { it.isRequired }.sortedBy { it.input.sortOrder() }
    val optionalParams = nonPathParams.filter { !it.isRequired }.sortedBy { it.input.sortOrder() }

    // Resolve body parameters
    val bodyParams = body?.toParameterSpecs(config)
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

    // Return type
    val returnType = returns.resolveReturnTypeName(method, config, interfaceClassName)
    if (returnType != null) {
        builder.returns(returnType)
    }

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

    // For inline enums, use param name (PascalCase) as the nested enum type name
    val baseTypeName = if (model is Model.Enum && model.nestedOrNull() != null) {
        interfaceClassName.nestedClass(name.toPascalCase())
    } else {
        model.toTypeName(config)
    }

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

private fun Route.Bodies.toParameterSpecs(config: RenderConfig): List<ParameterSpec>? {
    val body = defaultOrNull() ?: return null
    return when (body) {
        is Route.Body.SetBody -> {
            val typeName = body.type.toTypeName(config).let {
                if (!required) it.copy(nullable = true) else it
            }
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

/** Whether the returns need a sealed interface (multiple responses or has default + status). */
private fun Route.Returns.needsSealedInterface(): Boolean {
    val totalCases = responses.size + (if (default != null) 1 else 0)
    return totalCases > 1
}

/** Convert HttpStatusCode description to PascalCase for sealed interface case names. */
private fun HttpStatusCode.toCaseName(): String =
    description.split(" ").joinToString("") { word ->
        word.lowercase().replaceFirstChar { it.uppercase() }
    }

/** Resolve the return TypeName for an operation. Returns null for Unit return. */
private fun Route.Returns.resolveReturnTypeName(
    method: HttpMethod,
    config: RenderConfig,
    interfaceClassName: ClassName,
): TypeName? {
    if (needsSealedInterface()) {
        val methodName = method.value.lowercase().replaceFirstChar { it.uppercase() }
        return interfaceClassName.nestedClass("${methodName}Result")
    }
    // Single response
    val singleResponse = responses.values.firstOrNull() ?: default ?: return null
    val model = singleResponse.preferredModel() ?: return null
    if (model is Model.Primitive.Unit) return null
    return model.toTypeName(config)
}

/** Build a sealed result interface TypeSpec when multiple responses exist. */
private fun Route.buildResultTypeSpec(
    method: HttpMethod,
    config: RenderConfig,
    interfaceClassName: ClassName,
): TypeSpec? {
    if (!returns.needsSealedInterface()) return null

    val methodName = method.value.lowercase().replaceFirstChar { it.uppercase() }
    val resultName = "${methodName}Result"
    val resultClassName = interfaceClassName.nestedClass(resultName)

    val builder = TypeSpec.interfaceBuilder(resultName)
        .addModifiers(KModifier.SEALED)

    // Status code cases (ordered by status code value)
    for ((statusCode, returnType) in returns.responses.entries.sortedBy { it.key.value }) {
        val caseName = statusCode.toCaseName()
        val model = returnType.preferredModel()

        if (model == null || model is Model.Primitive.Unit) {
            builder.addType(
                TypeSpec.objectBuilder(caseName)
                    .addModifiers(KModifier.DATA)
                    .addSuperinterface(resultClassName)
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
                    .addSuperinterface(resultClassName)
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
            .addSuperinterface(resultClassName)

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
