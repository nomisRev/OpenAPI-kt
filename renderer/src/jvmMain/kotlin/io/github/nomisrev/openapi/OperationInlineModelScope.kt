package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.transformers.nestedOrNull
import kotlinx.serialization.json.Json

internal class OperationInlineModelScope(
    private val typeRemaps: Map<ClassName, TypeName>,
    private val methodStandaloneTypes: List<StandaloneInlineType>,
    private val responseStandaloneTypes: List<StandaloneInlineType>,
) {
    fun remap(typeName: TypeName): TypeName = typeName.remapTypeNames(typeRemaps)

    fun externalTypeNames(): Map<ClassName, TypeName> = typeRemaps

    fun methodTypeSpecs(config: RenderConfig): List<TypeSpec> =
        methodStandaloneTypes.mapNotNull { it.toTypeSpec(config, typeRemaps) }

    fun responseTypeSpecs(config: RenderConfig): List<TypeSpec> =
        responseStandaloneTypes.mapNotNull { it.toTypeSpec(config, typeRemaps) }

    companion object {
        fun empty(): OperationInlineModelScope = OperationInlineModelScope(
            typeRemaps = emptyMap(),
            methodStandaloneTypes = emptyList(),
            responseStandaloneTypes = emptyList(),
        )
    }
}

private val InlineModelSharingJson = Json { encodeDefaults = true }
private val SharedNamingContext = NamingContext.reference("Shared", SchemaContext.Null)

private enum class InlineCandidateKind(val priority: Int) {
    OverloadedRoot(priority = 0),
    OverloadedCollectionItem(priority = 0),
    OverloadedNested(priority = 1),
    ResponseCollectionItem(priority = 2),
}

private data class OperationInlineModelCandidate(
    val kind: InlineCandidateKind,
    val model: Model,
    val sourceClassName: ClassName,
    val simpleName: String,
    val targetClassName: ClassName,
    val sourceOrder: Int,
) {
    val structuralKey: String =
        InlineModelSharingJson.encodeToString(model.normalizedForSharingKey())

    val groupingKey: Pair<String, String>
        get() = simpleName to structuralKey
}

internal data class StandaloneInlineType(
    val model: Model,
    val targetClassName: ClassName,
)

internal fun Route.operationInlineModelScope(
    config: RenderConfig,
    methodClassName: ClassName,
): OperationInlineModelScope {
    // When the response has no wrapper class (single direct model), inline response types
    // must be placed on the method class itself instead of a nested Response class.
    val responseOwnerClassName =
        if (returns.isSingleDirectModelResponse()) methodClassName
        else methodClassName.nestedClass("Response")

    val overloadedBody = body?.defaultOrNull() as? Route.Body.OverloadedBody ?: return responseInlineModelScopeOnly(
        config = config,
        methodClassName = methodClassName,
        responseOwnerClassName = responseOwnerClassName,
    )
    var sourceOrder = 0
    val candidates = buildList {
        overloadedBody.cases.forEach { case ->
            case.model.directOverloadedBodyCandidateOrNull(config, methodClassName, sourceOrder++)?.let(::add)
            addAll(case.model.nestedOverloadedBodyCandidates(config, methodClassName, sourceOrder).also {
                sourceOrder += it.size
            })
            case.model.collectionItemCandidateOrNull(config, methodClassName, InlineCandidateKind.OverloadedCollectionItem, sourceOrder++)
                ?.let(::add)
        }
        returns.singlePreferredModelOrNull()
            ?.collectionItemCandidateOrNull(
                config = config,
                ownerClassName = responseOwnerClassName,
                kind = InlineCandidateKind.ResponseCollectionItem,
                sourceOrder = sourceOrder++,
            )
            ?.let(::add)
    }
    return candidates.toOperationInlineModelScope(methodClassName, responseOwnerClassName)
}

private fun Route.responseInlineModelScopeOnly(
    config: RenderConfig,
    methodClassName: ClassName,
    responseOwnerClassName: ClassName = methodClassName.nestedClass("Response"),
): OperationInlineModelScope {
    val candidate = returns.singlePreferredModelOrNull()
        ?.collectionItemCandidateOrNull(
            config = config,
            ownerClassName = responseOwnerClassName,
            kind = InlineCandidateKind.ResponseCollectionItem,
            sourceOrder = 0,
        )
        ?: return OperationInlineModelScope.empty()
    return listOf(candidate).toOperationInlineModelScope(methodClassName, responseOwnerClassName)
}

private fun List<OperationInlineModelCandidate>.toOperationInlineModelScope(
    methodClassName: ClassName,
    responseClassName: ClassName,
): OperationInlineModelScope {
    if (isEmpty()) return OperationInlineModelScope.empty()

    val winners = groupBy(OperationInlineModelCandidate::groupingKey)
        .mapValues { (_, candidates) ->
            candidates.minWith(
                compareBy<OperationInlineModelCandidate> { it.kind.priority }
                    .thenBy { it.sourceOrder }
            )
        }

    val typeRemaps = linkedMapOf<ClassName, TypeName>()
    for (candidate in this) {
        val winner = winners.getValue(candidate.groupingKey)
        if (candidate.sourceClassName != winner.targetClassName) {
            typeRemaps[candidate.sourceClassName] = winner.targetClassName
        }
    }

    val methodStandaloneTypes = linkedMapOf<ClassName, StandaloneInlineType>()
    val responseStandaloneTypes = linkedMapOf<ClassName, StandaloneInlineType>()
    for (winner in winners.values.sortedBy(OperationInlineModelCandidate::sourceOrder)) {
        val standalone = when (winner.kind) {
            InlineCandidateKind.OverloadedCollectionItem ->
                StandaloneInlineType(winner.model, winner.targetClassName)

            InlineCandidateKind.ResponseCollectionItem ->
                StandaloneInlineType(winner.model, winner.targetClassName)

            InlineCandidateKind.OverloadedNested,
            InlineCandidateKind.OverloadedRoot -> null
        } ?: continue

        when {
            winner.targetClassName.enclosingClassName() == methodClassName ->
                methodStandaloneTypes.putIfAbsent(winner.targetClassName, standalone)

            winner.targetClassName.enclosingClassName() == responseClassName ->
                responseStandaloneTypes.putIfAbsent(winner.targetClassName, standalone)
        }
    }

    return OperationInlineModelScope(
        typeRemaps = typeRemaps,
        methodStandaloneTypes = methodStandaloneTypes.values.toList(),
        responseStandaloneTypes = responseStandaloneTypes.values.toList(),
    )
}

private fun Model.directOverloadedBodyCandidateOrNull(
    config: RenderConfig,
    methodClassName: ClassName,
    sourceOrder: Int,
): OperationInlineModelCandidate? {
    val contextHolder = this as? Model.ContextHolder ?: return null
    if (contextHolder.context.head !is NamingContext.Path) return null
    return when (this) {
        is Model.Object,
        is Model.Enum -> {
            val sourceClassName = contextHolder.context.toClassName(config)
            OperationInlineModelCandidate(
                kind = InlineCandidateKind.OverloadedRoot,
                model = this,
                sourceClassName = sourceClassName,
                simpleName = sourceClassName.simpleName,
                targetClassName = methodClassName.nestedClass(sourceClassName.simpleName),
                sourceOrder = sourceOrder,
            )
        }

        else -> null
    }
}

private fun Model.nestedOverloadedBodyCandidates(
    config: RenderConfig,
    methodClassName: ClassName,
    sourceOrderStart: Int,
): List<OperationInlineModelCandidate> {
    val rootModel = this as? Model.Object ?: return emptyList()
    val rootSimpleName = rootModel.context.toClassName(config).simpleName
    val rootTargetClassName = methodClassName.nestedClass(rootSimpleName)
    return rootModel.properties.entries
        .sortedBy { it.key }
        .mapIndexedNotNull { index, (_, property) ->
            val nestedModel = property.model.nestedOrNull() as? Model.ContextHolder ?: return@mapIndexedNotNull null
            when (nestedModel) {
                is Model.Object -> {
                    val sourceClassName = nestedModel.context.toClassName(config)
                    val simpleName = if (property.model is Model.Collection) {
                        nestedModel.collectionItemSimpleName(config)
                    } else {
                        sourceClassName.simpleName
                    }
                    OperationInlineModelCandidate(
                        kind = InlineCandidateKind.OverloadedNested,
                        model = nestedModel,
                        sourceClassName = sourceClassName,
                        simpleName = simpleName,
                        targetClassName = rootTargetClassName.nestedClass(simpleName),
                        sourceOrder = sourceOrderStart + index,
                    )
                }

                is Model.Enum -> {
                    val sourceClassName = nestedModel.context.toClassName(config)
                    OperationInlineModelCandidate(
                        kind = InlineCandidateKind.OverloadedNested,
                        model = nestedModel,
                        sourceClassName = sourceClassName,
                        simpleName = sourceClassName.simpleName,
                        targetClassName = rootTargetClassName.nestedClass(sourceClassName.simpleName),
                        sourceOrder = sourceOrderStart + index,
                    )
                }

                is Model.DiscriminatedObject,
                is Model.Reference,
                is Model.OneOf,
                is Model.AnyOf -> null
            }
        }
        .toList()
}

private fun Model.collectionItemCandidateOrNull(
    config: RenderConfig,
    ownerClassName: ClassName,
    kind: InlineCandidateKind,
    sourceOrder: Int,
): OperationInlineModelCandidate? {
    val collection = this as? Model.Collection ?: return null
    val nestedModel = collection.inner.nestedOrNull() as? Model.ContextHolder ?: return null
    val sourceClassName = nestedModel.context.toClassName(config)
    return when (val model = nestedModel) {
        is Model.Object -> {
            val simpleName = model.collectionItemSimpleName(config)
            OperationInlineModelCandidate(
                kind = kind,
                model = model,
                sourceClassName = sourceClassName,
                simpleName = simpleName,
                targetClassName = ownerClassName.nestedClass(simpleName),
                sourceOrder = sourceOrder,
            )
        }

        is Model.Enum -> OperationInlineModelCandidate(
            kind = kind,
            model = model,
            sourceClassName = sourceClassName,
            simpleName = sourceClassName.simpleName,
            targetClassName = ownerClassName.nestedClass(sourceClassName.simpleName),
            sourceOrder = sourceOrder,
        )

        is Model.DiscriminatedObject,
        is Model.Reference,
        is Model.OneOf,
        is Model.AnyOf -> null
    }
}

private fun Model.Object.collectionItemSimpleName(config: RenderConfig): String =
    properties.keys
        .joinToString(separator = "And") { it.toPascalCase() }
        .takeIf(String::isNotBlank)
        ?: context.toClassName(config).simpleName

private fun StandaloneInlineType.toTypeSpec(
    config: RenderConfig,
    typeRemaps: Map<ClassName, TypeName>,
): TypeSpec? =
    when (model) {
        is Model.Object ->
            model.toTypeSpec(
                config = config,
                classNameOverride = targetClassName,
                externalTypeNames = typeRemaps,
            )

        is Model.Enum -> model.toTypeSpec(config, nameOverride = targetClassName.simpleName)

        is Model.ByteArray,
        is Model.Collection,
        is Model.Date,
        is Model.DateTime,
        is Model.DiscriminatedObject,
        is Model.FreeFormJson,
        is Model.Primitive,
        is Model.Reference,
        is Model.Union,
        is Model.Uuid -> null
    }

@Suppress("CyclomaticComplexMethod", "LongMethod")
private fun Model.normalizedForSharingKey(): Model =
    when (this) {
        is Model.ByteArray -> copy(description = null, title = null)
        is Model.Collection -> copy(
            inner = inner.normalizedForSharingKey(),
            description = null,
            title = null,
        )
        is Model.Date -> copy(description = null, title = null)
        is Model.DateTime -> copy(description = null, title = null)
        is Model.DiscriminatedObject -> copy(
            context = SharedNamingContext,
            abstractProperties = abstractProperties.mapValues { (_, property) ->
                property.copy(model = property.model.normalizedForSharingKey())
            },
            subtypes = subtypes.map { it.normalizedForSharingKey() as Model.Object },
            description = null,
            title = null,
        )
        is Model.Enum -> copy(
            context = SharedNamingContext,
            inner = inner.normalizedForSharingKey(),
            description = null,
            title = null,
        )
        is Model.FreeFormJson -> copy(description = null, title = null)
        is Model.Object -> copy(
            context = SharedNamingContext,
            description = null,
            title = null,
            properties = properties.mapValues { (_, property) ->
                property.copy(model = property.model.normalizedForSharingKey())
            },
            additionalProperties = when (val additional = additionalProperties) {
                is Model.Object.AdditionalProperties.Allowed -> additional
                is Model.Object.AdditionalProperties.Schema ->
                    Model.Object.AdditionalProperties.Schema(additional.value.normalizedForSharingKey())
            },
        )
        is Model.Primitive.Boolean -> copy(description = null, title = null)
        is Model.Primitive.Double -> copy(description = null, title = null)
        is Model.Primitive.Float -> copy(description = null, title = null)
        is Model.Primitive.Int -> copy(description = null, title = null)
        is Model.Primitive.Long -> copy(description = null, title = null)
        is Model.Primitive.String -> copy(description = null, title = null)
        is Model.Primitive.Unit -> copy(description = null, title = null)
        is Model.Reference -> copy(
            context = SharedNamingContext,
            description = null,
            title = null,
        )
        is Model.OneOf -> copy(
            context = SharedNamingContext,
            cases = cases.map { case ->
                case.copy(model = case.model.normalizedForSharingKey())
            },
            description = null,
            title = null,
        )
        is Model.AnyOf -> copy(
            context = SharedNamingContext,
            cases = cases.map { case ->
                case.copy(model = case.model.normalizedForSharingKey())
            },
            description = null,
            title = null,
        )
        is Model.Uuid -> copy(description = null, title = null)
    }
