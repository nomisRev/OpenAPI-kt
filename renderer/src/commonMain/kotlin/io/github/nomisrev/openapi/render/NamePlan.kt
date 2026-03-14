package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.API
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.Root
import io.github.nomisrev.openapi.routes.Route

internal data class RouteNameKey(
    val scopePath: List<String>,
    val operationId: String,
    val method: String,
    val path: String,
)

internal data class NamePlan(
    val apiNamesByPath: Map<List<String>, String>,
    val inlineNamesByContext: Map<NamingContext, String>,
    val resultNamesByRoute: Map<RouteNameKey, String>,
    val scopeDeclaredNames: Map<List<String>, Set<String>>,
) {
    fun apiSimpleName(path: List<String>): String {
        require(path.isNotEmpty()) { "Path must not be empty" }
        return apiNamesByPath[path] ?: path.last().toPascalCase()
    }

    fun apiQualifiedName(path: List<String>): String =
        pathSegments(path).joinToString(".")

    fun implName(path: List<String>): String =
        "Ktor" + pathSegments(path).joinToString("")

    fun inlineSimpleName(context: NamingContext): String? =
        inlineNamesByContext[context]

    fun resultTypeName(scopePath: List<String>, route: Route): String? =
        resultNamesByRoute[route.toNameKey(scopePath)]

    fun isNameVisibleInScope(scopePath: List<String>, name: String): Boolean =
        visibleScopeNames(scopePath).contains(name)

    fun mappedPathParts(parts: List<String>): List<String> =
        pathSegments(parts)

    private fun pathSegments(path: List<String>): List<String> =
        path.indices.map { idx ->
            val segmentPath = path.take(idx + 1)
            apiNamesByPath[segmentPath] ?: segmentPath.last().toPascalCase()
        }

    private fun visibleScopeNames(scopePath: List<String>): Set<String> =
        buildSet {
            for (depth in 0..scopePath.size) {
                addAll(scopeDeclaredNames[scopePath.take(depth)].orEmpty())
            }
        }
}

context(ctx: Renderer)
internal fun Root.buildNamePlan(): NamePlan {
    val planner = Planner()
    planner.planRoot(this)
    return planner.build()
}

context(ctx: Renderer)
internal fun API.buildNamePlanForFile(): NamePlan {
    val planner = Planner()
    planner.planApiFile(this)
    return planner.build()
}

private class Planner {
    private val apiNamesByPath = mutableMapOf<List<String>, String>()
    private val inlineNamesByContext = mutableMapOf<NamingContext, String>()
    private val resultNamesByRoute = mutableMapOf<RouteNameKey, String>()
    private val scopeDeclaredNames = mutableMapOf<List<String>, MutableSet<String>>()

    fun planRoot(root: Root) {
        val rootScope = emptyList<String>()
        scopeDeclaredNames.getOrPut(rootScope) { mutableSetOf() }.add(root.name.toPascalCase())
        visitScope(rootScope, root.operations, root.endpoints)
    }

    fun planApiFile(api: API) {
        val scopePath = listOf(api.name)
        val scopeSet = scopeDeclaredNames.getOrPut(scopePath) { mutableSetOf() }
        val selfName = api.name.toPascalCase()
        scopeSet.add(selfName)
        apiNamesByPath[scopePath] = selfName
        visitScope(scopePath, api.routes, api.nested)
    }

    fun build(): NamePlan = NamePlan(
        apiNamesByPath = apiNamesByPath,
        inlineNamesByContext = inlineNamesByContext,
        resultNamesByRoute = resultNamesByRoute,
        scopeDeclaredNames = scopeDeclaredNames.mapValues { (_, value) -> value.toSet() },
    )

    private fun visitScope(
        scopePath: List<String>,
        routes: List<Route>,
        children: List<API>,
    ) {
        scopeDeclaredNames.getOrPut(scopePath) { mutableSetOf() }

        children.forEach { child ->
            val childPath = scopePath + child.name
            val base = child.name.toPascalCase()
            val resolved = allocate(
                scopePath = scopePath,
                candidates = listOf(base, "${base}Api")
            )
            apiNamesByPath[childPath] = resolved
            scopeDeclaredNames.getOrPut(childPath) { mutableSetOf() }.add(resolved)
        }

        routes.inlineModelsForPlan().forEach { model ->
            val base = model.defaultSimpleName()
            val semanticCandidate = model.semanticInlineCandidate(base)
            val candidates = buildList {
                add(base)
                if (semanticCandidate != null && semanticCandidate != base) add(semanticCandidate)
            }
            val resolved = allocate(scopePath, candidates)
            inlineNamesByContext[model.context] = resolved
        }

        routes
            .filter { it.usesSealedReturnType() }
            .forEach { route ->
                val base = route.sealedResultTypeNameDefault()
                val resolved = allocate(scopePath, listOf(base))
                resultNamesByRoute[route.toNameKey(scopePath)] = resolved
            }

        children.forEach { child ->
            val childPath = scopePath + child.name
            visitScope(childPath, child.routes, child.nested)
        }
    }

    private fun allocate(scopePath: List<String>, candidates: List<String>): String {
        require(candidates.isNotEmpty()) { "At least one candidate is required" }

        val used = visibleScopeNames(scopePath).toMutableSet()
        for (candidate in candidates) {
            if (used.add(candidate)) {
                scopeDeclaredNames.getOrPut(scopePath) { mutableSetOf() }.add(candidate)
                return candidate
            }
        }

        val seed = candidates.last()
        var index = 2
        while (true) {
            val candidate = "$seed$index"
            if (used.add(candidate)) {
                scopeDeclaredNames.getOrPut(scopePath) { mutableSetOf() }.add(candidate)
                return candidate
            }
            index += 1
        }
    }

    private fun visibleScopeNames(scopePath: List<String>): Set<String> =
        buildSet {
            for (depth in 0..scopePath.size) {
                addAll(scopeDeclaredNames[scopePath.take(depth)].orEmpty())
            }
        }
}

private fun List<Route>.inlineModelsForPlan(): List<Model.ContextHolder> =
    asSequence()
        .flatMap { it.nested.asSequence() }
        .filterIsInstance<Model.ContextHolder>()
        .distinctBy { it.context }
        .sortedBy { it.context.toString() }
        .toList()

private fun Model.ContextHolder.defaultSimpleName(): String {
    val nested = context.nested.lastOrNull() ?: return when (val head = context.head) {
        is NamingContext.Path -> head.parts.lastOrNull()?.toPascalCase() ?: "Type"
        is NamingContext.Reference -> head.name.toPascalCase()
    }

    return when (nested) {
        NamingContext.AdditionalProperties -> "Additional"
        is NamingContext.DiscriminatedObjectCase -> nested.discriminator.toPascalCase()
        is NamingContext.ObjectProperty -> nested.name.toPascalCase()
        is NamingContext.Response -> "${nested.operationId.toPascalCase()}Response"
        is NamingContext.RouteBody -> "${nested.operationId.toPascalCase()}Body"
        is NamingContext.RouteParam -> nested.name.toPascalCase()
        is NamingContext.UnionCase -> nested.value.toPascalCase()
    }
}

private fun Model.ContextHolder.semanticInlineCandidate(base: String): String? {
    val nested = context.nested.lastOrNull() ?: return null
    return when (nested) {
        is NamingContext.RouteParam -> "${nested.operationId.toPascalCase()}$base"
        is NamingContext.UnionCase -> "${base}Case"
        is NamingContext.DiscriminatedObjectCase -> "${base}Case"
        else -> null
    }
}

internal fun Route.toNameKey(scopePath: List<String>): RouteNameKey =
    RouteNameKey(
        scopePath = scopePath,
        operationId = operationId,
        method = method.value,
        path = path,
    )

internal fun Route.sealedResultTypeNameDefault(): String =
    "${operationId.toPascalCase()}Result"
