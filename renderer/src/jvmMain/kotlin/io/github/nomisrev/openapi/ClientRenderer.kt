package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec

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
