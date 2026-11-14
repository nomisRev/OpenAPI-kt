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
    is PathSegment.Composite -> composite()
}

/** A [PathSegment.Composite] is expanded into one step per part ([toSteps]) before a node or a class is named. */
private fun PathSegment.Composite.composite(): Nothing =
    error("The composite segment '$name' is navigated one step per part, and is named by those")

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
    is PathSegment.Composite -> composite()
}

/**
 * The class of a child [PathNode] and its navigation member in the parent's class. [suffix] is set when it is renamed
 * with a number, [separator] when it is renamed with the text that joins it to its parent.
 */
internal data class PathNodeName(
    val simpleName: String,
    val memberName: String,
    val suffix: Int?,
    val separator: String? = null,
)

private val SeparatorWords = mapOf(
    '.' to "dot", ':' to "colon", '-' to "dash", '_' to "underscore", ',' to "comma", ';' to "semicolon",
    '=' to "equals", '~' to "tilde", '@' to "at", '+' to "plus", '*' to "star", '#' to "hash", '!' to "bang",
    '$' to "dollar", '&' to "and", '|' to "pipe",
)

/**
 * `dot` for a step joined to the one before it by `.` (`{id}.json`), `colon` for `:`; the words of every character of
 * a longer text. `null` for a step after a slash, a step joined by nothing (`{a}{b}`), and text without a word.
 */
private fun PathNode.separatorWord(): String? {
    val text = joinedBy.removePrefix("/")
    if (joinedBy == "/" || text.isEmpty()) return null
    val words = text.map { SeparatorWords[it] ?: return null }
    return words.first() + words.drop(1).joinToString("") { it.replaceFirstChar(Char::uppercase) }
}

/**
 * The names of these sibling nodes, unique among them. Distinct segments can have one Kotlin name: `secret-scanning`
 * and `secret_scanning` (two nodes when they have an operation in common), `Users` and `users`, `a.b` and `a-b`,
 * `{accountId}` and `{account_id}`, or `slash` and the empty segment of a trailing slash. Class names are compared
 * ignoring case, since `Users.kt` and `USERS.kt` (and their `.class` files) are one file on a case-insensitive file
 * system.
 *
 * A step joined to its parent without a slash (`.json` of `{id}.json`) that has the name of a sibling is named with
 * its separator, whatever the order of the spec: next to `/hackathons/{id}/json`, which stays `json: Json`,
 * `/hackathons/{id}.json` is `dotJson: DotJson`, and `{name}:cancel` next to `{name}/cancel` is `colonCancel`.
 *
 * Of what still collides, the node declared first keeps the name, and a later one with a name already taken is
 * suffixed with the first number from 2 that is free, on both its class and member: `/secret-scanning` is
 * `secretScanning: SecretScanning` and `/secret_scanning` is `secretScanning2: SecretScanning2`. Every node that does
 * not collide keeps its name, so adding a colliding path to a spec renames only that path.
 */
internal fun List<PathNode>.uniqueNames(): List<PathNodeName> {
    val plain = map { node -> node.segment.routeSegmentSimpleName() to node.segment.navigationMemberName() }
    fun collides(index: Int): Boolean = plain.indices.any { other ->
        other != index && (
            plain[other].first.equals(plain[index].first, ignoreCase = true) ||
                plain[other].second.unescapeBackticks() == plain[index].second.unescapeBackticks()
            )
    }
    val separators = mapIndexed { index, node -> node.separatorWord()?.takeIf { collides(index) } }
    val bases = plain.mapIndexed { index, [simpleName, memberName] ->
        val word = separators[index] ?: return@mapIndexed simpleName to memberName
        val member = word + memberName.unescapeBackticks().replaceFirstChar(Char::uppercase)
        val escaped = if (memberName.startsWith("`") && !member.isValidParamName()) "`$member`" else member
        (word.replaceFirstChar(Char::uppercase) + simpleName) to escaped
    }
    val takenClasses = bases.mapTo(mutableSetOf()) { it.first.lowercase() }
    val takenMembers = bases.mapTo(mutableSetOf()) { it.second.unescapeBackticks() }
    val seenClasses = mutableSetOf<String>()
    val seenMembers = mutableSetOf<String>()
    return bases.mapIndexed { index, [simpleName, memberName] ->
        val separator = this[index].joinedBy.removePrefix("/").takeIf { separators[index] != null }
        val member = memberName.unescapeBackticks()
        val collides = !seenClasses.add(simpleName.lowercase()) or !seenMembers.add(member)
        if (!collides) return@mapIndexed PathNodeName(simpleName, memberName, suffix = null, separator = separator)
        val suffix = generateSequence(2) { it + 1 }.first { n ->
            "$simpleName$n".lowercase() !in takenClasses && "$member$n" !in takenMembers
        }
        takenClasses += "$simpleName$suffix".lowercase()
        takenMembers += "$member$suffix"
        val suffixed = "$member$suffix"
        // A literal's property is escaped as [toParamName] escapes it, a parameter's function name is left to KotlinPoet.
        val escaped = if (memberName.startsWith("`") && !suffixed.isValidParamName()) "`$suffixed`" else suffixed
        PathNodeName("$simpleName$suffix", escaped, suffix, separator)
    }
}
