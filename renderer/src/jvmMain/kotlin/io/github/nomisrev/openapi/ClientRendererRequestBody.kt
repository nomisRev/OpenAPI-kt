@file:Suppress("TooManyFunctions")
package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.joinToCode
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.transformers.nestedOrNull

private val MapType = ClassName("kotlin.collections", "Map")

internal data class FlattenedBodyRendering(
    val bodyTypeSpec: TypeSpec,
    val publicTypeSpecs: List<TypeSpec>,
    val overloads: List<FlattenedBodyOverload>,
    val externalTypeNames: Map<ClassName, TypeName>,
    val bodyHasConstructorArgs: Boolean,
)

private data class FlattenedBodyPublicType(
    val model: Model,
    val typeSpec: TypeSpec,
)

internal data class FlattenedBodyOverload(
    val parameters: List<ParameterSpec>,
    val bodyConstructorArgs: CodeBlock,
    val bodyHasConstructorArgs: Boolean,
    val bodyParamsBeforeOptionalParams: Boolean,
    val bodyMayBeNull: Boolean,
)

private data class FlattenedBodyOverloadContext(
    val config: RenderConfig,
    val methodClassName: ClassName,
    val bodyModel: Model.Object,
    val typeNameRemaps: Map<ClassName, TypeName>,
    val additionalProperty: Model.Object.AdditionalProperties.Schema?,
    val selectedModels: Map<String, Model>,
    val bodyParamsBeforeOptionalParams: Boolean,
    val bodyMayBeNull: Boolean,
)

internal fun Route.Bodies.flattenedBodyRendering(
    config: RenderConfig,
    methodClassName: ClassName,
): FlattenedBodyRendering? {
    val body = defaultOrNull() as? Route.Body.SetBody
    val model = body?.type?.nestedOrNull() as? Model.Object
    val isInlineAdditionalProps = body == null || model == null || !model.isRouteInlineModel() ||
            model.additionalProperties is Model.Object.AdditionalProperties.Schema
    return if (isInlineAdditionalProps) {
        null
    } else {
        val publicTypes = buildFlattenedBodyPublicTypes(
            config = config,
            methodClassName = methodClassName,
            bodyModel = model,
            requiredPrimitiveUnionFieldNames = model.properties.entries
                .filter { (_, property) -> property.isRequired && property.model.isFlattenablePrimitiveUnion() }
                .mapTo(mutableSetOf()) { it.key },
        )

        val publicTypeSpecs = publicTypes.map(FlattenedBodyPublicType::typeSpec)
        val publicTypeRemaps = publicTypes.associate { publicType ->
            val contextHolder = publicType.model as? Model.ContextHolder
                ?: error("Public body type must be context-aware: ${publicType.model}")
            val nestedClassName = contextHolder.context.toClassName(config)
            nestedClassName to methodClassName.nestedClass(
                requireNotNull(publicType.typeSpec.name)
            )
        }

        val overloads = buildFlattenedBodyOverloads(
            config = config,
            methodClassName = methodClassName,
            bodyModel = model,
            bodyRequired = required,
            typeNameRemaps = publicTypeRemaps,
        )

        if (overloads == null) {
            null
        } else {
            FlattenedBodyRendering(
                bodyTypeSpec = bodyTypeSpec(
                    config = config,
                    methodClassName = methodClassName,
                    bodyModel = model,
                    externalTypeNames = publicTypeRemaps,
                ),
                publicTypeSpecs = publicTypeSpecs,
                overloads = overloads,
                externalTypeNames = publicTypeRemaps,
                bodyHasConstructorArgs = model.properties.isNotEmpty(),
            )
        }
    }
}

private fun buildFlattenedBodyOverloads(
    config: RenderConfig,
    methodClassName: ClassName,
    bodyModel: Model.Object,
    bodyRequired: Boolean,
    typeNameRemaps: Map<ClassName, TypeName>,
    maxOverloads: Long = 32
): List<FlattenedBodyOverload>? {
    val propertyOptions = bodyModel.properties.entries.map { (name, property) ->
        val modelOptions = if (property.isRequired && property.model.isFlattenablePrimitiveUnion()) {
            val union = property.model as Model.Union
            union.cases.map { case -> case.model }
        } else {
            listOf(property.model)
        }
        name to modelOptions
    }
    val additionalProperty = bodyModel.additionalProperties as? Model.Object.AdditionalProperties.Schema

    val overloadCount = propertyOptions.fold(1) { acc, (_, options) ->
        acc * options.size
    }
    if (overloadCount > maxOverloads) return null

    val bodyParamsBeforeOptionalParams = bodyModel.properties.values.any { it.isRequired }
    val bodyMayBeNull = !bodyRequired && bodyModel.properties.values.none { it.isRequired }
    val parameterGroups = propertyOptions.fold(listOf(emptyMap<String, Model>())) { acc, (name, options) ->
        acc.flatMap { current ->
            options.map { option -> current + (name to option) }
        }
    }

    return parameterGroups.map { selectedModels ->
        buildFlattenedBodyOverload(
            FlattenedBodyOverloadContext(
                config = config,
                methodClassName = methodClassName,
                bodyModel = bodyModel,
                typeNameRemaps = typeNameRemaps,
                additionalProperty = additionalProperty,
                selectedModels = selectedModels,
                bodyParamsBeforeOptionalParams = bodyParamsBeforeOptionalParams,
                bodyMayBeNull = bodyMayBeNull,
            )
        )
    }
}

private fun buildFlattenedBodyOverload(
    context: FlattenedBodyOverloadContext,
): FlattenedBodyOverload {
    val parameters = mutableListOf<ParameterSpec>()
    val constructorArgPieces = mutableListOf<CodeBlock>()

    context.bodyModel.properties.entries.forEach { (propertyName, property) ->
        val paramName = propertyName.toParamName()
        val selectedCase = if (property.isRequired && property.model.isFlattenablePrimitiveUnion()) {
            context.selectedModels.getValue(propertyName)
        } else {
            null
        }
        val typeName = flattenedBodyParameterTypeName(
            config = context.config,
            propertyModel = property.model,
            typeNameRemaps = context.typeNameRemaps,
            selectedCase = selectedCase,
        ).copy(nullable = property.model.isNullable || !property.isRequired)
        val parameter = ParameterSpec.builder(paramName, typeName).apply {
            if (!property.isRequired) {
                defaultValue(CodeBlock.of("null"))
            }
        }.build()
        parameters += parameter
        constructorArgPieces += CodeBlock.of(
            "%L = %L",
            paramName,
            flattenedBodyValueExpression(
                propertyName = propertyName,
                selectedCase = selectedCase,
                paramName = paramName,
                methodClassName = context.methodClassName,
            ),
        )
    }

    if (context.additionalProperty != null) {
        val additionalTypeName = MapType.parameterizedBy(
            ClassName("kotlin", "String"),
            context.additionalProperty.value.publicInputTypeName(context.config).remapTypeNames(context.typeNameRemaps),
        ).copy(nullable = true)
        parameters += ParameterSpec.builder("additional", additionalTypeName)
            .defaultValue(CodeBlock.of("null"))
            .build()
        constructorArgPieces += CodeBlock.of("additional = additional")
    }

    return FlattenedBodyOverload(
        parameters = parameters,
        bodyConstructorArgs = constructorArgPieces.joinToCode(separator = ", "),
        bodyHasConstructorArgs = constructorArgPieces.isNotEmpty(),
        bodyParamsBeforeOptionalParams = context.bodyParamsBeforeOptionalParams,
        bodyMayBeNull = context.bodyMayBeNull,
    )
}

private fun buildFlattenedBodyPublicTypes(
    config: RenderConfig,
    methodClassName: ClassName,
    bodyModel: Model.Object,
    requiredPrimitiveUnionFieldNames: Set<String>,
): List<FlattenedBodyPublicType> {
    val regularTypes = bodyModel.properties.entries.mapNotNull { (propertyName, property) ->
        if (propertyName in requiredPrimitiveUnionFieldNames) return@mapNotNull null
        val nestedModel = property.model.nestedOrNull() ?: return@mapNotNull null
        if (nestedModel is Model.Object && nestedModel.isScalarWrapper) return@mapNotNull null
        val nestedContext = nestedModel as? Model.ContextHolder ?: return@mapNotNull null
        val nestedClassName = nestedContext.context.toClassName(config)
        val typeSpec = nestedModel.toInlineOperationTypeSpecOrNull(
            config = config,
            ownerClassName = methodClassName,
            nameOverride = nestedClassName.simpleName,
        ) ?: return@mapNotNull null
        FlattenedBodyPublicType(nestedModel, typeSpec)
    }
    val additionalTypes = (bodyModel.additionalProperties as? Model.Object.AdditionalProperties.Schema)
        ?.value
        ?.nestedOrNull()
        ?.takeIf { it !is Model.Object || !it.isScalarWrapper }
        ?.let { nestedModel ->
            val nestedContext = nestedModel as? Model.ContextHolder ?: return@let null
            val nestedClassName = nestedContext.context.toClassName(config)
            val typeSpec = nestedModel.toInlineOperationTypeSpecOrNull(
                config = config,
                ownerClassName = methodClassName,
                nameOverride = nestedClassName.simpleName,
            ) ?: return@let null
            listOf(FlattenedBodyPublicType(nestedModel, typeSpec))
        }.orEmpty()
    return (regularTypes + additionalTypes).distinctBy { it.typeSpec.name }
}

private fun bodyTypeSpec(
    config: RenderConfig,
    methodClassName: ClassName,
    bodyModel: Model.Object,
    externalTypeNames: Map<ClassName, TypeName>,
): TypeSpec {
    val bodyClassName = methodClassName.nestedClass("Body")
    return bodyModel.toTypeSpec(
        config = config,
        classNameOverride = bodyClassName,
        externalTypeNames = externalTypeNames,
    ).toBuilder()
        .addModifiers(KModifier.INTERNAL)
        .build()
}

@Suppress("LongParameterList")
private fun flattenedBodyParameterTypeName(
    config: RenderConfig,
    propertyModel: Model,
    typeNameRemaps: Map<ClassName, TypeName>,
    selectedCase: Model?,
): TypeName {
    val typeName = when {
        selectedCase != null -> selectedCase.publicInputTypeName(config)
        else -> propertyModel.publicInputTypeName(config)
    }
    return typeName.remapTypeNames(typeNameRemaps)
}

internal fun Route.buildFlattenedBodyOperationBody(
    config: RenderConfig,
    method: io.ktor.http.HttpMethod,
    methodClassName: ClassName,
    overload: FlattenedBodyOverload,
): CodeBlock {
    val bodyClassName = methodClassName.nestedClass("Body")
    val bodyExpr = if (!overload.bodyHasConstructorArgs) {
        CodeBlock.of("%T", bodyClassName)
    } else {
        CodeBlock.of("%T(%L)", bodyClassName, overload.bodyConstructorArgs)
    }
    val bodyGuard = if (overload.bodyMayBeNull) {
        overload.parameters.joinToString(" || ") { parameter -> "${parameter.name} != null" }
            .takeIf(String::isNotBlank)
    } else {
        null
    }
    if (overload.bodyMayBeNull && bodyGuard == null) {
        return buildOperationBody(
            config = config,
            method = method,
            methodClassName = methodClassName,
            includeBody = false,
        )
    }
    return buildOperationBody(
        config = config,
        method = method,
        methodClassName = methodClassName,
        bodyExpr = bodyExpr,
        bodyGuard = bodyGuard,
    )
}

private fun flattenedBodyValueExpression(
    propertyName: String,
    selectedCase: Model?,
    paramName: String,
    methodClassName: ClassName,
): CodeBlock {
    if (selectedCase == null) return CodeBlock.of(paramName)
    val caseClassName = methodClassName.nestedClass("Body")
        .nestedClass(propertyName.toPascalCase())
        .nestedClass(selectedCase.flattenedPrimitiveUnionCaseName())
    return CodeBlock.of("%T(%L)", caseClassName, paramName)
}

private fun Model.isFlattenablePrimitiveUnion(): Boolean =
    this is Model.Union && cases.all { case ->
        case.model is Model.Primitive ||
                case.model is Model.Uuid ||
                case.model is Model.Date ||
                case.model is Model.DateTime ||
                case.model is Model.ByteArray
    }

private fun Model.flattenedPrimitiveUnionCaseName(): String = when (this) {
    is Model.Primitive.String -> "CaseString"
    is Model.Primitive.Int -> "CaseInt"
    is Model.Primitive.Long -> "CaseLong"
    is Model.Primitive.Float -> "CaseFloat"
    is Model.Primitive.Double -> "CaseDouble"
    is Model.Primitive.Boolean -> "CaseBoolean"
    is Model.Primitive.Unit -> "Empty"
    is Model.ByteArray -> "CaseBinary"
    is Model.Date -> "CaseDate"
    is Model.DateTime -> "CaseDateTime"
    is Model.Uuid -> "CaseUuid"

    is Model.Collection,
    is Model.DiscriminatedObject,
    is Model.Enum,
    is Model.FreeFormJson,
    is Model.Object,
    is Model.Reference,
    is Model.AnyOf,
    is Model.OneOf -> "Case"
}

internal fun Route.Bodies.inlineBodyTypeSpec(
    config: RenderConfig,
    ownerClassName: ClassName,
    externalTypeNames: Map<ClassName, TypeName> = emptyMap(),
    internal: Boolean = false,
): TypeSpec? =
    when (val body = defaultOrNull()) {
        is Route.Body.OverloadedBody -> null
        else -> body
            .bodyModelOrNull()
            ?.takeIf(Model::isRouteInlineModel)
            ?.toInlineOperationTypeSpecOrNull(
                config = config,
                ownerClassName = ownerClassName,
                nameOverride = "Body",
                externalTypeNames = externalTypeNames,
            )
            ?.let { typeSpec ->
                if (internal) typeSpec.toBuilder().addModifiers(KModifier.INTERNAL).build() else typeSpec
            }
    }

internal data class OverloadedBodyCaseSignature(
    val typeName: TypeName,
    val jvmName: String?,
)

private val KotlinListType = ClassName("kotlin.collections", "List")

internal fun Route.Body.OverloadedBody.directInlineTypeSpecs(
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

internal fun Route.Body.OverloadedBody.distinctCaseSignatures(
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
