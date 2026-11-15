package io.github.nomisrev.openapi

import io.ktor.http.HttpMethod

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
    /**
     * The simple name of the generated tri-state type of optional and nullable properties, `Optional` unless a
     * top-level class of the rendered tree has that name. [generateModels] and [generateClient] choose it when it is
     * `null`.
     */
    val optionalTypeName: String? = null,
    /**
     * The classes of the path nodes down to each operation of the rendered [ApiTree], by the operation's path template
     * and method. A node's class is not always named after its segment alone ([uniqueNames] renames a sibling with a
     * name already taken), and a type declared inline in an operation is nested in these classes.
     * [generateModels] and [generateClient] fill it when it is empty.
     */
    val pathClassNames: Map<Pair<String, String>, List<String>> = emptyMap(),
    /**
     * The simple class name of each schema, by the schema's name and the suffix of its context (`""`, `"Read"` or
     * `"Write"`). Several schemas can have one Kotlin name (`_metadata` and `metadata`, `HTTPHealthCheck` and
     * `HttpHealthCheck`), so a name is not always the schema's own, see [modelClassNames]. [generateModels] and
     * [generateClient] fill it when it is empty; a schema without an entry has its own name.
     */
    val modelClassNames: Map<Pair<String, String>, String> = emptyMap(),
)

enum class KmpTarget {
    JVM,
    JS,
}

/**
 * The config the rendering of [tree] uses: the class names of its schemas, the tri-state type's name (which avoids
 * them), its named models and its path classes. Each is filled once.
 */
internal fun RenderConfig.withResolvedNames(tree: ApiTree): RenderConfig =
    withModelClassNames(tree.models).withOptionalTypeName(tree).withNamedModels(tree.models).withPathClassNames(tree)

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

internal fun RenderConfig.withPathClassNames(tree: ApiTree): RenderConfig =
    if (pathClassNames.isNotEmpty()) this
    else copy(
        pathClassNames = buildMap {
            fun visit(nodes: List<PathNode>, operations: Set<HttpMethod>, parents: List<String>) {
                nodes.zip(nodes.uniqueNames(operations)).forEach { [node, name] ->
                    val classes = parents + name.simpleName
                    node.operations.forEach { [method, route] -> put(route.segments.pathClassKey() to method.value, classes) }
                    visit(node.children, node.operations.keys, classes)
                }
            }
            visit(tree.children, tree.operations.keys, emptyList())
        }
    )

/** The path template of these segments, as the key of [RenderConfig.pathClassNames]. */
internal fun List<PathSegment>.pathClassKey(): String = joinToString("/") { segment ->
    when (segment) {
        is PathSegment.FixedValue -> segment.wireValue
        is PathSegment.Literal -> segment.name
        is PathSegment.Parameter -> "{${segment.name}}"
        is PathSegment.OverloadedParameter -> "{${segment.name}}"
        is PathSegment.Composite -> segment.name
    }
}
