package io.github.nomisrev.openapi

/**
 * Simple name for the child interface/class for this [PathNode].
 * Parameter nodes get a "Path" postfix; literal nodes use the raw PascalCase name.
 */
internal fun PathSegment.routeSegmentSimpleName(): String = when (this) {
    is PathSegment.Parameter -> name.toPascalCase() + "Path"
    is PathSegment.OverloadedParameter -> name.toPascalCase() + "Path"
    is PathSegment.FixedValue -> wireValue.toPascalCase()
    is PathSegment.Literal -> navigationName.toPascalCase()
}

/**
 * The name of a literal segment's navigation property, and of its class: `slash` for the empty segment of a trailing
 * slash (`/owners/`) or a double slash (`/a//b`), which is a node of its own only next to the same path without it
 * (`/owners`), as `api.owners.slash`.
 */
internal val PathSegment.Literal.navigationName: String
    get() = name.ifEmpty { "slash" }

/** The name of the navigation property, or function, from a node's class to this segment's child class. */
private fun PathSegment.navigationMemberName(): String = when (this) {
    is PathSegment.Parameter -> name.toCamelCase()
    is PathSegment.OverloadedParameter -> name.toCamelCase()
    is PathSegment.FixedValue -> name.toParamName()
    is PathSegment.Literal -> navigationName.toParamName()
}

/**
 * The class of a child [PathNode] and its navigation member in the parent's class, [suffix] when it is renamed.
 */
internal data class PathNodeName(val simpleName: String, val memberName: String, val suffix: Int?)

/**
 * The names of these sibling nodes, unique among them. Distinct segments can have one Kotlin name: `secret-scanning`
 * and `secret_scanning` (two nodes when they have an operation in common), `Users` and `users`, `a.b` and `a-b`,
 * `{accountId}` and `{account_id}`, or `slash` and the empty segment of a trailing slash. Class names are compared
 * ignoring case, since `Users.kt` and `USERS.kt` (and their `.class` files) are one file on a case-insensitive file
 * system.
 *
 * The node declared first keeps the name, and a later one with a name already taken is suffixed with the first
 * number from 2 that is free, on both its class and member: `/secret-scanning` is `secretScanning: SecretScanning` and
 * `/secret_scanning` is `secretScanning2: SecretScanning2`. Every node that does not collide keeps its name, so adding a
 * colliding path to a spec renames only that path, and the order of the spec decides deterministically which one it is.
 */
internal fun List<PathNode>.uniqueNames(): List<PathNodeName> {
    val bases = map { node -> node.segment.routeSegmentSimpleName() to node.segment.navigationMemberName() }
    val takenClasses = bases.mapTo(mutableSetOf()) { it.first.lowercase() }
    val takenMembers = bases.mapTo(mutableSetOf()) { it.second.unescapeBackticks() }
    val seenClasses = mutableSetOf<String>()
    val seenMembers = mutableSetOf<String>()
    return bases.map { [simpleName, memberName] ->
        val member = memberName.unescapeBackticks()
        val collides = !seenClasses.add(simpleName.lowercase()) or !seenMembers.add(member)
        if (!collides) return@map PathNodeName(simpleName, memberName, suffix = null)
        val suffix = generateSequence(2) { it + 1 }.first { n ->
            "$simpleName$n".lowercase() !in takenClasses && "$member$n" !in takenMembers
        }
        takenClasses += "$simpleName$suffix".lowercase()
        takenMembers += "$member$suffix"
        val suffixed = "$member$suffix"
        // A literal's property is escaped as [toParamName] escapes it, a parameter's function name is left to KotlinPoet.
        val escaped = if (memberName.startsWith("`") && !suffixed.isValidParamName()) "`$suffixed`" else suffixed
        PathNodeName("$simpleName$suffix", escaped, suffix)
    }
}
