package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.Server
import io.github.nomisrev.openapi.routes.Route
import io.ktor.http.HttpMethod

data class ApiTree(
    val name: String,
    val operations: Map<HttpMethod, Route>,
    val children: List<PathNode>,
    val servers: List<Server> = emptyList(),
)

data class PathNode(
    val segment: PathSegment,
    val operations: Map<HttpMethod, Route>,
    val children: List<PathNode>,
)

fun Iterable<Route>.buildTree(name: String, servers: List<Server> = emptyList()): ApiTree {
    val rootBuilder = PathNodeBuilder(segment = null)

    for (route in this) {
        rootBuilder.insert(route.segments, route)
    }

    return ApiTree(
        name = name,
        operations = rootBuilder.operations,
        children = rootBuilder.children.map { it.build() },
        servers = servers,
    )
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
    else -> false
}
