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
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.transformers.nestedOrNull
import io.ktor.http.HttpMethod

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
        rootInterface.addFunction(route.toOperationFunSpec(method, config, rootClassName))
    }

    // Direct children: separate files, top-level interfaces
    val childFiles = mutableListOf<FileSpec>()
    for (child in children) {
        val childSimpleName = child.segment.name.toPascalCase()
        val childClassName = ClassName(config.apiPackage, childSimpleName)

        child.segment.addNavigationMember(rootInterface, childClassName, config)

        val childTypeSpec = child.toTypeSpec(config, childClassName)
        childFiles.add(
            FileSpec.builder(config.apiPackage, childSimpleName)
                .addType(childTypeSpec)
                .build()
        )
    }

    val rootFile = FileSpec.builder(config.apiPackage, rootName)
        .addType(rootInterface.build())
        .build()

    return listOf(rootFile) + childFiles
}

private fun PathNode.toTypeSpec(config: RenderConfig, className: ClassName): TypeSpec {
    val builder = TypeSpec.interfaceBuilder(className.simpleName)

    // Inline enum types from operations
    operations.values.flatMap { it.inlineEnums() }.forEach { (name, enum) ->
        builder.addType(enum.toTypeSpec(config, nameOverride = name))
    }

    // Operation stubs
    for ((method, route) in operations.entries.sortedBy { it.key.value }) {
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

/** Build the operation suspend function with parameters and body. */
private fun Route.toOperationFunSpec(
    method: HttpMethod,
    config: RenderConfig,
    interfaceClassName: ClassName,
): FunSpec {
    val builder = FunSpec.builder(method.value.lowercase())
        .addModifiers(KModifier.ABSTRACT, KModifier.SUSPEND)

    if (deprecated) {
        builder.addAnnotation(
            AnnotationSpec.builder(Deprecated::class)
                .addMember("%S", "Deprecated by the API provider")
                .build()
        )
    }

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
