package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import io.github.nomisrev.openapi.transformers.nestedOrNull

fun ApiTree.generateClient(config: RenderConfig): List<FileSpec> =
    generateClientWithDiagnostics(config)
        .also(GenerationResult::throwOnErrors)
        .files

fun ApiTree.generateClientWithDiagnostics(config: RenderConfig): GenerationResult {
    val diagnostics = generationDiagnostics(config)
    val filteredTree = withSupportedRequestBodiesOnly()
    return GenerationResult(
        files = filteredTree.generateClientFiles(config.withOptionalTypeName(this).withNamedModels(models)),
        diagnostics = diagnostics,
    )
}

private fun ApiTree.generateClientFiles(config: RenderConfig): List<FileSpec> {
    if (children.isEmpty() && operations.isEmpty()) return emptyList()
    val needsSerializationUtils =
        hasInlineNonDiscriminatedParameterUnion() ||
                hasInlineNonDiscriminatedBodyUnion() ||
                hasInlineNonDiscriminatedResponseUnion()

    val rootName = name.toPascalCase()
    val rootClassName = ClassName(config.apiPackage, rootName)

    val childFiles = mutableListOf<FileSpec>()
    for ([child, name] in children.zip(children.uniqueNames())) {
        val childSimpleName = name.simpleName
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

    // The tri-state type lives in the model package only. The models' files include it when a model needs it; when
    // only an inline model or a flattened body of an operation does, it is added here.
    val operationsNeedIt = buildSet { collectOptionalPackages(config, this) }.isNotEmpty()
    val modelsGenerateIt = buildSet { models.forEach { it.collectOptionalPackages(config, this) } }.isNotEmpty()
    val optional = if (operationsNeedIt && !modelsGenerateIt) {
        listOf(generateOptionalSupport(config, config.modelPackage.toPackageName()))
    } else emptyList()

    val ianaCodes = buildSet { collectIanaStatusCodes(this) }
    val statusCodes = if (ianaCodes.isNotEmpty()) listOf(generateStatusCodeSupport(config, ianaCodes)) else emptyList()

    return files + serializationUtils + optional + statusCodes
}

private fun ApiTree.toRootTypeSpec(config: RenderConfig, className: ClassName): TypeSpec {
    val builder = TypeSpec.classBuilder(className.simpleName)
        .addSuperinterface(ClassName("kotlin", "AutoCloseable"))
    builder.addClientConstructorAndState(config, emptyList())
    for (ctor in generateSecondaryConstructors(config)) {
        builder.addFunction(ctor)
    }
    builder.addFunction(
        com.squareup.kotlinpoet.FunSpec.builder("close")
            .addModifiers(com.squareup.kotlinpoet.KModifier.OVERRIDE)
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

    sharedInlineParameterModels.forEach { inline ->
        inline.model
            .toInlineParameterTypeSpec(config, className, inline.simpleName)
            ?.let(builder::addType)
    }

    for ([method, route] in orderedOperations) {
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

    for ([child, name] in children.zip(children.uniqueNames())) {
        val childClassName = ClassName(config.apiPackage, name.simpleName)
        child.segment.addConcreteNavigationMember(
            builder = builder,
            memberName = name.memberName,
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
    // An inline enum or object parameter of a composite is a type nested in the class of its own step
    // (`ArticlesPath.MediaTypeExtensionPath.MediaTypeExtension`): the navigation function takes it.
    (segment as? PathSegment.Parameter)?.let { param ->
        param.model.nestedOrNull()
            ?.takeIf { (it as? Model.ContextHolder)?.context?.head is NamingContext.Path }
            ?.toInlineParameterTypeSpec(config, className, param.name.toPascalCase())
            ?.let(builder::addType)
    }
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

    for ([method, route] in orderedOperations) {
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

    for ([child, name] in children.zip(children.uniqueNames())) {
        val childClassName = className.nestedClass(name.simpleName)

        child.segment.addConcreteNavigationMember(
            builder = builder,
            memberName = name.memberName,
            childClassName = childClassName,
            currentAccumulatedParams = currentAccumulatedParams,
            parentClassName = className,
            config = config,
        )
        builder.addType(child.toTypeSpec(config, childClassName, currentAccumulatedParams))
    }

    return builder.build()
}
