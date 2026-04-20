package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.routes.SchemaContext

private data class NamedContextModel(
    val name: String,
    val context: SchemaContext,
    val model: Model,
)

internal fun List<Model>.contextSpecificNames(): Set<String> {
    val modelsByName = filterIsInstance<Model.ContextHolder>()
        .mapNotNull { holder ->
            val model = holder as? Model ?: return@mapNotNull null
            val reference = holder.context.head as? NamingContext.Reference ?: return@mapNotNull null
            NamedContextModel(reference.name, reference.context, model)
        }
        .groupBy(NamedContextModel::name)

    var splitNames = emptySet<String>()
    while (true) {
        val next = modelsByName
            .filter { (_, models) ->
                val readKeys = models
                    .asSequence()
                    .filter { it.context == SchemaContext.Read }
                    .map { it.model.contextComparisonKey(splitNames) }
                    .toSet()
                val writeKeys = models
                    .asSequence()
                    .filter { it.context == SchemaContext.Write }
                    .map { it.model.contextComparisonKey(splitNames) }
                    .toSet()
                readKeys.isNotEmpty() && writeKeys.isNotEmpty() && readKeys != writeKeys
            }
            .keys

        if (next == splitNames) return next
        splitNames = next
    }
}

private fun Model.contextComparisonKey(splitNames: Set<String>): String =
    InlineModelSharingJson.encodeToString(normalizedForContextComparison(splitNames))

private fun Model.normalizedForContextComparison(splitNames: Set<String>): Model =
    when (this) {
        is Model.Collection -> normalizedForContextComparison(splitNames)
        is Model.DiscriminatedObject -> normalizedForContextComparison(splitNames)
        is Model.Enum -> normalizedForContextComparison(splitNames)
        is Model.Object -> normalizedForContextComparison(splitNames)
        is Model.OneOf -> normalizedForContextComparison(splitNames)
        is Model.AnyOf -> normalizedForContextComparison(splitNames)
        is Model.ByteArray,
        is Model.Date,
        is Model.DateTime,
        is Model.FreeFormJson,
        is Model.Reference,
        is Model.Primitive,
        is Model.Uuid -> clearedContextComparisonMetadata(splitNames)
    }

private fun Model.clearedContextComparisonMetadata(splitNames: Set<String>): Model =
    if (this is Model.ByteArray) copy(description = null, title = null)
    else if (this is Model.Date) copy(description = null, title = null)
    else if (this is Model.DateTime) copy(description = null, title = null)
    else if (this is Model.FreeFormJson) copy(description = null, title = null)
    else if (this is Model.Primitive.Boolean) copy(description = null, title = null)
    else if (this is Model.Primitive.Double) copy(description = null, title = null)
    else if (this is Model.Primitive.Float) copy(description = null, title = null)
    else if (this is Model.Primitive.Int) copy(description = null, title = null)
    else if (this is Model.Primitive.Long) copy(description = null, title = null)
    else if (this is Model.Primitive.String) copy(description = null, title = null)
    else if (this is Model.Primitive.Unit) copy(description = null, title = null)
    else if (this is Model.Reference) copy(
        context = context.normalizedReferenceContext(splitNames),
        description = null,
        title = null,
    )
    else if (this is Model.Uuid) copy(description = null, title = null)
    else error("Unsupported model: $this")

private fun Model.Collection.normalizedForContextComparison(splitNames: Set<String>): Model = copy(
    inner = inner.normalizedForContextComparison(splitNames),
    description = null,
    title = null,
)

private fun Model.DiscriminatedObject.normalizedForContextComparison(splitNames: Set<String>): Model = copy(
    context = SharedNamingContext,
    abstractProperties = abstractProperties.mapValues { (_, property) ->
        property.copy(model = property.model.normalizedForContextComparison(splitNames))
    },
    subtypes = subtypes.map { subtype ->
        subtype.normalizedForContextComparison(splitNames) as Model.Object
    },
    description = null,
    title = null,
)

private fun Model.Enum.normalizedForContextComparison(splitNames: Set<String>): Model = copy(
    context = SharedNamingContext,
    inner = inner.normalizedForContextComparison(splitNames),
    description = null,
    title = null,
)

private fun Model.Object.normalizedForContextComparison(splitNames: Set<String>): Model = copy(
    context = SharedNamingContext,
    description = null,
    title = null,
    properties = properties.mapValues { (_, property) ->
        property.copy(model = property.model.normalizedForContextComparison(splitNames))
    },
    additionalProperties = when (val additional = additionalProperties) {
        is Model.Object.AdditionalProperties.Allowed -> additional
        is Model.Object.AdditionalProperties.Schema ->
            Model.Object.AdditionalProperties.Schema(additional.value.normalizedForContextComparison(splitNames))
    },
)

private fun Model.OneOf.normalizedForContextComparison(splitNames: Set<String>): Model = copy(
    context = SharedNamingContext,
    cases = cases.map { case ->
        case.copy(model = case.model.normalizedForContextComparison(splitNames))
    },
    description = null,
    title = null,
)

private fun Model.AnyOf.normalizedForContextComparison(splitNames: Set<String>): Model = copy(
    context = SharedNamingContext,
    cases = cases.map { case ->
        case.copy(model = case.model.normalizedForContextComparison(splitNames))
    },
    description = null,
    title = null,
)

private fun NamingContext.normalizedReferenceContext(splitNames: Set<String>): NamingContext =
    when (val reference = head as? NamingContext.Reference) {
        null -> SharedNamingContext
        else -> NamingContext.reference(
            reference.name,
            if (reference.name in splitNames) reference.context else SchemaContext.Null
        )
    }
