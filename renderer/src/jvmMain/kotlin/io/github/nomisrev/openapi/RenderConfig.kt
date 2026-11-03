package io.github.nomisrev.openapi

data class RenderConfig(
    val modelPackage: String = "io.github.nomisrev.openapi.generated.model",
    val apiPackage: String = "io.github.nomisrev.openapi.generated.api",
    val targets: Set<KmpTarget> = setOf(KmpTarget.JVM, KmpTarget.JS),
    val contextSpecificNames: Set<String> = emptySet(),
    /**
     * The named models of the rendered [ApiTree], by the [NamingContext] a [Model.Reference] to them carries.
     * [generateModels] and [generateClient] fill it from [ApiTree.models] when it is empty.
     */
    val namedModels: Map<NamingContext, Model> = emptyMap(),
)

enum class KmpTarget {
    JVM,
    JS,
}

internal fun RenderConfig.withNamedModels(models: List<Model>): RenderConfig =
    if (namedModels.isNotEmpty()) this
    else copy(
        namedModels = buildMap {
            models.forEach { model ->
                if (model is Model.ContextHolder && model !is Model.Reference) put(model.context, model)
                if (model is Model.DiscriminatedObject) model.subtypes.forEach { put(it.context, it) }
            }
        }
    )

/** The named model [reference] points at, or `null` when it is not part of the rendered tree. */
internal fun RenderConfig.resolve(reference: Model.Reference): Model? =
    namedModels[reference.context]
        ?: (reference.context.head as? NamingContext.Reference)?.name?.let { name ->
            namedModels.entries.firstOrNull { [context, _] ->
                (context.head as? NamingContext.Reference)?.name == name && context.nested == reference.context.nested
            }?.value
        }
