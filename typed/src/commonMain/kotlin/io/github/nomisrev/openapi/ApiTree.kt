package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.Server
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.routes.toRoutes
import io.github.nomisrev.openapi.transformers.topLevelNames
import io.ktor.http.HttpMethod
import kotlin.collections.flatMapTo

data class ApiTree(
    val name: String,
    val operations: Map<HttpMethod, Route>,
    val children: List<PathNode>,
    val models: List<Model> = emptyList(),
    val servers: List<Server> = emptyList(),
)

data class PathNode(
    val segment: PathSegment,
    val operations: Map<HttpMethod, Route>,
    val children: List<PathNode>,
)

fun Iterable<Route>.buildTree(
    name: String,
    models: List<Model> = emptyList(),
    servers: List<Server> = emptyList(),
): ApiTree {
    val rootBuilder = PathNodeBuilder(segment = null)

    for (route in this) {
        rootBuilder.insert(route.segments, route)
    }

    return ApiTree(
        name = name,
        operations = rootBuilder.operations,
        children = rootBuilder.children.map { it.build() },
        models = models,
        servers = servers,
    )
}

private tailrec suspend fun Set<NamingContext.Reference>.topLevelModels(registry: Registry): List<Model> {
    val models = with(registry) { map { it.toModel() } }
    val newNames = models.flatMapTo(mutableSetOf()) { it.topLevelNames() }
    return if (newNames == this) models else newNames.topLevelModels(registry)
}

suspend fun OpenAPI.toApiTree(name: String = info.title.toPascalCase()): ApiTree =
    Registry(this).use { registry ->
        val globalServers = servers.normalizeForClientGeneration()
        val routes = with(registry) { toRoutes() }
        val models = with(registry) {
            routes
                .flatMapTo(mutableSetOf()) { it.topLevelNames() }
                .topLevelModels(registry)
        }
        routes.buildTree(name = name, models = models, servers = globalServers)
    }

private fun Route.topLevelNames(): Set<NamingContext.Reference> =
    parameters.topLevelNames() + returns.topLevelNames() + body.topLevelNames()

private fun Route.Bodies?.topLevelNames(): Set<NamingContext.Reference> =
    this?.types.orEmpty().flatMapTo(mutableSetOf()) { (_, body) ->
        when (body) {
            is Route.Body.Multipart.Value -> body.parameters.flatMap { it.type.topLevelNames() }
            is Route.Body.Multipart.Ref -> body.value.topLevelNames()
            is Route.Body.FormUrlEncoded -> body.parameters.flatMap { it.type.topLevelNames() }
            is Route.Body.SetBody -> body.type.topLevelNames()
            is Route.Body.OverloadedBody -> body.type.topLevelNames()
        }
    }

private fun Route.Returns.topLevelNames(): Set<NamingContext.Reference> {
    val defaultNested = default?.types?.values.orEmpty().flatMapTo(mutableSetOf()) { it.topLevelNames() }
    val responsesNested = responses.values.flatMapTo(mutableSetOf()) { returnType ->
        returnType.types.values.flatMap { it.topLevelNames() }
    }
    return defaultNested + responsesNested
}

private fun List<Route.Input>.topLevelNames(): Set<NamingContext.Reference> =
    flatMapTo(mutableSetOf()) { it.type.topLevelNames() }

private fun List<Server>.normalizeForClientGeneration(): List<Server> {
    if (size != 1) return this
    val only = first()
    val isImplicitDefault = only.url == "/" &&
        only.description == null &&
        only.variables.isNullOrEmpty() &&
        only.extensions.isNullOrEmpty()
    return if (isImplicitDefault) emptyList() else this
}

private class PathNodeBuilder(
    val segment: PathSegment?,
    val operations: MutableMap<HttpMethod, Route> = mutableMapOf(),
    val children: MutableList<PathNodeBuilder> = mutableListOf(),
) {
    fun insert(segments: List<PathSegment>, route: Route) {
        if (segments.isEmpty()) {
            operations[route.method] = route
            return
        }

        val head = segments.first()
        val tail = segments.drop(1)
        val existing = children.firstOrNull { it.segment.matches(head) }
        if (existing != null) {
            existing.insert(tail, route)
        } else {
            val child = PathNodeBuilder(head)
            children.add(child)
            child.insert(tail, route)
        }
    }

    fun build(): PathNode = PathNode(
        segment = segment!!, // root only
        operations = operations.toMap(),
        children = children.map { it.build() },
    )
}

private fun PathSegment?.matches(other: PathSegment): Boolean = when {
    this is PathSegment.Literal && other is PathSegment.Literal -> name == other.name
    this is PathSegment.Parameter && other is PathSegment.Parameter -> name == other.name
    this is PathSegment.Parameter && other is PathSegment.OverloadedParameter -> name == other.name
    this is PathSegment.OverloadedParameter && other is PathSegment.Parameter -> name == other.name
    this is PathSegment.OverloadedParameter && other is PathSegment.OverloadedParameter -> name == other.name
    else -> false
}
