package io.github.nomisrev.openapi

/**
 * Simple name for the child interface/class for this [PathNode].
 * Parameter nodes get a "Path" postfix; literal nodes use the raw PascalCase name.
 */
internal fun PathSegment.routeSegmentSimpleName(): String = when (this) {
    is PathSegment.Parameter -> name.toPascalCase() + "Path"
    is PathSegment.OverloadedParameter -> name.toPascalCase() + "Path"
    is PathSegment.FixedValue -> wireValue.pathNameSource().toPascalCase()
    is PathSegment.Literal -> navigationName.pathNameSource().toPascalCase()
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
    is PathSegment.FixedValue -> name.pathNameSource().toParamName()
    is PathSegment.Literal -> navigationName.pathNameSource().toParamName()
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
    /** Set when the node is one of several declarations of a parameter as different types, named by its type. */
    val typeWord: String? = null,
)

/**
 * The word a parameter node is named with next to a sibling that declares the same parameter as another type:
 * `String`, `Long`, `Uuid`, `IntList`. `null` for a type without a word of its own.
 */
private fun Model.pathTypeWord(): String? = when (this) {
    is Model.Primitive.String -> "String"
    is Model.Primitive.Int -> "Int"
    is Model.Primitive.Long -> "Long"
    is Model.Primitive.Float -> "Float"
    is Model.Primitive.Double -> "Double"
    is Model.Primitive.Boolean -> "Boolean"
    is Model.Uuid -> "Uuid"
    is Model.Date -> "Date"
    is Model.DateTime -> "DateTime"
    is Model.Collection -> inner.pathTypeWord()?.let { it + "List" }
    is Model.Reference -> (context.head as? NamingContext.Reference)?.name?.toPascalCase()
    else -> null
}

/**
 * The type words of the parameter nodes that are declarations of one parameter as different types, by index. Such
 * siblings keep one member name, as overloads of one navigation function, and their classes are named with the type.
 * A group is left out, and numbered like any other collision, when a type has no word, two have the same, or more
 * than one is a list (lists of different elements are one signature on the JVM).
 */
private fun List<PathNode>.typeWords(memberNames: List<String>): Map<Int, String> =
    indices.filter { this[it].segment is PathSegment.Parameter }
        .groupBy { memberNames[it].unescapeBackticks() to this[it].joinedBy }
        .values
        .filter { it.size > 1 }
        .flatMap { group ->
            val models = group.map { (this[it].segment as PathSegment.Parameter).model }
            val words = models.map { it.pathTypeWord() }
            val named = words.none { it == null } && words.distinct().size == words.size &&
                models.count { it is Model.Collection } <= 1
            if (named) group.zip(words.filterNotNull()) else emptyList()
        }
        .toMap()

private val SeparatorWords = mapOf(
    '.' to "dot", ':' to "colon", '-' to "dash", '_' to "underscore", ',' to "comma", ';' to "semicolon",
    '=' to "equals", '~' to "tilde", '@' to "at", '+' to "plus", '*' to "star", '#' to "hash", '!' to "bang",
    '$' to "dollar", '&' to "and", '|' to "pipe",
    // The rest of what a path segment can hold without a letter or digit, so that it can be spelled ([pathNameSource]).
    '\'' to "apostrophe", '(' to "openParen", ')' to "closeParen", '%' to "percent", '[' to "openBracket",
    ']' to "closeBracket", '{' to "openBrace", '}' to "closeBrace", '?' to "question", '<' to "less",
    '>' to "greater", '^' to "caret", '"' to "quote", '\\' to "backslash", '`' to "backtick",
    ' ' to "space",
)

/**
 * The text a literal segment is named from, for its class and its member, never for the request. A character that is
 * not a letter or a digit is a word break (`secret-scanning`, `a@b` is `a b`), so the name has no character that
 * needs back-ticks. A segment without any letter or digit (`-`, `*`, `...`) is spelled with the [SeparatorWords] of
 * its characters (`dash`, `star`, `dotDotDot`), and a character without a word by its code (`u20ac`).
 */
internal fun String.pathNameSource(): String {
    if (any(Char::isLetterOrDigit)) return map { if (it.isLetterOrDigit()) it else ' ' }.joinToString("")
    return map { SeparatorWords[it] ?: "u${it.code.toString(16)}" }
        .joinToString("") { it.replaceFirstChar(Char::uppercase) }
        .replaceFirstChar(Char::lowercase)
}

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
 * Parameter nodes that declare one parameter as different types (`{id}` a string for one operation and an integer for
 * another) keep one member name, as overloads of one navigation function, and each class is named with its type,
 * whatever the order of the spec: `id(id: String): IdStringPath` and `id(id: Long): IdLongPath`.
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
    val separated = plain.mapIndexed { index, [simpleName, memberName] ->
        val word = separators[index] ?: return@mapIndexed simpleName to memberName
        val member = word + memberName.unescapeBackticks().replaceFirstChar(Char::uppercase)
        val escaped = if (memberName.startsWith("`") && !member.isValidParamName()) "`$member`" else member
        (word.replaceFirstChar(Char::uppercase) + simpleName) to escaped
    }
    val typeWords = typeWords(plain.map { it.second })
    val bases = separated.mapIndexed { index, [simpleName, memberName] ->
        val word = typeWords[index] ?: return@mapIndexed simpleName to memberName
        (simpleName.removeSuffix("Path") + word + "Path") to memberName
    }
    val takenClasses = bases.mapTo(mutableSetOf()) { it.first.lowercase() }
    val takenMembers = bases.mapTo(mutableSetOf()) { it.second.unescapeBackticks() }
    val seenClasses = mutableSetOf<String>()
    val seenMembers = mutableSetOf<String>()
    return bases.mapIndexed { index, [simpleName, memberName] ->
        val separator = this[index].joinedBy.removePrefix("/").takeIf { separators[index] != null }
        val member = memberName.unescapeBackticks()
        val typeWord = typeWords[index]
        // Declarations of one parameter as different types are overloads of one member, so only their classes can collide.
        val memberTaken = !seenMembers.add(member) && typeWord == null
        val collides = !seenClasses.add(simpleName.lowercase()) or memberTaken
        if (!collides) {
            return@mapIndexed PathNodeName(simpleName, memberName, suffix = null, separator = separator, typeWord = typeWord)
        }
        val suffix = generateSequence(2) { it + 1 }.first { n ->
            "$simpleName$n".lowercase() !in takenClasses && "$member$n" !in takenMembers
        }
        takenClasses += "$simpleName$suffix".lowercase()
        takenMembers += "$member$suffix"
        val suffixed = "$member$suffix"
        // A literal's property is escaped as [toParamName] escapes it, a parameter's function name is left to KotlinPoet.
        val escaped = if (memberName.startsWith("`") && !suffixed.isValidParamName()) "`$suffixed`" else suffixed
        PathNodeName("$simpleName$suffix", escaped, suffix, separator, typeWord)
    }
}
