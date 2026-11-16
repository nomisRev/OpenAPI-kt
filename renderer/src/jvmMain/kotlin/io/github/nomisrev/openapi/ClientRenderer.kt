package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import io.github.nomisrev.openapi.transformers.nestedOrNull

fun ApiTree.generateClient(config: RenderConfig): List<FileSpec> =
    generateClientWithDiagnostics(config)
        .also(GenerationResult::throwOnErrors)
        .files

/** The client files and every diagnostic; an operation with an `Error` is not in the files ([renderableTree]). */
fun ApiTree.generateClientWithDiagnostics(config: RenderConfig): GenerationResult {
    val rendered = renderableTree()
    return GenerationResult(
        files = rendered.generateClientFiles(config.withResolvedNames(rendered)),
        diagnostics = generationDiagnostics(config),
    )
}

/** Whether the client package has a copy of the union helpers (`AttemptDeserialize.kt`), for its inline unions. */
internal fun ApiTree.clientNeedsSerializationUtils(): Boolean =
    hasInlineNonDiscriminatedParameterUnion() ||
            hasInlineNonDiscriminatedBodyUnion() ||
            hasInlineNonDiscriminatedResponseUnion()

internal fun ApiTree.generateClientFiles(config: RenderConfig): List<FileSpec> {
    if (children.isEmpty() && operations.isEmpty()) return emptyList()
    val needsSerializationUtils = clientNeedsSerializationUtils()

    val rootName = rootClassName(config).simpleName
    val rootClassName = ClassName(config.apiPackage, rootName)

    val childFiles = mutableListOf<FileSpec>()
    for ([child, name] in children.zip(children.uniqueNames(operations.keys))) {
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
            .addStatement("%N.close()", ClientStateName)
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

    for ([child, name] in children.zip(children.uniqueNames(operations.keys))) {
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

    return builder.buildChecked(className)
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

    val namedChildren = children.zip(children.uniqueNames(operations.keys))
    // A step with the name of an operation of this node (`/standards/get` next to `GET /standards`) and the operation
    // are one member: `get()` is the operation and `get.post()` is the step, as the paths read.
    val operationSteps = mutableMapOf<PathNode, TypeSpec>()
    for ([method, route] in orderedOperations) {
        val operationTypeSpec = route.toOperationTypeSpec(
            method = method,
            config = config,
            pathClassName = className,
            sharedInlineParameterKeys = sharedInlineParameterKeys,
            accumulatedParams = currentAccumulatedParams,
        )
        builder.addProperty(route.toOperationPropertySpec(method, className, currentAccumulatedParams))
        val step = namedChildren.firstOrNull { [child, name] ->
            (child.segment is PathSegment.Literal || child.segment is PathSegment.FixedValue) &&
                name.simpleName == operationTypeSpec.name && name.memberName.unescapeBackticks() == method.value.lowercase()
        }
        val merged = step?.let { [child, name] ->
            child.toTypeSpec(config, className.nestedClass(name.simpleName), currentAccumulatedParams)
                .withOperation(operationTypeSpec)
        }
        if (step != null && merged != null) operationSteps[step.first] = merged
        builder.addType(merged ?: operationTypeSpec)
    }

    for ([child, name] in namedChildren) {
        if (child in operationSteps) continue
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

    return builder.buildChecked(className)
}

/**
 * This class of a path step with the functions and types of [operation], the class of an operation of the step's
 * parent that has its name. Both are constructed from the client and the path parameters down to the parent. `null`
 * when a type nested in one has the name of a type nested in the other.
 */
private fun TypeSpec.withOperation(operation: TypeSpec): TypeSpec? {
    val nestedNames = typeSpecs.mapTo(mutableSetOf()) { it.name }
    if (operation.typeSpecs.any { it.name in nestedNames }) return null
    val propertyNames = propertySpecs.mapTo(mutableSetOf()) { it.name }
    val optIn = ClassName("kotlin", "OptIn")
    // `@OptIn` is not repeatable, and a deprecated operation does not deprecate the step: its functions say so.
    val optIns = (annotations + operation.annotations).filter { it.typeName == optIn }
        .flatMap { it.members }.distinctBy { it.toString() }
    return toBuilder().apply {
        annotations.removeAll { it.typeName == optIn }
        if (optIns.isNotEmpty()) {
            addAnnotation(AnnotationSpec.builder(optIn).apply { optIns.forEach { addMember(it) } }.build())
        }
        operation.funSpecs.forEach(::addFunction)
        operation.propertySpecs.filter { it.name !in propertyNames }.forEach(::addProperty)
        operation.typeSpecs.forEach(::addType)
    }.build()
}

/**
 * Builds a path or root class, failing when two of its properties (the private state in the primary constructor and the
 * navigation and operation members) have one name: KotlinPoet would write both and the class would not compile, or
 * the spec's member would be lost.
 */
private fun TypeSpec.Builder.buildChecked(className: ClassName): TypeSpec {
    val duplicates = propertySpecs.groupingBy { it.name }.eachCount().filterValues { it > 1 }.keys
    check(duplicates.isEmpty()) { "The class ${className.canonicalName} declares the properties $duplicates twice" }
    return build()
}
